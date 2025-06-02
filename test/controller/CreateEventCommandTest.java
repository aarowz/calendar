// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import exceptions.CommandExecutionException;
import model.EventStatus;
import model.ICalendar;
import view.IView;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Unit tests for the CreateEventCommand class.
 */
public class CreateEventCommandTest {

  private MockCalendar calendar;
  private MockView view;

  @Before
  public void setUp() {
    calendar = new MockCalendar();
    view = new MockView();
  }

  /**
   * Verifies that a single non-recurring event is added to the calendar and success is rendered.
   */
  @Test
  public void testExecuteCreatesSingleEvent() throws CommandExecutionException {
    CreateEventCommand cmd = new CreateEventCommand(
            "Doctor Appointment",
            LocalDateTime.of(2025, 6, 10, 15, 0),
            LocalDateTime.of(2025, 6, 10, 16, 0),
            "Annual check-up",
            "Clinic",
            EventStatus.PRIVATE,
            null,
            null,
            null
    );
    cmd.execute(calendar, view);

    assertTrue(calendar.singleEventAdded);
    assertTrue(view.output.toLowerCase().contains("event created"));
  }

  /**
   * Verifies that a recurring event series is added when repeatDays are provided.
   */
  @Test
  public void testExecuteCreatesRecurringEventSeries() throws CommandExecutionException {
    CreateEventCommand cmd = new CreateEventCommand(
            "Lecture Series",
            LocalDateTime.of(2025, 6, 1, 10, 0),
            LocalDateTime.of(2025, 6, 1, 11, 0),
            "Weekly lectures",
            "Room 101",
            EventStatus.PUBLIC,
            Collections.singletonList('M'),  // Repeats every Monday
            4,
            null
    );
    cmd.execute(calendar, view);

    assertTrue(calendar.seriesAdded);
    assertTrue(view.output.toLowerCase().contains("series created"));
  }

  /**
   * Verifies that executing the command with a null calendar throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testExecuteWithNullCalendarThrows() throws CommandExecutionException {
    CreateEventCommand cmd = new CreateEventCommand(
            "Lunch",
            LocalDateTime.of(2025, 6, 10, 12, 0),
            null,
            null,
            null,
            EventStatus.PRIVATE,
            null,
            null,
            null
    );
    cmd.execute(null, view);
  }

  /**
   * Verifies that executing the command with a null view throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testExecuteWithNullViewThrows() throws CommandExecutionException {
    CreateEventCommand cmd = new CreateEventCommand(
            "Lunch",
            LocalDateTime.of(2025, 6, 10, 12, 0),
            null,
            null,
            null,
            EventStatus.PRIVATE,
            null,
            null,
            null
    );
    cmd.execute(calendar, null);
  }

  // ===============================
  // Mock Implementations
  // ===============================

  /**
   * A mock calendar to track method calls for test verification.
   */
  private static class MockCalendar implements ICalendar {
    boolean singleEventAdded = false;
    boolean seriesAdded = false;

    @Override
    public void addEvent(String subject, LocalDateTime start, LocalDateTime end,
                         String description, String location, EventStatus status) {
      singleEventAdded = true;
    }

    @Override
    public void addRecurringSeries(String subject, LocalDateTime start, LocalDateTime end,
                                   String description, String location, EventStatus status,
                                   java.util.List<Character> days, Integer count, LocalDate until) {
      seriesAdded = true;
    }

    // Stub out other methods as needed...
  }

  /**
   * A mock view that logs output to a string for test verification.
   */
  private static class MockView implements IView {
    StringBuilder output = new StringBuilder();

    @Override
    public void renderMessage(String message) throws IOException {
      output.append(message).append("\n");
    }
  }
}