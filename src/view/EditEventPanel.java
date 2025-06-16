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
  private final JCheckBox allDayBox;
  private final JComboBox<ROIEvent> eventSelector;
  private final JTextField subjectField;
  private final JTextField locationField;
  private final JTextArea descriptionArea;
  private final JCheckBox isPrivateBox;

  private final JComboBox<Integer> startYear;
  private final JComboBox<Integer> startMonth;
  private final JComboBox<Integer> startDay;
  private final JComboBox<Integer> startHour;
  private final JComboBox<Integer> startMinute;

  private final JComboBox<Integer> endYear;
  private final JComboBox<Integer> endMonth;
  private final JComboBox<Integer> endDay;
  private final JComboBox<Integer> endHour;
  private final JComboBox<Integer> endMinute;

  /**
   * Constructs the form panel for editing an existing event.
   */
  public EditEventPanel() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBorder(BorderFactory.createTitledBorder("Edit Event"));

    this.eventSelector = new JComboBox<>();
    this.allDayBox = new JCheckBox("All-Day Event");
    this.subjectField = new JTextField(20);
    this.locationField = new JTextField(20);
    this.descriptionArea = new JTextArea(4, 20);
    this.isPrivateBox = new JCheckBox("Private Event");

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

    // add layout
    this.add(labeledField("Select Event to Edit:", this.eventSelector));
    this.add(labeledField("New Subject:", this.subjectField));
    this.add(labeledField("New Location:", this.locationField));
    this.add(labeledField("New Description:", new JScrollPane(this.descriptionArea)));

    this.add(labeledField("New Start Date & Time:", createLabeledDropdownRow(
            new String[]{"Year", "Month", "Day", "Hour", "Minute"},
            new JComboBox[]{
                    this.startYear, this.startMonth, this.startDay,
                    this.startHour, this.startMinute
            }
    )));

    this.add(labeledField("New End Date & Time:", createLabeledDropdownRow(
            new String[]{"Year", "Month", "Day", "Hour", "Minute"},
            new JComboBox[]{
                    this.endYear, this.endMonth, this.endDay,
                    this.endHour, this.endMinute
            }
    )));

    this.add(this.isPrivateBox);
    this.add(this.allDayBox);

    // all-Day checkbox behavior
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

    // autofill form when an event is selected
    this.eventSelector.addActionListener(e -> {
      model.IEvent selected = (model.IEvent) this.eventSelector.getSelectedItem();
      if (selected == null) return;

      this.subjectField.setText(selected.getSubject());
      this.locationField.setText(selected.getLocation());
      this.descriptionArea.setText(selected.getDescription());
      this.isPrivateBox.setSelected(selected.getStatus() == model.EventStatus.PRIVATE);

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

      // detect all-day pattern and pre-check the box
      boolean isAllDay =
              start.toLocalTime().equals(LocalTime.of(8, 0)) &&
                      end.toLocalTime().equals(LocalTime.of(17, 0)) &&
                      start.toLocalDate().equals(end.toLocalDate());

      this.allDayBox.setSelected(isAllDay);

      // trigger time field enabling/disabling
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
    this.isPrivateBox.setSelected(false);

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
   * Returns whether the form inputs are valid.
   *
   * @return true if valid, false otherwise
   */
  public boolean isValidForm() {
    // Must select an event to edit
    if (this.eventSelector.getSelectedItem() == null) {
      return false;
    }

    // Subject is required
    if (this.subjectField.getText().trim().isEmpty()) {
      return false;
    }

    try {
      LocalDateTime start = EventFormDataUtil.parseDateTime(
              this.startYear, this.startMonth, this.startDay, this.startHour, this.startMinute);
      LocalDateTime end = EventFormDataUtil.parseDateTime(
              this.endYear, this.endMonth, this.endDay, this.endHour, this.endMinute);

      return start.isBefore(end);
    } catch (Exception e) {
      return false; // date parsing failure
    }
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
   * Returns the selected privacy status.
   *
   * @return the new event status
   */
  public EventStatus getStatus() {
    return this.isPrivateBox.isSelected()
            ? EventStatus.PRIVATE
            : EventStatus.PUBLIC;
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
}