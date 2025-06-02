// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package integration;

import model.CalendarEvent;
import model.CalendarModel;

import org.junit.Before;
import org.junit.Test;

import view.IView;

import java.io.StringReader;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Tests for the calendar in interactive mode.
 *
 * This class verifies:
 * - That typed input commands are parsed and executed properly.
 * - That the model updates and output rendering match expectations.
 * - That invalid commands and exit behavior are handled gracefully.
 */
public class InteractiveModeTest {

  private CalendarModel model;
  private MockView view;

  @Before
  public void setUp() {
    model = new CalendarModel();
    view = new MockView();
  }

  /**
   * Tests that a create-event command results in a new event being added to the model.
   */
  @Test
  public void testInteractiveCreateEvent() {
    String input = "create-event 2025-06-15T13:00 Interview Prep\nexit\n";
    CalendarController controller = new CalendarController(model, view, new StringReader(input));
    controller.run();

    assertEquals(1, model.getEventsBetween(
            LocalDateTime.of(2025, 6, 15, 0, 0),
            LocalDateTime.of(2025, 6, 15, 23, 59)
    ).size());

    assertTrue(view.getOutput().toLowerCase().contains("event created"));
  }

  /**
   * Tests that an edit-event command updates an existing event in the model.
   */
  @Test
  public void testInteractiveEditEvent() {
    CalendarEvent event = new CalendarEvent("Original", LocalDateTime.of(2025, 6, 16, 9, 0));
    model.addEvent(event);

    String input = "edit-event 2025-06-16T09:00 Updated 2025-06-16T10:00\nexit\n";
    CalendarController controller = new CalendarController(model, view, new StringReader(input));
    controller.run();

    assertTrue(view.getOutput().toLowerCase().contains("event edited"));
    assertTrue(model.getEventsBetween(
            LocalDateTime.of(2025, 6, 16, 10, 0),
            LocalDateTime.of(2025, 6, 16, 10, 0)
    ).stream().anyMatch(e -> e.getTitle().equals("Updated")));
  }

  /**
   * Tests that a query-events command retrieves and displays relevant events.
   */
  @Test
  public void testInteractiveQueryEvents() {
    model.addEvent(new CalendarEvent("Workout", LocalDateTime.of(2025, 6, 18, 7, 30)));

    String input = "query-events 2025-06-18T00:00 2025-06-18T23:59\nexit\n";
    CalendarController controller = new CalendarController(model, view, new StringReader(input));
    controller.run();

    String output = view.getOutput();
    assertTrue(output.contains("Workout"));
  }

  /**
   * Tests that an unknown command results in an appropriate error message.
   */
  @Test
  public void testInteractiveHandlesUnknownCommand() {
    String input = "do-stuff-now\nexit\n";
    CalendarController controller = new CalendarController(model, view, new StringReader(input));
    controller.run();

    assertTrue(view.getOutput().toLowerCase().contains("unknown command"));
  }

  /**
   * Tests that only the exit command results in clean shutdown without errors.
   */
  @Test
  public void testInteractiveExitOnly() {
    String input = "exit\n";
    CalendarController controller = new CalendarController(model, view, new StringReader(input));
    controller.run();

    assertTrue(view.getOutput().toLowerCase().contains("exiting"));
  }

  /**
   * Simple mock view that stores output for assertions.
   */
  private static class MockView implements IView {
    private final StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) throws IOException {
      log.append(message).append("\n");
    }

    public String getOutput() {
      return log.toString();
    }
  }
}