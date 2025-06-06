// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class encapsulating recurrence logic for an event series.
 * Holds parameters like weekdays, repeat count, and end date, and applies them
 * to generate valid recurrence patterns.
 */
public class RecurrenceRule {

  private final Set<DayOfWeek> repeatDays; // days of the week this repeats on (e.g., MWF)
  private final int count;                 // how many times the event repeats (0 = unlimited)
  private final LocalDate start;           // date to begin recurrence from
  private final LocalDate repeatUntil;     // optional end date for recurrence

  /**
   * Private constructor to initiate builder pattern.
   *
   * @param builder the given builder
   */
  private RecurrenceRule(Builder builder) {
    this.repeatDays = builder.repeatDays;
    this.count = builder.count;
    this.start = builder.start;
    this.repeatUntil = builder.repeatUntil;
  }

  /**
   * Returns the days of week this rule repeats on.
   */
  public Set<DayOfWeek> getRepeatDays() {
    return repeatDays;
  }

  /**
   * Returns how many times the event should repeat.
   */
  public int getRepeatCount() {
    return count;
  }

  /**
   * Returns the start date of the recurrence window.
   */
  public LocalDate getStart() {
    return LocalDate.of(start.getYear(), start.getMonth(), start.getDayOfMonth());
  }

  /**
   * Returns the ending date of the recurrence window.
   * If there is no end, returns null.
   */
  public LocalDate getRepeatUntil() {
    if (repeatUntil == null) {
      return null;
    }
    return LocalDate.of(repeatUntil.getYear(), repeatUntil.getMonth(),
            repeatUntil.getDayOfMonth());
  }

  /**
   * Generates a list of start-end date-time pairs for each valid event occurrence.
   * Assumes all occurrences use the same time-of-day.
   *
   * @param startTime the time the event starts each day
   * @param endTime   the time the event ends each day
   * @return list of LocalDateTime arrays: [start, end]
   */
  public List<LocalDateTime[]> generateOccurrences(LocalTime startTime, LocalTime endTime) {
    List<LocalDateTime[]> occurrences = new ArrayList<>();
    LocalDate current = start;
    int occurrencesCount = 0;

    // generate occurrences until either reaching the specified count
    // or going past the repeat-until date (if any)
    while ((count == 0 || occurrencesCount < count) &&
            (repeatUntil == null || !current.isAfter(repeatUntil))) {

      // only create an occurrence if the current day is one of the repeat days
      if (repeatDays.contains(current.getDayOfWeek())) {
        // create start and end timestamps for the current date
        LocalDateTime startDateTime = LocalDateTime.of(current, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(current, endTime);

        // add the [start, end] pair to the list
        occurrences.add(new LocalDateTime[]{startDateTime, endDateTime});
        occurrencesCount++;
      }

      // move to the next day
      current = current.plusDays(1);
    }

    return occurrences;
  }

  /**
   * Determines whether an event should occur on a given date.
   *
   * @param date the date to check
   * @return true if the rule allows recurrence on that date
   */
  public boolean occursOn(LocalDate date) {
    if (date.isBefore(start)) {
      return false;
    }
    if (repeatUntil != null && date.isAfter(repeatUntil)) {
      return false;
    }
    return repeatDays.contains(date.getDayOfWeek());
  }

  /**
   * Returns the total number of event occurrences based on the configured repeat pattern.
   *
   * @return the number of valid event occurrences
   */
  public int getOccurrenceCount() {
    int occurrenceCounter = 0;
    LocalDate current = start;

    // loop until the desired number of occurrences is reached,
    // or until the repeatUntil date is passed (whichever comes first)
    while ((count == 0 || occurrenceCounter < count) &&
            (repeatUntil == null || !current.isAfter(repeatUntil))) {

      // increment counter only if current day is in the set of repeat days
      if (repeatDays.contains(current.getDayOfWeek())) {
        occurrenceCounter++;
      }

      // move to the next day
      current = current.plusDays(1);
    }

    return occurrenceCounter;
  }

  /**
   * Builder for creating RecurrenceRule instances.
   */
  public static class Builder {
    private Set<DayOfWeek> repeatDays = new HashSet<>();
    private int count = 0;
    private LocalDate start;
    private LocalDate repeatUntil;

    /**
     * Sets the days of the week for the recurrence.
     */
    public Builder repeatDays(Set<DayOfWeek> repeatDays) {
      this.repeatDays = repeatDays;
      return this;
    }

    /**
     * Sets how many times the event should repeat.
     */
    public Builder count(int count) {
      this.count = count;
      return this;
    }

    /**
     * Sets the starting date for the recurrence window.
     */
    public Builder start(LocalDate start) {
      this.start = start;
      return this;
    }

    /**
     * Sets the optional ending date for the recurrence window.
     */
    public Builder repeatUntil(LocalDate repeatUntil) {
      this.repeatUntil = repeatUntil;
      return this;
    }

    /**
     * Builds the RecurrenceRule object after validation.
     *
     * @return constructed RecurrenceRule
     * @throws IllegalArgumentException if required fields are missing
     */
    public RecurrenceRule build() {
      if (repeatDays == null || repeatDays.isEmpty()) {
        throw new IllegalArgumentException("Repeat days must be provided");
      }
      if (start == null) {
        throw new IllegalArgumentException("Start date must be provided");
      }
      return new RecurrenceRule(this);
    }
  }
}