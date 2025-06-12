// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import model.*;
import view.IView;
import exceptions.CommandExecutionException;

import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test suite for the {@link CreateEventCommand}.
 * Verifies correct creation and behavior of single and recurring events,
 * with and without optional fields, as well as edge cases like duplicate creation
 * and proper time boundaries for recurrence.
 */
public class CreateEventCommandTest {
  private IDelegator model;
  private MockView view;

  /**
   * A mock view that logs all messages passed to {@code renderMessage}.
   */
  private static class MockView implements IView {
    StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) {
      log.append(message).append("\n");
    }

    public String getLog() {
      return log.toString();
    }
  }

  /**
   * Sets up a Delegator with a default calendar and view before each test.
   */
  @Before
  public void setup() {
    model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");
    view = new MockView();
  }

  /**
   * Tests creation of a single one-time event and validates its presence and log output.
   */
  @Test
  public void testExecuteCreatesSingleEvent() throws CommandExecutionException {
    CreateEventCommand cmd = new CreateEventCommand(
            "Team Meeting",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            LocalDateTime.of(2025, 6, 6, 11, 0),
            null, null, EventStatus.PUBLIC, null, null, null
    );
    cmd.execute(model, view);
    List<IEvent> events = CalendarModel.getAllEvents();
    assertEquals(1, events.size());
    assertTrue(events.get(0).getSubject().contains("Team Meeting"));
    assertTrue(view.getLog().toLowerCase().contains("created"));
  }

  /**
   * Verifies that a recurring event series set to repeat for a fixed number of times
   * is created and all generated events match the expected pattern.
   */
  @Test
  public void testSeriesRepeatsNTimes() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new MockView();
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event StudySession from 2025-06-02T13:00 to 2025-06-02T14:00 repeats MW for 5 times";
    ICommand command = CommandParser.parse(model, input);
    assertNotNull(command);
    command.execute(model, view);

    List<ROIEvent> allEvents = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      allEvents.addAll(model.getEventsOn(date));
    }

    for (ROIEvent event : allEvents) {
      assertEquals("StudySession", event.getSubject());
      assertEquals(13, event.getStart().getHour());
      assertEquals(14, event.getEnd().getHour());
    }
  }

  /**
   * Tests that a recurring event series set to repeat for a count
   * actually creates the correct number of event instances.
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
   * Tests that a recurring event series set to repeat until a specific end date
   * only includes events up to and including that date.
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
   * Verifies that an event can be created with only required fields present.
   */
  @Test
  public void testExecuteWithMissingOptionalFields() throws CommandExecutionException {
    CreateEventCommand cmd = new CreateEventCommand(
            "Solo Work",
            LocalDateTime.of(2025, 6, 7, 14, 0),
            LocalDateTime.of(2025, 6, 7, 16, 0),
            null, null, EventStatus.PRIVATE, null, null, null
    );
    cmd.execute(model, view);
    List<IEvent> events = CalendarModel.getAllEvents();
    assertEquals(1, events.size());
    assertTrue(events.get(0).getSubject().contains("Solo Work"));
  }

  /**
   * Verifies that a recurring series stops at the correct 'until' date.
   */
  @Test
  public void testSeriesRepeatsUntilDate() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new MockView();
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event Review from 2025-06-02T10:00 to 2025-06-02T11:00 repeats MW until 2025-06-12";
    ICommand command = CommandParser.parse(model, input);
    assertNotNull(command);
    command.execute(model, view);

    List<ROIEvent> allEvents = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      allEvents.addAll(model.getEventsOn(date));
    }

    for (ROIEvent e : allEvents) {
      assertEquals("Review", e.getSubject());
      assertFalse(e.getStart().toLocalDate().isAfter(LocalDate.of(2025, 6, 12)));
    }
  }

  /**
   * Verifies that an all-day event defaults to 8 AM to 5 PM.
   */
  @Test
  public void testCreateAllDayEvent() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new MockView();
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event Workshop on 2025-06-07";
    ICommand command = CommandParser.parse(model, input);
    assertNotNull(command);
    command.execute(model, view);

    List<ROIEvent> events = model.getEventsOn(LocalDate.of(2025, 6, 7));
    assertEquals(1, events.size());

    IEvent event = (IEvent) events.get(0);
    assertEquals("Workshop", event.getSubject());
    assertEquals(LocalDateTime.of(2025, 6, 7, 8, 0), event.getStart());
    assertEquals(LocalDateTime.of(2025, 6, 7, 17, 0), event.getEnd());
  }

  /**
   * Ensures a duplicate event (same subject, start, end) is rejected.
   */
  @Test(expected = CommandExecutionException.class)
  public void testExecuteFailsGracefullyOnModelError() throws CommandExecutionException {
    CreateEventCommand cmd1 = new CreateEventCommand(
            "Duplicate",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            LocalDateTime.of(2025, 6, 6, 11, 0),
            null, null, EventStatus.PUBLIC, null, null, null
    );
    CreateEventCommand cmd2 = new CreateEventCommand(
            "Duplicate",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            LocalDateTime.of(2025, 6, 6, 11, 0),
            null, null, EventStatus.PUBLIC, null, null, null
    );
    cmd1.execute(model, view);
    cmd2.execute(model, view); // should throw
  }

  /**
   * Tests that success message for a single event is shown.
   */
  @Test
  public void testSuccessMessageSingleEvent() throws CommandExecutionException {
    CreateEventCommand cmd = new CreateEventCommand(
            "Briefing",
            LocalDateTime.of(2025, 6, 6, 11, 0),
            LocalDateTime.of(2025, 6, 6, 12, 0),
            null, null, EventStatus.PUBLIC, null, null, null
    );
    cmd.execute(model, view);
    assertTrue(view.getLog().contains("Briefing"));
  }

  /**
   * Ensures that all events in a series start and end on the same day.
   */
  @Test
  public void testAllEventsInSeriesAreSameDay() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new MockView();
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event Class from 2025-06-02T09:00 to 2025-06-02T10:00 repeats MW for 4 times";
    ICommand command = CommandParser.parse(model, input);
    assertNotNull(command);
    command.execute(model, view);

    List<ROIEvent> allEvents = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      allEvents.addAll(model.getEventsOn(date));
    }

    for (ROIEvent e : allEvents) {
      assertEquals(e.getStart().toLocalDate(), e.getEnd().toLocalDate());
    }
  }

  /**
   * Ensures the subject appears in the string representation.
   */
  @Test
  public void testToStringDisplaysSubject() {
    CreateEventCommand cmd = new CreateEventCommand(
            "Demo Day",
            LocalDateTime.of(2025, 6, 6, 16, 0),
            LocalDateTime.of(2025, 6, 6, 17, 0),
            null, null, EventStatus.PRIVATE, null, null, null
    );
    assertTrue(cmd.toString().contains("Demo Day"));
  }

  /**
   * Ensures a recurring event only occurs on specified weekdays.
   */
  @Test
  public void testSeriesRepeatsOnExpectedWeekdays() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new MockView();
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event Seminar from 2025-06-02T15:00 to 2025-06-02T16:00 repeats MW for 5 times";
    ICommand command = CommandParser.parse(model, input);
    assertNotNull(command);
    command.execute(model, view);

    List<ROIEvent> allEvents = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      allEvents.addAll(model.getEventsOn(date));
    }

    for (ROIEvent event : allEvents) {
      DayOfWeek day = event.getStart().getDayOfWeek();
      assertTrue(day == DayOfWeek.MONDAY || day == DayOfWeek.WEDNESDAY);
    }
  }
}