// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.CalendarModel;
import model.EventStatus;
import model.ICalendar;
import model.IEvent;
import model.ROIEvent;
import view.IView;

import org.junit.Before;
import org.junit.Test;

import exceptions.CommandExecutionException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for QueryEventsCommand.
 * Ensures correct behavior when querying events based on various criteria.
 */
public class QueryEventsCommandTest {

  private CalendarModel model;
  private MockView view;

  /**
   * A mock view that records messages passed to renderMessage.
   */
  private static class MockView implements IView {
    StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) throws IOException {
      log.append(message).append("\n");
    }

    public String getLog() {
      return log.toString();
    }
  }

  /**
   * Sets up a calendar model with scheduled events for query testing.
   */
  @Before
  public void setup() throws CommandExecutionException {
    model = new CalendarModel();
    view = new MockView();

    CreateEventCommand e1 = new CreateEventCommand(
            "Meeting",
            LocalDateTime.of(2025, 6, 3, 10, 0),
            LocalDateTime.of(2025, 6, 3, 11, 0),
            "Team sync", "Room 1", EventStatus.PUBLIC, null,
            null, null
    );

    CreateEventCommand e2 = new CreateEventCommand(
            "Workshop",
            LocalDateTime.of(2025, 6, 4, 14, 0),
            LocalDateTime.of(2025, 6, 4, 16, 0),
            "Skills", "Room 2", EventStatus.PRIVATE, null,
            null, null
    );

    e1.execute(model, view);
    e2.execute(model, view);
  }

  /**
   * Verifies that the calendar correctly returns all events on a specific day.
   */
  @Test
  public void testQueryEventsOnSpecificDay() throws Exception {
    ICalendar model = new CalendarModel();
    IView view = new MockView();

    // create two events on the same day
    ICommand event1 = CommandParser.parse("create event Breakfast from " +
            "2025-06-15T08:00 to 2025-06-15T09:00");
    ICommand event2 = CommandParser.parse("create event Meeting from " +
            "2025-06-15T10:00 to 2025-06-15T11:00");

    assert event1 != null;
    event1.execute(model, view);
    assert event2 != null;
    event2.execute(model, view);

    // query for events on June 15, 2025
    List<ROIEvent> events = model.getEventsOn(LocalDate.of(2025, 6, 15));

    assertEquals(2, events.size());

    // confirm expected event subjects are present
    boolean hasBreakfast = false;
    boolean hasMeeting = false;
    for (ROIEvent event : events) {
      if (event.getSubject().equals("Breakfast")) {
        hasBreakfast = true;
      } else if (event.getSubject().equals("Meeting")) {
        hasMeeting = true;
      }
    }

    assertTrue(hasBreakfast);
    assertTrue(hasMeeting);
  }

  /**
   * Verifies that events created at the very start and very end of a day
   * are both recognized when querying for that specific date.
   */
  @Test
  public void testEventsAtMidnightAndEndOfDay() throws Exception {
    ICalendar model = new CalendarModel();
    IView view = new MockView();

    Objects.requireNonNull(CommandParser
                    .parse("create event Early from 2025-06-12T00:00 to 2025-06-12T01:00"))
            .execute(model, view);
    Objects.requireNonNull(CommandParser
                    .parse("create event Late from 2025-06-12T23:00 to 2025-06-13T00:00"))
            .execute(model, view);

    List<IEvent> events = new ArrayList<>();
    for (IEvent e : CalendarModel.getAllEvents()) {
      if (e.getStart().toLocalDate().equals(LocalDate.of(2025, 6, 12))) {
        events.add(e);
      }
    }

    assertEquals(2, events.size());
  }

  /**
   * Verifies that an event which crosses into the next day is only counted
   * on the day it starts when querying by date.
   */
  @Test
  public void testEventCrossingMidnightNotCountedFullyOnStartDay() throws Exception {
    ICalendar model = new CalendarModel();
    IView view = new MockView();

    Objects.requireNonNull(CommandParser
                    .parse("create event Overnight from " +
                            "2025-06-14T22:00 to 2025-06-15T01:00"))
            .execute(model, view);

    int countStartDay = 0;
    int countNextDay = 0;

    for (IEvent e : CalendarModel.getAllEvents()) {
      if (e.getStart().toLocalDate().equals(LocalDate.of(2025, 6, 14))) {
        countStartDay++;
      }
      if (e.getStart().toLocalDate().equals(LocalDate.of(2025, 6, 15))) {
        countNextDay++;
      }
    }

    assertEquals(1, countStartDay);
    assertEquals(0, countNextDay);
  }

  /**
   * Verifies that two back-to-back events with no time gap between them
   * are both returned correctly when querying for that day.
   */
  @Test
  public void testBackToBackEventsSameDay() throws Exception {
    ICalendar model = new CalendarModel();
    IView view = new MockView();

    Objects.requireNonNull(CommandParser
                    .parse("create event First from 2025-06-16T09:00 to 2025-06-16T10:00"))
            .execute(model, view);
    Objects.requireNonNull(CommandParser
                    .parse("create event Second from 2025-06-16T10:00 to 2025-06-16T11:00"))
            .execute(model, view);

    List<IEvent> events = new ArrayList<>();
    for (IEvent e : CalendarModel.getAllEvents()) {
      if (e.getStart().toLocalDate().equals(LocalDate.of(2025, 6, 16))) {
        events.add(e);
      }
    }

    assertEquals(2, events.size());
  }

  /**
   * Verifies that overlapping events on the same day are both returned
   * when querying for that day.
   */
  @Test
  public void testOverlappingEventsQuery() throws Exception {
    ICalendar model = new CalendarModel();
    IView view = new MockView();

    Objects.requireNonNull(CommandParser
                    .parse("create event A from 2025-06-18T13:00 to 2025-06-18T14:00"))
            .execute(model, view);
    Objects.requireNonNull(CommandParser
                    .parse("create event B from 2025-06-18T13:30 to 2025-06-18T14:30"))
            .execute(model, view);

    int count = 0;
    for (IEvent e : CalendarModel.getAllEvents()) {
      if (e.getStart().toLocalDate().equals(LocalDate.of(2025, 6, 18))) {
        count++;
      }
    }

    assertEquals(2, count);
  }

  /**
   * Tests behavior when the query returns no events.
   */
  @Test
  public void testQueryReturnsEmpty() throws CommandExecutionException {
    QueryEventsCommand query =
            new QueryEventsCommand(LocalDate.of(2025, 6, 10));
    query.execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("no events"));
  }

  /**
   * Verifies that the calendar correctly identifies whether the user
   * is busy at a specific date and time.
   */
  @Test
  public void testIsUserBusyAtSpecificDateTime() throws Exception {
    ICalendar model = new CalendarModel();
    IView view = new MockView();

    // create an event from 10:00 to 11:00 on June 20, 2025
    String createCmd = "create event Interview from 2025-06-20T10:00 to 2025-06-20T11:00";
    ICommand create = CommandParser.parse(createCmd);
    assert create != null;
    create.execute(model, view);

    // check status at 10:30 (should be busy)
    LocalDateTime checkBusy = LocalDateTime.of(2025, 6, 20, 10, 30);
    assertTrue("User should be busy at 10:30", model.isBusyAt(checkBusy));

    // check status at 11:30 (should be available)
    LocalDateTime checkFree = LocalDateTime.of(2025, 6, 20, 11, 30);
    assertFalse("User should be available at 11:30", model.isBusyAt(checkFree));
  }

  /**
   * Verifies that the calendar correctly returns all events in a specific date-time range.
   */
  @Test
  public void testQueryEventsInDateTimeRange() throws Exception {
    ICalendar model = new CalendarModel();
    IView view = new MockView();

    // create 3 events across multiple days
    Objects.requireNonNull(CommandParser
                    .parse("create event Call from 2025-06-10T09:00 to 2025-06-10T10:00"))
            .execute(model, view);
    Objects.requireNonNull(CommandParser
                    .parse("create event Workshop from 2025-06-11T14:00 to 2025-06-11T16:00"))
            .execute(model, view);
    Objects.requireNonNull(CommandParser
                    .parse("create event Review from 2025-06-12T08:00 to 2025-06-12T09:00"))
            .execute(model, view);

    // emulate range query manually
    List<IEvent> inRange = new ArrayList<>();
    LocalDateTime start = LocalDateTime.of(2025, 6, 10, 0, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 11, 23, 59);

    for (int i = 0; i < 5; i++) {
      LocalDate date = start.toLocalDate().plusDays(i);
      for (ROIEvent e : model.getEventsOn(date)) {
        if (!e.getEnd().isBefore(start) && !e.getStart().isAfter(end)) {
          inRange.add((IEvent) e);
        }
      }
    }

    // should include Call and Workshop, but not Review
    assertEquals(2, inRange.size());
    List<String> subjects = inRange.stream()
            .map(IEvent::getSubject)
            .collect(Collectors.toList());
    assertTrue(subjects.contains("Call"));
    assertTrue(subjects.contains("Workshop"));
    assertFalse(subjects.contains("Review"));
  }
}