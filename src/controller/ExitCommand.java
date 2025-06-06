// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.ICalendar;
import view.IView;
import exceptions.CommandExecutionException;

import java.io.IOException;

/**
 * Represents a command to exit the calendar application.
 * Used in headless mode to terminate command processing cleanly.
 */
public class ExitCommand implements ICommand {

  /**
   * Constructs an ExitCommand.
   * No fields are needed since this command performs a fixed action.
   */
  public ExitCommand() {
    // no initialization required
  }

  /**
   * Executes the exit operation. Displays a goodbye message to the user.
   *
   * @param calendar the calendar model (not used)
   * @param view     the output view to display confirmation
   * @throws CommandExecutionException if the view fails to render the message
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    try {
      // show goodbye message to user
      view.renderMessage("Exiting calendar application. Goodbye!");
    } catch (IOException e) {
      // if rendering fails, throw exception
      throw new CommandExecutionException("Failed to render exit message", e);
    }
  }

  /**
   * Returns a string representation of the command.
   *
   * @return string summary
   */
  @Override
  public String toString() {
    return "ExitCommand{}";
  }
}