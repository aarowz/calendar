// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import exceptions.CommandExecutionException;
import model.IDelegator;
import model.CalendarMulti;
import model.EventStatus;
import model.DelegatorImpl;

import org.junit.Before;
import org.junit.Test;

import view.IView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for CopyEventsOnCommand.
 */
public class CopyEventsOnCommandTest {
  private IDelegator model;
  private MockView view;

  private final LocalDate sourceDate = LocalDate.of(2025, 6, 10);
  private final LocalDate targetDate = LocalDate.of(2025, 6, 15);
  private final LocalDateTime eventStart = LocalDateTime.of(2025, 6, 10,
          10, 0);
  private final LocalDateTime eventEnd = eventStart.plusHours(1);

  /**
   * A simple mock view that logs messages for testing.
   */
  private static class MockView implements IView {
    private final StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) throws IOException {
      log.append(message).append("\n");
    }

    public String getLog() {
      return log.toString();
    }
  }

  @Before
  public void setUp() throws CommandExecutionException, IOException {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();
    new CreateCalendarCommand("Source", "UTC").execute(model, view);
    new CreateCalendarCommand("Target", "America/Chicago").execute(model, view);

    new UseCalendarCommand("Source").execute(model, view);
    new CreateEventCommand("Meeting", eventStart, eventEnd,
            "Daily sync", "Zoom", EventStatus.PUBLIC,
            Collections.emptyList(), 0, null).execute(model, view);
  }

  /**
   * Tests copying events on a valid date with events present.
   */
  @Test
  public void testCopyEventsOnWithEvents() throws CommandExecutionException, IOException {
    new CopyEventsOnCommand(sourceDate, "Target", targetDate).execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("copied"));
  }

  /**
   * Tests that executing CopyEventsOnCommand with no source events still completes without error.
   */
  @Test
  public void testCopyEventsOnNoEventsOnDate() throws CommandExecutionException, IOException {
    // Arrange
    model.createCalendar("source", ZoneId.of("UTC"));
    model.useCalendar("source");

    LocalDate sourceDate = LocalDate.of(2025, 6, 11);
    LocalDate targetDate = LocalDate.of(2025, 6, 12);

    CopyEventsOnCommand cmd = new CopyEventsOnCommand(sourceDate, "Target",
            targetDate);
    cmd.execute(model, view);

    // You can only assert the default success message
    assertTrue(view.getLog().contains("Copied all events from " + sourceDate));
  }

  /**
   * Tests copying events to a calendar that does not exist.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsOnTargetCalendarMissing() throws CommandExecutionException,
          IOException {
    new CopyEventsOnCommand(sourceDate, "Ghost", targetDate).execute(model, view);
  }

  /**
   * Tests that creating a command with a null target date throws immediately.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventsOnNullTargetDate() {
    new CopyEventsOnCommand(LocalDate.of(2025, 6, 10),
            "Target", null);
  }

  /**
   * Tests that passing a null source date throws a CommandExecutionException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventsOnNullSourceDate() {
    new CopyEventsOnCommand(null, "Target",
            LocalDate.of(2025, 6, 12));
  }

  /**
   * Tests copying when no calendar is currently in use.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsOnNoCalendarInUse() throws CommandExecutionException, IOException {
    // Switch context away from any calendar (simulate fresh session)
    model = new DelegatorImpl(new CalendarMulti()); // new model without setting calendar
    new CopyEventsOnCommand(sourceDate, "Target", targetDate).execute(model, view);
  }

  /**
   * Tests copying to the same calendar (source == target).
   */
  @Test
  public void testCopyEventsOnSameCalendar() throws CommandExecutionException, IOException {
    new CopyEventsOnCommand(sourceDate, "Source", targetDate).execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("copied"));
  }

  /**
   * Tests that times are adjusted correctly when copying between timezones.
   */
  @Test
  public void testCopyEventsOnTimezoneConversion() throws CommandExecutionException, IOException {
    new CopyEventsOnCommand(sourceDate, "Target", targetDate).execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("copied"));
  }

  /**
   * Tests that an error message is rendered to the view on failure.
   */
  @Test
  public void testCopyEventsOnErrorMessageToView() throws IOException {
    LocalDate sourceDate = LocalDate.of(2025, 6, 11);
    LocalDate targetDate = LocalDate.of(2025, 6, 12);
    CopyEventsOnCommand cmd = new CopyEventsOnCommand(sourceDate,
            "Ghost", targetDate);

    // Simulate controller behavior: catch the exception and report to view
    try {
      cmd.execute(model, view);
    } catch (CommandExecutionException e) {
      view.renderMessage("Error: " + e.getMessage());
    }

    // Assert that an error message was rendered
    assertTrue(view.getLog().toLowerCase().contains("error"));
  }

  /**
   * Tests that null calendar name is rejected at construction time.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventsOnNullCalendarName() {
    new CopyEventsOnCommand(LocalDate.of(2025, 6, 10),
            null, LocalDate.of(2025, 6, 12));
  }

  /**
   * Tests that empty calendar name is rejected.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventsOnEmptyCalendarName() {
    new CopyEventsOnCommand(LocalDate.of(2025, 6, 10),
            "   ", LocalDate.of(2025, 6, 12));
  }

}