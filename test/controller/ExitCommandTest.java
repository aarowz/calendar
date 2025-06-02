// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import controller.ExitCommand;
import model.ICalendar;
import view.IView;
import exceptions.CommandExecutionException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for ExitCommand.
 * Verifies that exit command renders a message without errors.
 */
public class ExitCommandTest {

  private TestView view;
  private ICalendar dummyCalendar;

  @Before
  public void setUp() {
    view = new TestView();
    dummyCalendar = new ICalendar() {
      // Dummy no-op implementation
    };
  }

  @Test
  public void testExecuteRendersExitMessage() throws CommandExecutionException {
    ExitCommand cmd = new ExitCommand();
    cmd.execute(dummyCalendar, view);

    assertTrue(view.output.toLowerCase().contains("exit") || view.output.toLowerCase().contains("goodbye"));
  }

  // Mock view for capturing output
  private static class TestView implements IView {
    StringBuilder output = new StringBuilder();

    @Override
    public void render(String message) {
      output.append(message);
    }
  }
}