// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

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
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test class for the EditSeriesCommand.
 * This class verifies the correct behavior when editing recurring event series,
 * including recurrence fields, edge conditions, and error handling.
 */
public class EditSeriesCommandTest {
  private IDelegator model;
  private MockView view;

  /**
   * A mock view that records messages passed to renderMessage.
   */
  private static class MockView implements IView {
    StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) {
      log.append(message).append("\n");
    }
  }

  /**
   * Sets up a calendar model with a recurring series.
   */
  @Before
  public void setup() throws CommandExecutionException {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();

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
   * Verifies that editing a non-existent series results in appropriate error handling.
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
   * Ensures the string representation of the command describes the target series.
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
    assertTrue(edit.toString().toLowerCase().contains("checkin"));
    assertTrue(edit.toString().toLowerCase().contains("series"));
  }
}