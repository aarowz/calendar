// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Immutable implementation of a recurring event series.
 * Uses a recurrence rule to generate individual CalendarEvent instances.
 * Supports rules such as repeating on weekdays and ending after a count or by a date.
 */
public class CalendarEventSeries implements IEventSeries {
  private final IEvent baseEvent;
  private final RecurrenceRule rule;
  private final List<IEvent> occurrences;
  private final UUID seriesId;

  /**
   * Constructs a CalendarEventSeries with a base event and recurrence rule.
   * Generates all occurrences based on the rule and stores them immutably.
   *
   * @param baseEvent the template event to repeat
   * @param rule      the recurrence rule for generating the series
   */
  public CalendarEventSeries(CalendarEvent baseEvent, RecurrenceRule rule) {
    this.seriesId = UUID.randomUUID(); // assign a unique ID to group all related events
    this.baseEvent = baseEvent;
    this.rule = rule;
    this.occurrences = generateOccurrences(); // generate and cache all event instances
  }


  /**
   * Constructs a CalendarEventSeries from a list of existing events.
   * Assumes all events share the same series ID.
   *
   * @param occurrences the list of events
   */
  CalendarEventSeries(List<IEvent> occurrences) {
    // sort occurrences by start time using an anonymous Comparator
    occurrences.sort(Comparator.comparing(ROIEvent::getStart));
    this.occurrences = occurrences;
    this.baseEvent = occurrences.get(0);
    this.seriesId = baseEvent.getSeriesId();
    HashSet<DayOfWeek> temp = new HashSet<DayOfWeek>();
    temp.add(DayOfWeek.MONDAY);
    this.rule = new RecurrenceRule.Builder()
            .repeatDays(temp)
            .count(0)
            .start(baseEvent.getStart().toLocalDate())
            .build();
  }

  /**
   * Edits events in this series starting from the given date onward.
   *
   * @param fromDate       the start date to apply edits from
   * @param newSubject     updated subject or null
   * @param newStart       updated start or null
   * @param newEnd         updated end or null
   * @param newDescription updated description or null
   * @param newStatus      updated status or null
   * @param newLocation    updated location or null
   * @return list of edited events
   */
  public List<IEvent> editEvents(LocalDateTime fromDate,
                                 String newSubject,
                                 LocalDateTime newStart,
                                 LocalDateTime newEnd,
                                 String newDescription,
                                 EventStatus newStatus,
                                 String newLocation) {
    List<IEvent> updated = new ArrayList<>();

    // figure out time shift if newStart is given
    Duration shift = (newStart != null) ? Duration.between(fromDate, newStart) : null;
    Duration duration = (newStart != null && newEnd != null) ? Duration.between(newStart, newEnd) :
            null;

    for (IEvent e : this.occurrences) {
      if (!e.getStart().isBefore(fromDate)) {
        // shifted start and end
        LocalDateTime updatedStart = (shift != null) ? e.getStart().plus(shift) : e.getStart();
        LocalDateTime updatedEnd = (duration != null) ? updatedStart.plus(duration)
                : (newEnd != null ? newEnd : e.getEnd());

        updated.add(new CalendarEvent.Builder()
                .subject(newSubject != null ? newSubject : e.getSubject())
                .start(updatedStart)
                .end(updatedEnd)
                .description(newDescription != null ? newDescription : e.getDescription())
                .status(newStatus != null ? newStatus : e.getStatus())
                .location(newLocation != null ? newLocation : e.getLocation())
                .seriesId(e.getSeriesId())
                .build());
      }
    }
    return updated;
  }

  /**
   * Returns all occurrences of events in this series.
   *
   * @return list of CalendarEvent instances following the recurrence rule
   */
  @Override
  public List<IEvent> getAllOccurrences() {
    return List.copyOf(occurrences); // return an immutable copy
  }

  /**
   * Generates all recurring event instances according to the recurrence rule.
   *
   * @return a list of CalendarEvent objects based on the recurrence pattern
   */
  private List<IEvent> generateOccurrences() {
    List<IEvent> result = new ArrayList<>();

    // extract recurrence settings
    LocalDate currentDate = baseEvent.getStart().toLocalDate();
    LocalTime startTime = baseEvent.getStart().toLocalTime();
    LocalTime endTime = baseEvent.getEnd().toLocalTime();
    Set<DayOfWeek> repeatDays = new HashSet<>(rule.getRepeatDays());

    int count = 0;

    // generate each instance until the rule's termination condition is met
    while (true) {
      if (shouldGenerateOn(currentDate, repeatDays)) {
        result.add(buildOccurrence(currentDate, startTime, endTime));
        count++;

        if (hasReachedRepeatCount(count)) {
          break;
        }
      }

      currentDate = currentDate.plusDays(1);

      if (hasPassedRepeatUntil(currentDate)) {
        break;
      }
    }

    return result;
  }

  /**
   * Checks if an event should be generated on the given date.
   *
   * @param date       the date to check
   * @param repeatDays the set of valid repeat days
   * @return true if the event should occur on this day, false otherwise
   */
  private boolean shouldGenerateOn(LocalDate date, Set<DayOfWeek> repeatDays) {
    return repeatDays.contains(date.getDayOfWeek());
  }

  /**
   * Builds an individual event occurrence for the given date and times.
   *
   * @param date      the date of the event
   * @param startTime the start time on that date
   * @param endTime   the end time on that date
   * @return a fully built CalendarEvent instance
   */
  private CalendarEvent buildOccurrence(LocalDate date, LocalTime startTime, LocalTime endTime) {
    LocalDateTime start = LocalDateTime.of(date, startTime);
    LocalDateTime end = LocalDateTime.of(date, endTime);

    return new CalendarEvent.Builder()
            .subject(baseEvent.getSubject())
            .start(start)
            .end(end)
            .description(baseEvent.getDescription())
            .location(baseEvent.getLocation())
            .status(baseEvent.getStatus())
            .seriesId(seriesId)
            .build();
  }

  /**
   * Determines whether the repeat count has been satisfied.
   *
   * @param count how many events have been generated so far
   * @return true if the limit has been reached, false otherwise
   */
  private boolean hasReachedRepeatCount(int count) {
    return rule.getRepeatCount() > 0 && count >= rule.getRepeatCount();
  }

  /**
   * Checks whether the current date exceeds the recurrence end date.
   *
   * @param currentDate the date currently being processed
   * @return true if the recurrence should stop due to date
   */
  private boolean hasPassedRepeatUntil(LocalDate currentDate) {
    return rule.getRepeatUntil() != null && currentDate.isAfter(rule.getRepeatUntil());
  }

  @Override
  public IEvent getBaseEvent() {
    return new CalendarEvent.Builder()
            .subject(baseEvent.getSubject())
            .start(baseEvent.getStart())
            .end(baseEvent.getEnd())
            .description(baseEvent.getDescription())
            .status(baseEvent.getStatus())
            .location(baseEvent.getLocation())
            .seriesId(baseEvent.getSeriesId())
            .build();
  }
}