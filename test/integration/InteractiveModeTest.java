// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package integration;

import model.CalendarModel;

import org.junit.Before;
import org.junit.Test;

import view.IView;

import java.io.StringReader;
import java.io.IOException;

/**
 * Stubbed tests for the calendar in interactive mode.
 * <p>
 * This class will later verify:
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
   * Stubbed test: create-event command adds an event.
   */
  @Test
  public void testInteractiveCreateEvent() {
    // Stubbed
  }

  /**
   * Stubbed test: edit-event command updates an event.
   */
  @Test
  public void testInteractiveEditEvent() {
    // Stubbed
  }

  /**
   * Stubbed test: query-events returns matching events.
   */
  @Test
  public void testInteractiveQueryEvents() {
    // Stubbed
  }

  /**
   * Stubbed test: unknown command is handled gracefully.
   */
  @Test
  public void testInteractiveHandlesUnknownCommand() {
    // Stubbed
  }

  /**
   * Stubbed test: only exit command cleanly shuts down.
   */
  @Test
  public void testInteractiveExitOnly() {
    // Stubbed
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