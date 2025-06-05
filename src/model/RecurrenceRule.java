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
 * to generate a valid series of CalendarEvent instances.
 */
public class RecurrenceRule {

  private final Set<DayOfWeek> repeatDays; // the days of the week this repeats on
  private final int count; // how many times it repeats
  private final LocalDate start; // start of recurrence window
  private final LocalDate end; // optional end date
  private final List<CalendarEvent> seriesOfEvents; // optional: pre-built events list

  private RecurrenceRule(Builder builder) {
    this.repeatDays = builder.repeatDays;
    this.count = builder.count;
    this.start = builder.start;
    this.end = builder.end;
    this.seriesOfEvents = builder.seriesOfEvents;
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
  public int getCount() {
    return count;
  }

  /**
   * Returns a copy of the starting date of the recurrence window.
   */
  public LocalDate getStart() {
    return LocalDate.of(start.getYear(), start.getMonth(), start.getDayOfMonth());
  }

  /**
   * Returns a copy of the ending date of the recurrence window.
   */
  public LocalDate getEnd() {
    // if there is no end date
    if (end == null) {
      // return null
      return null;
    } else {
      // otherwise return the end date
      return LocalDate.of(end.getYear(), end.getMonth(), end.getDayOfMonth());
    }
  }

  /**
   * Returns a list of CalendarEvent objects if already generated.
   */
  public List<CalendarEvent> getSeriesOfEvents() {
    return seriesOfEvents;
  }

  /**
   * Generates a list of start-end LocalDateTime pairs for this recurrence rule.
   * Assumes all occurrences have the same time component.
   *
   * @return list of start/end date pairs
   */
  public List<LocalDateTime[]> generateOccurrences(LocalTime startTime, LocalTime endTime) {
    // make a new list to hold the generated start-end pairs
    List<LocalDateTime[]> occurrences = new ArrayList<>();

    // start from the configured start date
    LocalDate current = start;

    // keep track of how many events we've created
    int occurrencesCount = 0;

    // keep looping until we've hit the repeat count or gone past the end date
    while ((count == 0 || occurrencesCount < count) && (end == null || !current.isAfter(end))) {
      // only create events on the specified days of the week
      if (repeatDays.contains(current.getDayOfWeek())) {
        // make a start datetime from the current date and given start time
        LocalDateTime startDateTime = LocalDateTime.of(current, startTime);

        // make the corresponding end datetime
        LocalDateTime endDateTime = LocalDateTime.of(current, endTime);

        // add this pair to the results
        occurrences.add(new LocalDateTime[]{startDateTime, endDateTime});

        // increment how many we've added so far
        occurrencesCount++;
      }

      // move to the next calendar day
      current = current.plusDays(1);
    }

    // return the full list of recurrence times
    return occurrences;
  }

  /**
   * Returns whether an event should occur on the given date.
   */
  public boolean occursOn(LocalDate date) {
    if (date.isBefore(start)) {
      return false;
    }
    if (end != null && date.isAfter(end)) {
      return false;
    }
    return repeatDays.contains(date.getDayOfWeek());
  }

  /**
   * Computes the number of valid recurrence occurrences.
   */
  public int getOccurrenceCount() {
    int count = 0;
    LocalDate current = start;
    while ((this.count == 0 || count < this.count) && (end == null || !current.isAfter(end))) {
      if (repeatDays.contains(current.getDayOfWeek())) {
        count++;
      }
      current = current.plusDays(1);
    }
    return count;
  }

  /**
   * Builder for creating RecurrenceRule instances.
   */
  public static class Builder {
    private Set<DayOfWeek> repeatDays = new HashSet<>();
    private int count = 0;
    private LocalDate start;
    private LocalDate end;
    private List<CalendarEvent> seriesOfEvents;

    /**
     * Sets the repeating weekdays.
     */
    public Builder repeatDays(Set<DayOfWeek> repeatDays) {
      this.repeatDays = repeatDays;
      return this;
    }

    /**
     * Sets the repeat count.
     */
    public Builder count(int count) {
      this.count = count;
      return this;
    }

    /**
     * Sets the start date of the recurrence.
     */
    public Builder start(LocalDate start) {
      this.start = start;
      return this;
    }

    /**
     * Sets the end date of the recurrence.
     */
    public Builder end(LocalDate end) {
      this.end = end;
      return this;
    }

    /**
     * Sets an optional pre-generated series of events.
     */
    public Builder seriesOfEvents(List<CalendarEvent> seriesOfEvents) {
      this.seriesOfEvents = seriesOfEvents;
      return this;
    }

    /**
     * Builds the RecurrenceRule object after validating required fields.
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