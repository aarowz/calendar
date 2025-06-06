// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.DayOfWeek;
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
    // for each event in the occurrences
    for (IEvent e : this.occurrences) {
      // if the start time is valid
      if (!e.getStart().isBefore(fromDate)) {
        // apply any series updates
        updated.add(new CalendarEvent.Builder()
                .subject(newSubject != null ? newSubject : e.getSubject())
                .start(newStart != null ? newStart : e.getStart())
                .end(newEnd != null ? newEnd : e.getEnd())
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
   * @return list of CalendarEvent objects
   */
  private List<IEvent> generateOccurrences() {
    List<IEvent> result = new ArrayList<>();

    // start with the base event's date and extract times
    LocalDate currentDate = baseEvent.getStart().toLocalDate();
    LocalTime startTime = baseEvent.getStart().toLocalTime();
    LocalTime endTime = baseEvent.getEnd().toLocalTime();

    // determine which days of the week the event repeats on
    Set<DayOfWeek> repeatDays = new HashSet<>(rule.getRepeatDays());

    int count = 0; // how many occurrences we've generated so far

    // keep generating events until we hit the repeat count or repeat-until condition
    while (true) {
      // only generate an event if today is one of the repeat days
      if (repeatDays.contains(currentDate.getDayOfWeek())) {
        LocalDateTime start = LocalDateTime.of(currentDate, startTime);
        LocalDateTime end = LocalDateTime.of(currentDate, endTime);

        // build the event using the base data and assign the series ID
        CalendarEvent instance = new CalendarEvent.Builder()
                .subject(baseEvent.getSubject())
                .start(start)
                .end(end)
                .description(baseEvent.getDescription())
                .location(baseEvent.getLocation())
                .status(baseEvent.getStatus())
                .seriesId(seriesId)
                .build();

        result.add(instance);
        count++;

        // if a repeat count limit is set and reached, stop
        if (rule.getRepeatCount() > 0 && count >= rule.getRepeatCount()) {
          break;
        }
      }

      // move to the next day
      currentDate = currentDate.plusDays(1);

      // if we passed the repeat-until date, stop
      if (rule.getRepeatUntil() != null && currentDate.isAfter(rule.getRepeatUntil())) {
        break;
      }
    }
    return result;
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