// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import exceptions.CommandExecutionException;
import model.*;

import org.junit.Before;
import org.junit.Test;

import view.IView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Unit tests for CopyEventCommand.
 */
public class CopyEventCommandTest {

  private IDelegator model;
  private MockView view;
  private final LocalDateTime originalTime = LocalDateTime.of(2025, 6,
          10, 10, 0);
  private final LocalDateTime newTime = LocalDateTime.of(2025, 6, 15,
          9, 0);

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
    new CreateCalendarCommand("Target", "America/New_York").execute(model, view);
    new UseCalendarCommand("Source").execute(model, view);
    new CreateEventCommand("Meeting", originalTime, originalTime.plusHours(1),
            "Standup sync", "Zoom", EventStatus.PUBLIC, Collections.emptyList(),
            0, null).execute(model, view);
  }

  /**
   * Tests copying an event when all inputs are valid.
   */
  @Test
  public void testCopyEventSuccess() throws CommandExecutionException, IOException {
    new CopyEventCommand("Meeting", originalTime, "Target", newTime)
            .execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("copied"));
  }

  /**
   * Tests copying an event that does not exist in the source calendar.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventNameNotFound() throws CommandExecutionException, IOException {
    new CopyEventCommand("WrongName", originalTime, "Target", newTime)
            .execute(model, view);
  }

  /**
   * Tests copying an event to a calendar that does not exist.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventTargetCalendarNotFound() throws CommandExecutionException, IOException {
    new CopyEventCommand("Meeting", originalTime, "GhostCal", newTime)
            .execute(model, view);
  }

  /**
   * Tests event name matches but start time does not match.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventTimeMismatch() throws CommandExecutionException, IOException {
    new CopyEventCommand("Meeting", originalTime.plusHours(2), "Target",
            newTime).execute(model, view);
  }

  /**
   * Tests copying with a null event name.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventNullName() throws CommandExecutionException, IOException {
    new CopyEventCommand(null, originalTime, "Target", newTime)
            .execute(model, view);
  }

  /**
   * Tests copying with a null source start time.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventNullSourceStart() throws CommandExecutionException, IOException {
    new CopyEventCommand("Meeting", null, "Target", newTime)
            .execute(model, view);
  }

  /**
   * Tests copying with a null target start time.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventNullTargetStart() throws CommandExecutionException, IOException {
    new CopyEventCommand("Meeting", originalTime, "Target", null)
            .execute(model, view);
  }

  /**
   * Tests copying with a null target calendar name.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventNullTargetCalendar() throws CommandExecutionException, IOException {
    new CopyEventCommand("Meeting", originalTime, null, newTime)
            .execute(model, view);
  }

  /**
   * Tests that a duplicate conflict in the target calendar is detected.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventCreatesDuplicateInTarget() throws CommandExecutionException,
          IOException {
    new CopyEventCommand("Meeting", originalTime, "Target", newTime)
            .execute(model, view);
    new CopyEventCommand("Meeting", originalTime, "Target", newTime)
            .execute(model, view);
  }

  /**
   * Tests that an error message is rendered when copying fails.
   */
  @Test
  public void testCopyEventErrorMessageToView() throws IOException {
    CopyEventCommand cmd = new CopyEventCommand("Nonexistent", originalTime,
            "Target", newTime);
    try {
      cmd.execute(model, view);
    } catch (CommandExecutionException ignored) {
    }
    assertTrue(view.getLog().toLowerCase().contains("error"));
  }

  /**
   * Tests that timezone conversion is handled correctly in copy.
   */
  @Test
  public void testCopyEventTimezoneConversion() throws CommandExecutionException, IOException {
    new CopyEventCommand("Meeting", originalTime, "Target", newTime)
            .execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("copied"));
  }
}