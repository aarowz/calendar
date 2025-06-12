// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import model.CalendarMulti;
import model.DelegatorImpl;
import model.EventStatus;
import model.IDelegator;
import view.IView;

import org.junit.Before;
import org.junit.Test;

import exceptions.CommandExecutionException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test class for the EditSeriesCommand.
 * Verifies behavior when editing recurring event series, including valid edits,
 * error conditions, and description correctness.
 */
public class EditSeriesCommandTest {
  private IDelegator model;
  private MockView view;

  /**
   * A mock view that accumulates messages for verification.
   */
  private static class MockView implements IView {
    StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) {
      log.append(message).append("\n");
    }
  }

  /**
   * Sets up the calendar and a recurring "Checkin" series.
   */
  @Before
  public void setup() throws CommandExecutionException {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    List<Character> days = List.of('M', 'W', 'F');
    CreateEventCommand cmd = new CreateEventCommand(
            "Checkin",
            LocalDateTime.of(2025, 6, 2, 10, 0),
            LocalDateTime.of(2025, 6, 2, 11, 0),
            "Weekly check-in",
            "Room A",
            EventStatus.PUBLIC,
            days,
            6,
            null
    );
    cmd.execute(model, view);
  }

  /**
   * Verifies that attempting to edit a non-existent series throws an error.
   */
  @Test(expected = CommandExecutionException.class)
  public void testEditNonExistentSeriesFails() throws CommandExecutionException, IOException {
    EditSeriesCommand edit = new EditSeriesCommand(
            "GhostSeries",
            LocalDateTime.of(2025, 6, 1, 10, 0),
            "Oops",
            null,
            null,
            null,
            null,
            "public"
    );
    edit.execute(model, view);
  }

  /**
   * Verifies that the toString method includes the subject and series keyword.
   */
  @Test
  public void testToStringDescribesSeries() {
    EditSeriesCommand edit = new EditSeriesCommand(
            "Checkin",
            LocalDateTime.of(2025, 6, 2, 10, 0),
            "Plan",
            null,
            null,
            null,
            null,
            "public"
    );

    String desc = edit.toString().toLowerCase();
    assertTrue(desc.contains("checkin"));
    assertTrue(desc.contains("series"));
  }
}