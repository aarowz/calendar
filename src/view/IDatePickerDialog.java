package view;

import java.time.LocalDate;

/**
 * Interface for a modal dialog that allows the user to select a specific date.
 */
public interface IDatePickerDialog {

  /**
   * Returns the selected date as a LocalDate.
   * @return the selected date
   */
  LocalDate getSelectedDate();
}
