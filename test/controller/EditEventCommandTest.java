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
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Unit tests for the EditEventCommand class.
 */
public class EditEventCommandTest {

  private MockCalendar calendar;
  private MockView view;

  @Before
  public void setUp() {
    calendar = new MockCalendar();
    view = new MockView();
  }

  /**
   * Verifies that a valid edit operation correctly modifies the target event and reports success.
   */
  @Test
  public void testExecuteValidEdit() throws CommandExecutionException {
    EditEventCommand cmd = new EditEventCommand(
            "Meeting", LocalDateTime.of(2025, 6, 15, 9, 0),
            "Updated Meeting", LocalDateTime.of(2025, 6, 15, 10, 0),
            LocalDateTime.of(2025, 6, 15, 11, 0), "Discuss updates", "Room A", EventStatus.PUBLIC
    );
    cmd.execute(calendar, view);

    assertTrue(calendar.wasEdited);
    assertTrue(view.output.toLowerCase().contains("event updated"));
  }

  /**
   * Verifies that executing the command with a null calendar throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testExecuteWithNullCalendarThrows() throws CommandExecutionException {
    EditEventCommand cmd = new EditEventCommand(
            "Event", LocalDateTime.of(2025, 6, 15, 9, 0),
            "New", LocalDateTime.of(2025, 6, 15, 10, 0),
            null, null, null, null
    );
    cmd.execute(null, view);
  }

  /**
   * Verifies that executing the command with a null view throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testExecuteWithNullViewThrows() throws CommandExecutionException {
    EditEventCommand cmd = new EditEventCommand(
            "Event", LocalDateTime.of(2025, 6, 15, 9, 0),
            "New", LocalDateTime.of(2025, 6, 15, 10, 0),
            null, null, null, null
    );
    cmd.execute(calendar, null);
  }

  /**
   * Verifies that if the calendar fails to locate or edit the event, a CommandExecutionException is thrown.
   */
  @Test(expected = CommandExecutionException.class)
  public void testExecuteFailsIfEventNotFound() throws CommandExecutionException {
    calendar.failEdit = true;
    EditEventCommand cmd = new EditEventCommand(
            "Nonexistent", LocalDateTime.of(2025, 6, 1, 8, 0),
            "Updated", LocalDateTime.of(2025, 6, 1, 9, 0),
            null, null, null, null
    );
    cmd.execute(calendar, view);
  }
}