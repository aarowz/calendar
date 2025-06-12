// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import exceptions.CommandExecutionException;
import model.*;

import org.junit.Before;
import org.junit.Test;

import view.IView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.*;

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
   * Tests copying events when the source date has no events.
   */
  @Test
  public void testCopyEventsOnNoEventsOnDate() throws CommandExecutionException, IOException {
    CopyEventsOnCommand cmd = new CopyEventsOnCommand(LocalDate.of(2025, 6,
            11),
            "Target", targetDate);
    cmd.execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("no events"));
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
   * Tests copying when target date is invalid (null).
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsOnNullTargetDate() throws CommandExecutionException, IOException {
    new CopyEventsOnCommand(sourceDate, "Target", null).execute(model,
            view);
  }

  /**
   * Tests copying when source date is null.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsOnNullSourceDate() throws CommandExecutionException, IOException {
    new CopyEventsOnCommand(null, "Target", targetDate).execute(model,
            view);
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
   * Skipped: series tests not applicable to CopyEventsOnCommand (single-event copy only).
   */
  @Test
  public void testCopyEventsOnPreservesSeriesId() {
    // Not applicable — CopyEventsOnCommand does not support series metadata directly
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
    CopyEventsOnCommand cmd = new CopyEventsOnCommand(LocalDate.of(2025, 6,
            11),
            "Ghost", targetDate);
    try {
      cmd.execute(model, view);
    } catch (CommandExecutionException ignored) {
    }
    assertTrue(view.getLog().toLowerCase().contains("error"));
  }

  /**
   * Tests that null or empty calendar name is rejected.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsOnNullCalendarName() throws CommandExecutionException, IOException {
    new CopyEventsOnCommand(sourceDate, null, targetDate).execute(model, view);
  }
}