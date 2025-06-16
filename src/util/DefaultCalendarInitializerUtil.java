// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package util;

import java.time.ZoneId;

import model.IDelegator;

/**
 * Utility class for creating a default calendar instance using the system's current timezone.
 * This allows GUI or controller components to work with a pre-initialized calendar without
 * requiring user input.
 */
public class DefaultCalendarInitializerUtil {

  /**
   * Creates a default calendar using the system timezone and adds it to the model.
   * If a calendar with the default name already exists, this method does nothing.
   *
   * @param model the calendar model delegator
   * @return the name of the default calendar
   */
  public static String createDefaultCalendar(IDelegator model) {
    String defaultName = "default";
    ZoneId systemZone = java.time.ZoneId.systemDefault();

    try {
      model.createCalendar(defaultName, systemZone);
    } catch (Exception ignored) {
      // assume the calendar already exists; suppress any duplication error
    }

    model.useCalendar(defaultName);
    return defaultName;
  }
}