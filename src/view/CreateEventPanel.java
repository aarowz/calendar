// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package view;

import java.awt.FlowLayout;
import java.awt.Component;
import java.time.LocalDateTime;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import util.DateDropDownUtil;
import util.EventFormDataUtil;

/**
 * A form panel used to collect input for creating a new calendar event.
 */
public class CreateEventPanel extends JPanel implements ICreateEventPanel {
  private JCheckBox privateCheckBox;
  private JCheckBox allDayBox;
  private JTextField subjectField;
  private JComboBox<Integer> startYear;
  private JComboBox<Integer> startMonth;
  private JComboBox<Integer> startDay;
  private JComboBox<Integer> startHour;
  private JComboBox<Integer> startMinute;
  private JComboBox<Integer> endYear;
  private JComboBox<Integer> endMonth;
  private JComboBox<Integer> endDay;
  private JComboBox<Integer> endHour;
  private JComboBox<Integer> endMinute;
  private JTextField locationField;
  private JTextArea descriptionArea;

  /**
   * Constructs the form panel for creating an event.
   * Initializes all UI components, arranges them in layout,
   * and wires listeners for interactive behavior (e.g. All-Day checkbox).
   */
  public CreateEventPanel() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBorder(BorderFactory.createTitledBorder("New Event"));

    initFields();
    layoutFields();
    addListeners();
  }

  /**
   * Initializes all form input fields and dropdown components.
   */
  private void initFields() {
    this.allDayBox = new JCheckBox("All-Day Event");
    this.subjectField = new JTextField(20);
    this.locationField = new JTextField(20);
    this.descriptionArea = new JTextArea(4, 20);

    this.startYear = DateDropDownUtil.createYearDropdown();
    this.startMonth = DateDropDownUtil.createMonthDropdown();
    this.startDay = DateDropDownUtil.createDayDropdown();
    this.startHour = DateDropDownUtil.createHourDropdown();
    this.startMinute = DateDropDownUtil.createMinuteDropdown();

    this.endYear = DateDropDownUtil.createYearDropdown();
    this.endMonth = DateDropDownUtil.createMonthDropdown();
    this.endDay = DateDropDownUtil.createDayDropdown();
    this.endHour = DateDropDownUtil.createHourDropdown();
    this.endMinute = DateDropDownUtil.createMinuteDropdown();
    this.privateCheckBox = new JCheckBox("Private Event");
  }

  /**
   * Arranges all input fields into the form layout with aligned labels.
   */
  private void layoutFields() {
    this.add(labeledField("Subject:", this.subjectField));
    this.add(labeledField("Location:", this.locationField));
    this.add(labeledField("Description:", new JScrollPane(this.descriptionArea)));

    this.add(labeledField("Start:", createLabeledDropdownRow(
            new String[]{"Year", "Month", "Day", "Hour", "Minute"},
            new JComboBox[]{this.startYear, this.startMonth, this.startDay, this.startHour,
                            this.startMinute
            }
    )));

    this.add(labeledField("End:", createLabeledDropdownRow(
            new String[]{"Year", "Month", "Day", "Hour", "Minute"},
            new JComboBox[]{this.endYear, this.endMonth, this.endDay, this.endHour, this.endMinute}
    )));

    this.add(this.allDayBox);
    this.add(this.privateCheckBox);
  }

  /**
   * Attaches interactive behaviors such as enabling/disabling fields
   * based on checkbox selections (e.g. all-day events).
   */
  private void addListeners() {
    this.allDayBox.addActionListener(e -> {
      boolean isAllDay = this.allDayBox.isSelected();

      this.startHour.setEnabled(!isAllDay);
      this.startMinute.setEnabled(!isAllDay);

      this.endYear.setEnabled(!isAllDay);
      this.endMonth.setEnabled(!isAllDay);
      this.endDay.setEnabled(!isAllDay);
      this.endHour.setEnabled(!isAllDay);
      this.endMinute.setEnabled(!isAllDay);
    });
  }

  /**
   * Clears all fields in the form.
   */
  public void resetForm() {
    this.subjectField.setText("");
    this.locationField.setText("");
    this.descriptionArea.setText("");

    this.startYear.setSelectedIndex(0);
    this.startMonth.setSelectedIndex(0);
    this.startDay.setSelectedIndex(0);
    this.startHour.setSelectedIndex(0);
    this.startMinute.setSelectedIndex(0);

    this.endYear.setSelectedIndex(0);
    this.endMonth.setSelectedIndex(0);
    this.endDay.setSelectedIndex(0);
    this.endHour.setSelectedIndex(0);
    this.endMinute.setSelectedIndex(0);
  }

  /**
   * Utility panel that allows time dropdown to be easily differentiable.
   *
   * @param labels the given labels
   * @param boxes  the given dropdown boxes
   * @return the labeled version of the dropdown boxes
   */
  private JPanel createLabeledDropdownRow(String[] labels, JComboBox<?>[] boxes) {
    JPanel row = new JPanel();
    row.setLayout(new GridLayout(2, boxes.length, 5, 2));

    for (String label : labels) {
      row.add(new JLabel(label, SwingConstants.CENTER));
    }

    for (JComboBox<?> box : boxes) {
      row.add(box);
    }

    return row;
  }

  /**
   * Creates a labeled field that moves the headers to the left of the screen for better display.
   *
   * @param label the given label
   * @param field the given field
   * @return the cleanly formatted label
   */
  private JPanel labeledField(String label, Component field) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.add(new JLabel(label));
    panel.add(field);
    return panel;
  }

  /**
   * Returns the subject entered in the form.
   *
   * @return the event subject
   */
  public String getSubject() {
    return this.subjectField.getText().trim();
  }

  /**
   * Returns the location entered in the form.
   *
   * @return the event location
   */
  public String getLocationText() {
    return this.locationField.getText().trim();
  }

  /**
   * Returns the description entered in the form.
   *
   * @return the event description
   */
  public String getDescription() {
    return this.descriptionArea.getText().trim();
  }

  /**
   * Returns the start date and time selected in the form.
   *
   * @return the event start LocalDateTime
   */
  public LocalDateTime getStartDateTime() {
    return EventFormDataUtil.parseDateTime(
            this.startYear, this.startMonth, this.startDay, this.startHour, this.startMinute);
  }

  /**
   * Returns the end date and time selected in the form.
   *
   * @return the event end LocalDateTime
   */
  public LocalDateTime getEndDateTime() {
    return EventFormDataUtil.parseDateTime(
            this.endYear, this.endMonth, this.endDay, this.endHour, this.endMinute);
  }

  /**
   * Returns whether the all-day event checkbox is selected.
   *
   * @return true if the event is all-day
   */
  public boolean isAllDayEvent() {
    return this.allDayBox.isSelected();
  }

  /**
   * Returns whether the private event checkbox is selected.
   *
   * @return true if the event is private
   */
  public boolean isPrivateEvent() {
    return this.privateCheckBox.isSelected();
  }
}