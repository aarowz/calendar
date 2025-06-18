// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package util;

import java.time.Year;

import javax.swing.JComboBox;

/**
 * Utility class for creating standardized date and time input components
 * such as dropdowns and spinners. Used in event creation and editing forms
 * to ensure consistency and reduce UI duplication across panels.
 */
public class DateDropDownUtil {
  /**
   * Creates a combo box for selecting a year.
   * Shows a range of years centered around the current year.
   *
   * @return a JComboBox containing year options
   */
  public static JComboBox<Integer> createYearDropdown() {
    JComboBox<Integer> box = new JComboBox<>();
    int currentYear = Year.now().getValue();

    for (int y = currentYear - 50; y <= currentYear + 50; y++) {
      box.addItem(y);
    }

    return box;
  }

  /**
   * Creates a combo box for selecting a month (1–12).
   *
   * @return a JComboBox containing month numbers
   */
  public static JComboBox<Integer> createMonthDropdown() {
    JComboBox<Integer> box = new JComboBox<>();

    for (int m = 1; m <= 12; m++) {
      box.addItem(m);
    }

    return box;
  }

  /**
   * Creates a combo box for selecting a day (1–31).
   * Note: it is the caller’s responsibility to filter days based on month/year context.
   *
   * @return a JComboBox containing day numbers
   */
  public static JComboBox<Integer> createDayDropdown() {
    JComboBox<Integer> box = new JComboBox<>();

    for (int d = 1; d <= 31; d++) {
      box.addItem(d);
    }

    return box;
  }

  /**
   * Creates a combo box for selecting an hour (0–23).
   *
   * @return a JComboBox containing hour values
   */
  public static JComboBox<Integer> createHourDropdown() {
    JComboBox<Integer> box = new JComboBox<>();

    for (int h = 0; h <= 23; h++) {
      box.addItem(h);
    }

    return box;
  }

  /**
   * Creates a combo box for selecting a minute (0–59).
   *
   * @return a JComboBox containing minute values
   */
  public static JComboBox<Integer> createMinuteDropdown() {
    JComboBox<Integer> box = new JComboBox<>();

    for (int m = 0; m <= 59; m++) {
      box.addItem(m);
    }

    return box;
  }
}