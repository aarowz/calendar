// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.CalendarMulti;
import model.DelegatorImpl;
import model.IDelegator;
import view.IView;

import org.junit.Before;
import org.junit.Test;

import exceptions.CommandExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for ExitCommand.
 * Verifies that the command correctly handles program termination.
 */
public class ExitCommandTest {
  private IDelegator model;
  private MockView view;

  /**
   * A mock view that records messages passed to renderMessage.
   */
  private static class MockView implements IView {
    private final StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) {
      log.append(message).append("\n");
    }

    /**
     * Returns all rendered messages as a string.
     *
     * @return combined log of rendered messages
     */
    public String getLog() {
      return log.toString();
    }
  }

  /**
   * Sets up a fresh model and view before each test.
   */
  @Before
  public void setup() {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();
  }

  /**
   * Tests that executing the exit command renders the goodbye message.
   */
  @Test
  public void testExecuteTriggersExit() throws CommandExecutionException {
    ExitCommand cmd = new ExitCommand();
    cmd.execute(model, view);
    assertTrue(view.getLog().contains("Goodbye"));
  }

  /**
   * Verifies that no other message is rendered besides the goodbye message.
   */
  @Test
  public void testNoMessageRenderedOnExit() throws CommandExecutionException {
    ExitCommand cmd = new ExitCommand();
    cmd.execute(model, view);
    String output = view.getLog().trim();
    assertEquals("Exiting calendar application. Goodbye!", output);
  }

  /**
   * Confirms that the string representation identifies the exit command.
   */
  @Test
  public void testToStringIdentifiesExitCommand() {
    ExitCommand cmd = new ExitCommand();
    assertTrue(cmd.toString().toLowerCase().contains("exit"));
  }
}