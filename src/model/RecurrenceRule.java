// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * Helper class encapsulating recurrence logic for an event series.
 * Holds parameters like weekdays, repeat count, and end date, and applies them
 * to generate a valid series of CalendarEvent instances.
 */
public class RecurrenceRule {
  Set<DayOfWeek> repeatDays;       // EnumSet<DayOfWeek> better than a custom enum
  int count;                        // number of times to repeat (if used)
  LocalDate start;
  LocalDate end;
  List<CalendarEvent> seriesOfEvents;

  /**
   * Generates the list of start/end times for each event in the series.
   * @return List of (start, end) pairs as LocalDateTime[]
   */
  public List<LocalDateTime[]> generateOccurrences() {
    // Method body to be implemented
    return null;
  }

  /**
   * Determines whether the recurrence rule allows for an event to occur on the given date.
   *
   * @param date the date to check
   * @return true if an event occurs on the given date, false otherwise
   */
  public boolean occursOn(LocalDate date) {
    // Method body to be implemented
    return false;
  }

  /**
   * Computes the total number of valid occurrences defined by this rule.
   * This is based on the specified {@code count} or the number of dates up to {@code until}.
   *
   * @return the number of event occurrences
   */
  public int getOccurrenceCount() {
    // Method body to be implemented
    return 0;
  }
}
