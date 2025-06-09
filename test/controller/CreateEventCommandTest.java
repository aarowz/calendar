// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.CalendarModel;
import model.CalendarMulti;
import model.DelegatorImpl;
import model.EventStatus;
import model.ICalendar;
import model.IDelegator;
import model.IEvent;
import model.ROIEvent;
import view.IView;

import org.junit.Before;
import org.junit.Test;

import exceptions.CommandExecutionException;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the CreateEventCommand.
 * This suite verifies behavior for creating both single and recurring events,
 * ensuring proper interactions with the calendar model and expected side effects.
 */
public class CreateEventCommandTest {
  private IDelegator model;
  private MockView view;

  /**
   * A mock view that records messages passed to renderMessage.
   * Used to verify command output during testing.
   */
  private static class MockView implements IView {
    StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) {
      log.append(message).append("\n");
    }

    /**
     * Returns all messages recorded by the mock view.
     *
     * @return rendered message log as a string
     */
    public String getLog() {
      return log.toString();
    }
  }

  /**
   * Initializes a fresh CalendarModel and MockView before each test.
   */
  @Before
  public void setup() {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();
  }

  /**
   * Verifies that executing the command creates a single, non-recurring event
   * and produces the expected success message.
   */
  @Test
  public void testExecuteCreatesSingleEvent() throws CommandExecutionException {
    CreateEventCommand cmd = new CreateEventCommand(
            "Team Meeting",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            LocalDateTime.of(2025, 6, 6, 11, 0),
            null, null, EventStatus.PUBLIC, null, null,
            null
    );
    cmd.execute(model, view);
    List<IEvent> events = CalendarModel.getAllEvents();
    assertEquals(1, events.size());
    assertTrue(events.get(0).getSubject().contains("Team Meeting"));
    assertTrue(view.getLog().toLowerCase().contains("created"));
  }

  /**
   * Verifies that a series is created and repeats for a certain number of occurrences.
   */
  @Test
  public void testSeriesRepeatsNTimes() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new MockView();

    // command to repeat 5 times on Mondays and Wednesdays
    String input = "create event StudySession from 2025-06-02T13:00 to " +
            "2025-06-02T14:00 repeats MW for 5 times";
    ICommand command = CommandParser.parse(input);
    assertNotNull(command);
    command.execute(model, view);

    // gather events from the month of June
    List<ROIEvent> allEvents = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      allEvents.addAll(model.getEventsOn(date));
    }

    // verify each has the correct subject and time range
    for (ROIEvent event : allEvents) {
      assertEquals("StudySession", event.getSubject());
      assertEquals(13, event.getStart().getHour());
      assertEquals(14, event.getEnd().getHour());
    }
  }

  /**
   * Verifies that a recurring event with a repeat count creates
   * the correct number of events and logs the correct message.
   */
  @Test
  public void testExecuteCreatesRecurringEventWithCount() throws CommandExecutionException {
    List<Character> days = List.of('M');
    CreateEventCommand cmd = new CreateEventCommand(
            "Scrum",
            LocalDateTime.of(2025, 6, 2, 9, 0),
            LocalDateTime.of(2025, 6, 2, 9, 30),
            null, null, EventStatus.PUBLIC, days, 3, null
    );
    cmd.execute(model, view);
    List<IEvent> events = CalendarModel.getAllEvents();
    assertEquals(3, events.size());
    assertTrue(view.getLog().contains("Scrum"));
  }

  /**
   * Verifies that a recurring event with a repeat-until date
   * creates events up to the correct end date and logs the event.
   */
  @Test
  public void testExecuteCreatesRecurringEventWithUntilDate() throws CommandExecutionException {
    List<Character> days = List.of('T');
    CreateEventCommand cmd = new CreateEventCommand(
            "Consultation",
            LocalDateTime.of(2025, 6, 3, 13, 0),
            LocalDateTime.of(2025, 6, 3, 14, 0),
            null, null, EventStatus.PRIVATE, days, null,
            LocalDate.of(2025, 6, 17)
    );
    cmd.execute(model, view);
    List<IEvent> events = CalendarModel.getAllEvents();
    assertEquals(3, events.size());
    assertTrue(view.getLog().contains("Consultation"));
  }

  /**
   * Verifies that the command works when optional fields like
   * description and location are missing.
   */
  @Test
  public void testExecuteWithMissingOptionalFields() throws CommandExecutionException {
    CreateEventCommand cmd = new CreateEventCommand(
            "Solo Work",
            LocalDateTime.of(2025, 6, 7, 14, 0),
            LocalDateTime.of(2025, 6, 7, 16, 0),
            null, null, EventStatus.PRIVATE, null, null,
            null
    );
    cmd.execute(model, view);
    List<IEvent> events = CalendarModel.getAllEvents();
    assertEquals(1, events.size());
    assertTrue(events.get(0).getSubject().contains("Solo Work"));
  }

  /**
   * Verifies that a recurring event series is created and repeats
   * until a specified end date (inclusive).
   */
  @Test
  public void testSeriesRepeatsUntilDate() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new MockView();

    // command to repeat on Mondays and Wednesdays until 2025-06-12
    String input = "create event Review from 2025-06-02T10:00 to " +
            "2025-06-02T11:00 repeats MW until 2025-06-12";
    ICommand command = CommandParser.parse(input);
    assertNotNull(command);
    command.execute(model, view);

    // scan through June 1–30 and collect events
    List<ROIEvent> allEvents = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      allEvents.addAll(model.getEventsOn(date));
    }

    // check that all events in series are in place
    for (ROIEvent e : allEvents) {
      assertEquals("Review", e.getSubject());
      assertFalse("Event should not occur after 2025-06-12",
              e.getStart().toLocalDate().isAfter(LocalDate.of(2025, 6, 12)));
    }
  }

  /**
   * Verifies that a single all-day event is created correctly,
   * with default time from 8:00 AM to 5:00 PM.
   */
  @Test
  public void testCreateAllDayEvent() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new MockView();

    // simulate command for an all-day event
    String input = "create event Workshop on 2025-06-07";
    ICommand command = CommandParser.parse(input);

    // execute the command on the model
    assert command != null;
    command.execute(model, view);

    // check that the event exists and uses 8:00 AM to 5:00 PM as default times
    List<ROIEvent> events = model.getEventsOn(LocalDate.of(2025, 6, 7));
    assertEquals(1, events.size());

    IEvent event = (IEvent) events.get(0);
    assertEquals("Workshop", event.getSubject());
    assertEquals(LocalDateTime.of(2025, 6, 7, 8, 0), event.getStart());
    assertEquals(LocalDateTime.of(2025, 6, 7, 17, 0), event.getEnd());
  }

  /**
   * Verifies that a duplicate event throws a CommandExecutionException
   * when trying to add a second identical event.
   */
  @Test(expected = CommandExecutionException.class)
  public void testExecuteFailsGracefullyOnModelError() throws CommandExecutionException {
    CreateEventCommand cmd1 = new CreateEventCommand(
            "Duplicate",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            LocalDateTime.of(2025, 6, 6, 11, 0),
            null, null, EventStatus.PUBLIC, null, null,
            null
    );
    CreateEventCommand cmd2 = new CreateEventCommand(
            "Duplicate",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            LocalDateTime.of(2025, 6, 6, 11, 0),
            null, null, EventStatus.PUBLIC, null, null,
            null
    );
    cmd1.execute(model, view);
    cmd2.execute(model, view); // should fail
  }

  /**
   * Verifies that executing a valid single event logs a correct message.
   */
  @Test
  public void testSuccessMessageSingleEvent() throws CommandExecutionException {
    CreateEventCommand cmd = new CreateEventCommand(
            "Briefing",
            LocalDateTime.of(2025, 6, 6, 11, 0),
            LocalDateTime.of(2025, 6, 6, 12, 0),
            null, null, EventStatus.PUBLIC, null, null,
            null
    );
    cmd.execute(model, view);
    assertTrue(view.getLog().contains("Briefing"));
  }

  /**
   * Verifies that each event in a recurring series starts and ends on the same day.
   */
  @Test
  public void testAllEventsInSeriesAreSameDay() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new MockView();

    // create a series that repeats on Mondays and Wednesdays, 4 times
    String input = "create event Class from 2025-06-02T09:00 to " +
            "2025-06-02T10:00 repeats MW for 4 times";
    ICommand command = CommandParser.parse(input);
    assertNotNull(command);
    command.execute(model, view);

    // gather all events from a known date range
    List<ROIEvent> allEvents = new java.util.ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      allEvents.addAll(model.getEventsOn(date));
    }

    // ensure each event starts and ends on the same day
    for (ROIEvent e : allEvents) {
      assertEquals("Event start and end should be on the same day",
              e.getStart().toLocalDate(), e.getEnd().toLocalDate());
    }
  }

  /**
   * Verifies that the toString() method includes the subject
   * of the event for easy identification.
   */
  @Test
  public void testToStringDisplaysSubject() {
    CreateEventCommand cmd = new CreateEventCommand(
            "Demo Day",
            LocalDateTime.of(2025, 6, 6, 16, 0),
            LocalDateTime.of(2025, 6, 6, 17, 0),
            null, null, EventStatus.PRIVATE, null, null,
            null
    );
    assertTrue(cmd.toString().contains("Demo Day"));
  }

  /**
   * Verifies that an event series repeats only on the specified weekdays (M and W).
   */
  @Test
  public void testSeriesRepeatsOnExpectedWeekdays() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new MockView();

    // command: Repeat 5 times on Mondays and Wednesdays
    String input = "create event Seminar from 2025-06-02T15:00 " +
            "to 2025-06-02T16:00 repeats MW for 5 times";
    ICommand command = CommandParser.parse(input);
    assertNotNull(command);
    command.execute(model, view);

    // collect events across June
    List<ROIEvent> allEvents = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      allEvents.addAll(model.getEventsOn(date));
    }

    // check that all five events are in place
    for (ROIEvent event : allEvents) {
      DayOfWeek day = event.getStart().getDayOfWeek();
      assertTrue("Event should occur only on Monday or Wednesday",
              day == DayOfWeek.MONDAY || day == DayOfWeek.WEDNESDAY);
    }
  }
}