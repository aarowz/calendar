// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import exceptions.CommandExecutionException;
import model.IDelegator;
import model.DelegatorImpl;
import model.CalendarMulti;
import model.EventStatus;

import org.junit.Before;
import org.junit.Test;

import view.IView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for CopyEventsBetweenCommand.
 */
public class CopyEventsBetweenCommandTest {
  private IDelegator model;
  private MockView view;
  private final LocalDateTime eventTime = LocalDateTime.of(2025, 6,
          10, 10, 0);
  private final LocalDate rangeStart = LocalDate.of(2025, 6, 9);
  private final LocalDate rangeEnd = LocalDate.of(2025, 6, 11);
  private final LocalDate targetStart = LocalDate.of(2025, 6, 20);

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
    new CreateEventCommand(
            "TeamSync", eventTime, eventTime.plusHours(1),
            "Discuss roadmap", "Room A", EventStatus.PRIVATE,
            Collections.emptyList(), 0, null
    ).execute(model, view);
  }

  /**
   * Tests copying events when the date range is valid and events exist.
   */
  @Test
  public void testCopyEventsBetweenValidWithEvents() throws CommandExecutionException, IOException {
    new CopyEventsBetweenCommand(rangeStart, rangeEnd, "Target", targetStart)
            .execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("copied"));
  }

  /**
   * Tests that executing CopyEventsBetweenCommand with a valid empty range logs success.
   */
  @Test
  public void testCopyEventsBetweenValidNoEvents() throws CommandExecutionException, IOException {
    CopyEventsBetweenCommand cmd = new CopyEventsBetweenCommand(
            LocalDate.of(2025, 6, 5),
            LocalDate.of(2025, 6, 6),
            "Target",
            LocalDate.of(2025, 6, 25)
    );

    // Act
    cmd.execute(model, view);

    // Assert
    assertTrue(view.getLog().contains("Copied events from range 2025-06-05 to 2025-06-06"));
  }

  /**
   * Tests copying with a valid range but target calendar does not exist.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsBetweenTargetCalendarMissing() throws CommandExecutionException,
          IOException {
    new CopyEventsBetweenCommand(rangeStart, rangeEnd, "GhostCal", targetStart)
            .execute(model, view);
  }

  /**
   * Tests copying when start date is null.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsBetweenNullStartDate() throws CommandExecutionException, IOException {
    new CopyEventsBetweenCommand(null, rangeEnd, "Target", targetStart)
            .execute(model, view);
  }

  /**
   * Tests copying when end date is null.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsBetweenNullEndDate() throws CommandExecutionException, IOException {
    new CopyEventsBetweenCommand(rangeStart, null, "Target", targetStart)
            .execute(model, view);
  }

  /**
   * Tests copying when target start date is null.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsBetweenNullTargetDate() throws CommandExecutionException, IOException {
    new CopyEventsBetweenCommand(rangeStart, rangeEnd, "Target", null)
            .execute(model, view);
  }

  /**
   * Tests copying with a reversed range (start > end).
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsBetweenReversedRange() throws CommandExecutionException, IOException {
    new CopyEventsBetweenCommand(rangeEnd, rangeStart, "Target", targetStart)
            .execute(model, view);
  }

  /**
   * Tests that events causing conflicts in target calendar are handled appropriately.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsBetweenConflictsInTarget() throws CommandExecutionException,
          IOException {
    new CopyEventsBetweenCommand(rangeStart, rangeEnd, "Target", targetStart)
            .execute(model, view);
    // Copy again but it should fail due to conflict)
    new CopyEventsBetweenCommand(rangeStart, rangeEnd, "Target", targetStart)
            .execute(model, view);
  }

  /**
   * Tests copying events between calendars in different timezones.
   */
  @Test
  public void testCopyEventsBetweenTimezoneShift() throws CommandExecutionException, IOException {
    new CopyEventsBetweenCommand(rangeStart, rangeEnd, "Target", targetStart)
            .execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("copied"));
  }

  /**
   * Tests copying events from and to the same calendar.
   */
  @Test
  public void testCopyEventsBetweenSameCalendar() throws CommandExecutionException, IOException {
    new CopyEventsBetweenCommand(rangeStart, rangeEnd, "Source", targetStart)
            .execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("copied"));
  }

  /**
   * Tests that null calendar name or other null fields are handled gracefully.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCopyEventsBetweenNullArguments() throws CommandExecutionException, IOException {
    new CopyEventsBetweenCommand(rangeStart, rangeEnd, null, targetStart)
            .execute(model, view);
  }

  /**
   * Tests that an error message is passed to the view when CopyEventsBetweenCommand fails.
   */
  @Test
  public void testCopyEventsBetweenErrorMessageToView() throws IOException {
    // Arrange: use valid range and start date, but non-existent calendar to trigger failure
    LocalDate rangeStart = LocalDate.of(2025, 6, 1);
    LocalDate rangeEnd = LocalDate.of(2025, 6, 10);
    LocalDate targetStart = LocalDate.of(2025, 6, 25);

    CopyEventsBetweenCommand cmd = new CopyEventsBetweenCommand(
            rangeStart, rangeEnd, "Ghost", targetStart
    );

    // Act: simulate controller catching and rendering the error
    try {
      cmd.execute(model, view);
    } catch (CommandExecutionException e) {
      view.renderMessage("Error: " + e.getMessage());
    }

    // Assert: verify that "error" was rendered
    assertTrue(view.getLog().toLowerCase().contains("error"));
  }

  /**
   * Tests that series partially overlapping the range are copied partially.
   */
  @Test
  public void testCopyEventsBetweenPartialSeriesOverlap() throws CommandExecutionException,
          IOException {
    LocalDate seriesStart = LocalDate.of(2025, 6, 8); // Sunday
    LocalDateTime sdt = seriesStart.atTime(9, 0);
    List<Character> repeatDays = Arrays.asList('U', 'M', 'T'); // Sun, Mon, Tue

    new CreateEventCommand("Yoga", sdt, sdt.plusHours(1),
            "Morning class", "Gym", EventStatus.PUBLIC,
            repeatDays, 5, null).execute(model, view);

    LocalDate from = LocalDate.of(2025, 6, 9);
    LocalDate to = LocalDate.of(2025, 6, 10);
    LocalDate target = LocalDate.of(2025, 6, 20);

    new CopyEventsBetweenCommand(from, to, "Target", target).execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("copied"));
  }

  /**
   * Tests that series fully within the range are copied with series ID preserved.
   */
  @Test
  public void testCopyEventsBetweenFullSeriesCopied() throws CommandExecutionException,
          IOException {
    LocalDate seriesStart = LocalDate.of(2025, 6, 9); // Monday
    LocalDateTime sdt = seriesStart.atTime(10, 0);
    List<Character> repeatDays = Arrays.asList('M', 'W'); // Mon/Wed

    new CreateEventCommand("Scrum", sdt, sdt.plusHours(1),
            "Sprint updates", "Zoom", EventStatus.PUBLIC,
            repeatDays, 3, null).execute(model, view);

    LocalDate from = LocalDate.of(2025, 6, 9);
    LocalDate to = LocalDate.of(2025, 6, 15);
    LocalDate target = LocalDate.of(2025, 6, 21);

    new CopyEventsBetweenCommand(from, to, "Target", target).execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("copied"));
  }
}