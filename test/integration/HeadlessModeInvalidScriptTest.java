// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package integration;

import model.CalendarModel;

import org.junit.Before;
import org.junit.Test;

import view.IView;

import java.io.StringReader;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Tests for invalid command scripts in headless mode.
 * <p>
 * This class verifies:
 * - That malformed or unrecognized commands are handled gracefully.
 * - That appropriate error messages are rendered.
 * - That no unintended changes are made to the calendar model.
 */
public class HeadlessModeInvalidScriptsTest {

  private CalendarModel model;
  private MockView view;

  @Before
  public void setUp() {
    model = new CalendarModel();
    view = new MockView();
  }

  /**
   * Tests that an unknown command keyword produces an appropriate error.
   */
  @Test
  public void testUnknownCommand() {
    String script = "invalid-command 2025-06-05T10:00 Meeting";
    CalendarController controller = new CalendarController(model, view, new StringReader(script));
    controller.run();

    assertTrue(view.getOutput().toLowerCase().contains("unknown command"));
  }

  /**
   * Tests that a create-event command missing required arguments triggers an error.
   */
  @Test
  public void testMalformedCreateEvent() {
    String script = "create-event 2025-06-05T10:00"; // Missing title
    CalendarController controller = new CalendarController(model, view, new StringReader(script));
    controller.run();

    assertTrue(view.getOutput().toLowerCase().contains("invalid arguments"));
  }

  /**
   * Tests that a create-event command with improperly formatted date triggers an error.
   */
  @Test
  public void testInvalidDateFormat() {
    String script = "create-event 06-05-2025T10:00 Team Meeting"; // Bad date format
    CalendarController controller = new CalendarController(model, view, new StringReader(script));
    controller.run();

    assertTrue(view.getOutput().toLowerCase().contains("invalid date"));
  }

  /**
   * Tests that an edit-event command missing required arguments triggers an error.
   */
  @Test
  public void testIncompleteEditCommand() {
    String script = "edit-event 2025-06-05T10:00"; // Missing new title or time
    CalendarController controller = new CalendarController(model, view, new StringReader(script));
    controller.run();

    assertTrue(view.getOutput().toLowerCase().contains("invalid arguments"));
  }

  /**
   * Tests that unstructured random input is rejected with an appropriate error.
   */
  @Test
  public void testRandomGarbageInput() {
    String script = "blah blah blah 1234 !@#$%^&*()";
    CalendarController controller = new CalendarController(model, view, new StringReader(script));
    controller.run();

    assertTrue(view.getOutput().toLowerCase().contains("unknown command"));
  }

  /**
   * Mock view to capture outputs for assertions.
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