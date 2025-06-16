// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package util;

import java.time.LocalDateTime;

import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * A simple data holder for capturing user input from the event creation form.
 * Encapsulates all the necessary fields required to create a single calendar event,
 * including required and optional attributes.
 */
public class EventFormDataUtil {
  /**
   * Parses a LocalDateTime from dropdown components.
   *
   * @param yearBox   the year dropdown
   * @param monthBox  the month dropdown
   * @param dayBox    the day dropdown
   * @param hourBox   the hour dropdown
   * @param minuteBox the minute dropdown
   * @return the resulting LocalDateTime
   * @throws IllegalArgumentException if any field is null or unselected
   */
  public static LocalDateTime parseDateTime(
          JComboBox<Integer> yearBox,
          JComboBox<Integer> monthBox,
          JComboBox<Integer> dayBox,
          JComboBox<Integer> hourBox,
          JComboBox<Integer> minuteBox) {

    if (yearBox.getSelectedItem() == null ||
            monthBox.getSelectedItem() == null ||
            dayBox.getSelectedItem() == null ||
            hourBox.getSelectedItem() == null ||
            minuteBox.getSelectedItem() == null) {
      throw new IllegalArgumentException("All date and time fields must be selected.");
    }

    int year = (int) yearBox.getSelectedItem();
    int month = (int) monthBox.getSelectedItem();
    int day = (int) dayBox.getSelectedItem();
    int hour = (int) hourBox.getSelectedItem();
    int minute = (int) minuteBox.getSelectedItem();

    return java.time.LocalDateTime.of(year, month, day, hour, minute);
  }

  /**
   * Extracts trimmed text from a JTextField.
   *
   * @param field the text field
   * @return the trimmed string, or an empty string if null
   */
  public static String extractText(JTextField field) {
    return (field == null || field.getText() == null)
            ? ""
            : field.getText().trim();
  }

  /**
   * Validates that the given field is not empty.
   *
   * @param field the text field to check
   * @param label the name of the field (for error messaging)
   * @throws IllegalArgumentException if the field is empty
   */
  public static void requireNonEmpty(JTextField field, String label) {
    if (extractText(field).isEmpty()) {
      throw new IllegalArgumentException(label + " cannot be empty.");
    }
  }
}