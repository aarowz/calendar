// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import model.*;
import view.IView;
import exceptions.CommandExecutionException;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Unit tests for querying calendar events by date and time.
 * Verifies expected behavior across various edge cases and time boundaries.
 */
public class QueryEventsCommandTest {

  private IDelegator model;
  private MockView view;

  /**
   * A mock view implementation that accumulates messages sent to it.
   */
  private static class MockView implements IView {
    StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) throws IOException {
      log.append(message).append("\n");
    }

    /**
     * Returns the full log of rendered messages.
     *
     * @return the log as a String
     */
    public String getLog() {
      return log.toString();
    }
  }

  /**
   * Initializes the test calendar and populates it with two base events
   * for general-purpose testing. Sets active calendar to "testcal".
   */
  @Before
  public void setup() throws CommandExecutionException {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    CreateEventCommand e1 = new CreateEventCommand(
            "Meeting",
            LocalDateTime.of(2025, 6, 3, 10, 0),
            LocalDateTime.of(2025, 6, 3, 11, 0),
            "Team sync", "Room 1", EventStatus.PUBLIC, null, null, null
    );

    CreateEventCommand e2 = new CreateEventCommand(
            "Workshop",
            LocalDateTime.of(2025, 6, 4, 14, 0),
            LocalDateTime.of(2025, 6, 4, 16, 0),
            "Skills", "Room 2", EventStatus.PRIVATE, null, null, null
    );

    e1.execute(model, view);
    e2.execute(model, view);
  }

  /**
   * Tests that events created on a specific day are correctly returned by getEventsOn.
   */
  @Test
  public void testQueryEventsOnSpecificDay() throws Exception {
    // Create two events on June 15, 2025
    ICommand event1 = new CreateEventCommand(
            "Breakfast",
            LocalDateTime.of(2025, 6, 15, 8, 0),
            LocalDateTime.of(2025, 6, 15, 9, 0),
            null, null, null,
            null, null, null
    );

    ICommand event2 = new CreateEventCommand(
            "Meeting",
            LocalDateTime.of(2025, 6, 15, 10, 0),
            LocalDateTime.of(2025, 6, 15, 11, 0),
            null, null, null,
            null, null, null
    );

    // Execute both event creation commands
    event1.execute(model, view);
    event2.execute(model, view);

    // Query for events on that specific date
    List<ROIEvent> events = model.getEventsOn(LocalDate.of(2025, 6, 15));
    assertEquals(2, events.size());

    // Extract subjects from events and verify
    List<String> subjects = events.stream()
            .map(e -> ((IEvent) e).getSubject())
            .collect(Collectors.toList());

    assertTrue(subjects.contains("Breakfast"));
    assertTrue(subjects.contains("Meeting"));
  }

  /**
   * Tests that events created at midnight and end of day are both included
   * in getEventsOn for that specific date.
   */
  @Test
  public void testEventsAtMidnightAndEndOfDay() throws Exception {
    // Create two events: one at the start of the day, one at the end
    ICommand early = new CreateEventCommand(
            "Early",
            LocalDateTime.of(2025, 6, 12, 0, 0),
            LocalDateTime.of(2025, 6, 12, 1, 0),
            null, null, null,
            null, null, null
    );

    ICommand late = new CreateEventCommand(
            "Late",
            LocalDateTime.of(2025, 6, 12, 23, 0),
            LocalDateTime.of(2025, 6, 13, 0, 0),
            null, null, null,
            null, null, null
    );

    // Execute both commands
    early.execute(model, view);
    late.execute(model, view);

    // Fetch events that start on 2025-06-12
    List<IEvent> events = CalendarModel.getAllEvents().stream()
            .filter(e -> e.getStart().toLocalDate().equals(LocalDate.of(2025, 6,
                    12)))
            .collect(Collectors.toList());

    assertEquals(2, events.size());

    // Optionally check subjects
    List<String> subjects = events.stream()
            .map(IEvent::getSubject)
            .collect(Collectors.toList());

    assertTrue(subjects.contains("Early"));
    assertTrue(subjects.contains("Late"));
  }

  /**
   * Tests that consecutive events with no time gap between them
   * are both returned when querying for the same day.
   */
  @Test
  public void testBackToBackEventsSameDay() throws Exception {
    // Create two back-to-back events on June 16, 2025
    ICommand first = new CreateEventCommand(
            "First",
            LocalDateTime.of(2025, 6, 16, 9, 0),
            LocalDateTime.of(2025, 6, 16, 10, 0),
            null, null, null,
            null, null, null
    );

    ICommand second = new CreateEventCommand(
            "Second",
            LocalDateTime.of(2025, 6, 16, 10, 0),
            LocalDateTime.of(2025, 6, 16, 11, 0),
            null, null, null,
            null, null, null
    );

    // Execute both commands
    first.execute(model, view);
    second.execute(model, view);

    // Fetch events that start on June 16
    List<IEvent> events = CalendarModel.getAllEvents().stream()
            .filter(e -> e.getStart().toLocalDate().equals(LocalDate.of(2025, 6,
                    16)))
            .collect(Collectors.toList());

    assertEquals(2, events.size());

    // Optional: verify subjects are both present
    List<String> subjects = events.stream()
            .map(IEvent::getSubject)
            .collect(Collectors.toList());

    assertTrue(subjects.contains("First"));
    assertTrue(subjects.contains("Second"));
  }

  /**
   * Tests that overlapping events on the same day are both returned.
   */
  @Test
  public void testOverlappingEventsQuery() throws Exception {
    // Create two overlapping events on June 18, 2025
    ICommand eventA = new CreateEventCommand(
            "A",
            LocalDateTime.of(2025, 6, 18, 13, 0),
            LocalDateTime.of(2025, 6, 18, 14, 0),
            null, null, null,
            null, null, null
    );

    ICommand eventB = new CreateEventCommand(
            "B",
            LocalDateTime.of(2025, 6, 18, 13, 30),
            LocalDateTime.of(2025, 6, 18, 14, 30),
            null, null, null,
            null, null, null
    );

    // Execute both commands
    eventA.execute(model, view);
    eventB.execute(model, view);

    // Count how many events start on June 18
    List<IEvent> events = CalendarModel.getAllEvents().stream()
            .filter(e -> e.getStart().toLocalDate().equals(LocalDate.of(2025, 6,
                    18)))
            .collect(Collectors.toList());

    assertEquals(2, events.size());

    // Optional: verify subjects
    List<String> subjects = events.stream()
            .map(IEvent::getSubject)
            .collect(Collectors.toList());

    assertTrue(subjects.contains("A"));
    assertTrue(subjects.contains("B"));
  }

  /**
   * Tests that an empty query produces a "no events" message.
   */
  @Test
  public void testQueryReturnsEmpty() throws CommandExecutionException {
    QueryEventsCommand query = new QueryEventsCommand(LocalDate.of(2025, 6,
            10));
    query.execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("no events"));
  }

  /**
   * Tests user busy status at specific timepoints.
   */
  @Test
  public void testIsUserBusyAtSpecificDateTime() throws Exception {
    // Create an event from 10:00 to 11:00 on June 20, 2025
    ICommand interview = new CreateEventCommand(
            "Interview",
            LocalDateTime.of(2025, 6, 20, 10, 0),
            LocalDateTime.of(2025, 6, 20, 11, 0),
            null, null, null,
            null, null, null
    );

    interview.execute(model, view);

    // Verify user is busy at 10:30
    LocalDateTime busyTime = LocalDateTime.of(2025, 6, 20, 10,
            30);
    assertTrue("User should be busy at 10:30", model.isBusyAt(busyTime));

    // Verify user is not busy at 11:30
    LocalDateTime freeTime = LocalDateTime.of(2025, 6, 20, 11,
            30);
    assertFalse("User should be available at 11:30", model.isBusyAt(freeTime));
  }

  /**
   * Tests that only events within a specific date-time range are returned.
   */
  @Test
  public void testQueryEventsInDateTimeRange() throws Exception {
    // Create 3 events across multiple days
    ICommand call = new CreateEventCommand(
            "Call",
            LocalDateTime.of(2025, 6, 10, 9, 0),
            LocalDateTime.of(2025, 6, 10, 10, 0),
            null, null, null,
            null, null, null
    );

    ICommand workshop = new CreateEventCommand(
            "Workshop",
            LocalDateTime.of(2025, 6, 11, 14, 0),
            LocalDateTime.of(2025, 6, 11, 16, 0),
            null, null, null,
            null, null, null
    );

    ICommand review = new CreateEventCommand(
            "Review",
            LocalDateTime.of(2025, 6, 12, 8, 0),
            LocalDateTime.of(2025, 6, 12, 9, 0),
            null, null, null,
            null, null, null
    );

    // Execute all three commands
    call.execute(model, view);
    workshop.execute(model, view);
    review.execute(model, view);

    // Define date-time range
    LocalDateTime start = LocalDateTime.of(2025, 6, 10, 0, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 11, 23, 59);

    // Collect events in the specified range
    List<IEvent> inRange = new ArrayList<>();
    for (int i = 0; i <= 2; i++) {
      LocalDate date = start.toLocalDate().plusDays(i);
      for (ROIEvent e : model.getEventsOn(date)) {
        if (!e.getEnd().isBefore(start) && !e.getStart().isAfter(end)) {
          inRange.add((IEvent) e);
        }
      }
    }

    // Validate that only the two expected events are included
    assertEquals(2, inRange.size());
    List<String> subjects = inRange.stream()
            .map(IEvent::getSubject)
            .collect(Collectors.toList());

    assertTrue(subjects.contains("Call"));
    assertTrue(subjects.contains("Workshop"));
    assertFalse(subjects.contains("Review"));
  }
}