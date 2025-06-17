// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package view;

import model.EventStatus;
import model.IEvent;
import model.ROIEvent;
import util.DateDropDownUtil;
import util.EventFormDataUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import java.awt.GridLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * A form panel for selecting and editing an existing event.
 */
public class EditEventPanel extends JPanel {
  private JCheckBox privateCheckBox;
  private JCheckBox allDayBox;
  private JComboBox<ROIEvent> eventSelector;
  private JTextField subjectField;
  private JTextField locationField;
  private JTextArea descriptionArea;
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

  /**
   * Constructs the form panel for editing an existing event.
   * Initializes fields, lays out the UI components, and wires event listeners.
   */
  public EditEventPanel() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBorder(BorderFactory.createTitledBorder("Edit Event"));

    initFields();
    layoutFields();
    addListeners();
  }

  /**
   * Initializes all editable input fields and dropdowns.
   */
  private void initFields() {
    this.eventSelector = new JComboBox<>();
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
   * Arranges all input fields and dropdowns into a labeled vertical layout.
   */
  private void layoutFields() {
    this.add(labeledField("Select Event to Edit:", this.eventSelector));
    this.add(labeledField("New Subject:", this.subjectField));
    this.add(labeledField("New Location:", this.locationField));
    this.add(labeledField("New Description:", new JScrollPane(this.descriptionArea)));

    this.add(labeledField("New Start Date & Time:", createLabeledDropdownRow(
            new String[]{"Year", "Month", "Day", "Hour", "Minute"},
            new JComboBox[]{this.startYear, this.startMonth, this.startDay, this.startHour,
                    this.startMinute
            }
    )));

    this.add(labeledField("New End Date & Time:", createLabeledDropdownRow(
            new String[]{"Year", "Month", "Day", "Hour", "Minute"},
            new JComboBox[]{this.endYear, this.endMonth, this.endDay, this.endHour, this.endMinute
            }
    )));

    this.add(this.allDayBox);
    this.add(this.privateCheckBox);
  }

  /**
   * Attaches checkbox and dropdown listeners to the form.
   * Includes:
   * - Disabling time/date fields when "All-Day" is selected
   * - Auto-filling the form when an event is selected
   */
  private void addListeners() {
    addAllDayCheckboxListener();
    addEventSelectorListener();
  }

  /**
   * Attaches a listener to the "All-Day Event" checkbox.
   * Disables/enables start and end time/date fields based on its state.
   */
  private void addAllDayCheckboxListener() {
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
   * Attaches a listener to the event dropdown.
   * When an event is selected, pre-fills the form with its values
   * and detects if it's an all-day event to update the checkbox and time field states.
   */
  private void addEventSelectorListener() {
    this.eventSelector.addActionListener(e -> {
      model.IEvent selected = (model.IEvent) this.eventSelector.getSelectedItem();
      if (selected == null) {
        return;
      }

      fillEventForm(selected);
      detectAndApplyAllDayState(selected);
    });
  }

  /**
   * Pre-fills the form with the selected event's data.
   *
   * @param selected the event selected from the dropdown
   */
  private void fillEventForm(model.IEvent selected) {
    this.subjectField.setText(selected.getSubject());
    this.locationField.setText(selected.getLocation());
    this.descriptionArea.setText(selected.getDescription());

    LocalDateTime start = selected.getStart();
    LocalDateTime end = selected.getEnd();

    this.startYear.setSelectedItem(start.getYear());
    this.startMonth.setSelectedItem(start.getMonthValue());
    this.startDay.setSelectedItem(start.getDayOfMonth());
    this.startHour.setSelectedItem(start.getHour());
    this.startMinute.setSelectedItem(start.getMinute());

    this.endYear.setSelectedItem(end.getYear());
    this.endMonth.setSelectedItem(end.getMonthValue());
    this.endDay.setSelectedItem(end.getDayOfMonth());
    this.endHour.setSelectedItem(end.getHour());
    this.endMinute.setSelectedItem(end.getMinute());
    this.privateCheckBox.setSelected(
            selected.getStatus().equals(EventStatus.PRIVATE)
    );
  }

  /**
   * Detects if the event follows the all-day 8AM–5PM pattern
   * and updates the all-day checkbox and disables fields accordingly.
   *
   * @param selected the event selected from the dropdown
   */
  private void detectAndApplyAllDayState(model.IEvent selected) {
    LocalDateTime start = selected.getStart();
    LocalDateTime end = selected.getEnd();

    boolean isAllDay =
            start.toLocalTime().equals(LocalTime.of(8, 0)) &&
                    end.toLocalTime().equals(LocalTime.of(17, 0)) &&
                    start.toLocalDate().equals(end.toLocalDate());

    this.allDayBox.setSelected(isAllDay);

    this.startHour.setEnabled(!isAllDay);
    this.startMinute.setEnabled(!isAllDay);
    this.endYear.setEnabled(!isAllDay);
    this.endMonth.setEnabled(!isAllDay);
    this.endDay.setEnabled(!isAllDay);
    this.endHour.setEnabled(!isAllDay);
    this.endMinute.setEnabled(!isAllDay);
  }

  /**
   * Populates the event selector with a list of events.
   *
   * @param events the list of events to edit
   */
  public void loadEvents(List<ROIEvent> events) {
    this.eventSelector.removeAllItems();

    for (ROIEvent event : events) {
      this.eventSelector.addItem(event);
    }

    if (!events.isEmpty()) {
      this.eventSelector.setSelectedIndex(0);
    }
  }

  /**
   * Clears the form and selection.
   */
  public void resetForm() {
    this.eventSelector.setSelectedIndex(-1); // deselect event

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
      row.add(new JLabel(label, JLabel.CENTER));
    }

    for (JComboBox<?> box : boxes) {
      row.add(box);
    }

    return row;
  }

  /**
   * Returns the selected event to edit.
   *
   * @return the selected event
   */
  public IEvent getSelectedEvent() {
    return (IEvent) this.eventSelector.getSelectedItem();
  }

  /**
   * Returns the updated subject entered in the form.
   *
   * @return the new subject
   */
  public String getSubject() {
    return this.subjectField.getText().trim();
  }

  /**
   * Returns the updated location entered in the form.
   *
   * @return the new location
   */
  public String getLocationText() {
    return this.locationField.getText().trim();
  }

  /**
   * Returns the updated description entered in the form.
   *
   * @return the new description
   */
  public String getDescription() {
    return this.descriptionArea.getText().trim();
  }

  /**
   * Returns the new start date and time selected.
   *
   * @return the new start LocalDateTime
   */
  public LocalDateTime getStartDateTime() {
    return EventFormDataUtil.parseDateTime(
            this.startYear, this.startMonth, this.startDay, this.startHour, this.startMinute);
  }

  /**
   * Returns the new end date and time selected.
   *
   * @return the new end LocalDateTime
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