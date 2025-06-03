// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Helper class encapsulating recurrence logic for an event series.
 * Holds parameters like weekdays, repeat count, and end date, and applies them
 * to generate a valid series of CalendarEvent instances.
 */
public class RecurrenceRule {
  // ─── Fields (all private and final) ─────────────────────────────────────────────

  private final Set<DayOfWeek> repeatDays;       // EnumSet<DayOfWeek> better than a custom enum
  private final int count;                        // number of times to repeat (if used)
  private final LocalDate start;
  private final LocalDate end;
  private final List<CalendarEvent> seriesOfEvents;


  private final String firstName;
  private final String lastName;
  private final int age;
  private final String address;
  private final String phoneNumber;

  // ─── Private constructor only called by the Builder ─────────────────────────────
  private RecurrenceRule(Builder builder) {

    this.repeatDays = builder.repeatDays;       // EnumSet<DayOfWeek> better than a custom enum
    this.count = builder.count;                  // number of times to repeat (if used)
    this.start = builder.start;
    this.end = builder.end;
    this.seriesOfEvents = builder.seriesOfEvents;

    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.age = builder.age;
    this.address = builder.address;
    this.phoneNumber = builder.phoneNumber;
  }

  // ─── Public getters (no setters) ─────────────────────────────────────────────────
  public Set<DayOfWeek> getRepeatDays() {
    return repeatDays;
  }

  public int getCount() {
    return count;
  }

  public LocalDate getStart() {
    return start;
  }

  public LocalDate getEnd() {
    return end;
  }

  public List<CalendarEvent> getSeriesOfEvents() {
    return seriesOfEvents;
  }

  /**
   * Generates the list of start/end times for each event in the series.
   *
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

  // ─── The Builder ────────────────────────────────────────────────────────────────
  public static class Builder {
    // 1) match the fields in the outer class (but NOT final here)
    private Set<DayOfWeek> repeatDays;       // EnumSet<DayOfWeek> better than a custom enum
    private int count;                        // number of times to repeat (if used)
    private LocalDate start;
    private LocalDate end;
    private List<CalendarEvent> seriesOfEvents;


    private String firstName;
    private String lastName;
    private int age;
    private String address;
    private String phoneNumber;

    // 2) Provide "setter" methods for each field, returning the Builder itself

    /**
     * Required: first name
     */
    public Builder repeatDays(Set<DayOfWeek> repeatDays) {
      this.repeatDays = repeatDays;
      return this;
    }

    /**
     * Required: last name
     */
    public Builder count(int count) {
      this.count = count;
      return this;
    }

    /**
     * Optional: age (default is 0 if not set)
     */
    public Builder start(LocalDate start) {
      this.start = start;
      return this;
    }

    /**
     * Optional: address (default is null if not set)
     */
    public Builder end(LocalDate end) {
      this.end = end;
      return this;
    }

    /**
     * Optional: phone number (default is null if not set)
     */
    public Builder seriesOfEvents(List<CalendarEvent> seriesOfEvents) {
      this.seriesOfEvents = seriesOfEvents;
      return this;
    }

    // 3) The build() method checks any invariants you want, then calls Person's private constructor
    public RecurrenceRule build() {
      // Example invariant check: first and last name must not be null
      return new RecurrenceRule(this);
    }
  }
}