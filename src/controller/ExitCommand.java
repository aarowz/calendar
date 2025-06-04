// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.ICalendar;
import view.IView;
import exceptions.CommandExecutionException;

import java.io.IOException;

/**
 * Represents a command to exit the calendar application.
 * In headless mode, it signals the controller to finish processing commands.
 */
public class ExitCommand implements ICommand {

  /**
   * Constructs an ExitCommand.
   * No fields are needed since this command performs a fixed action.
   */
  public ExitCommand() {
    // No initialization required
  }

  /**
   * Executes the exit operation. This typically sets a flag or informs the controller to stop.
   *
   * @param calendar the calendar model (unused)
   * @param view     the output view to display exit message
   * @throws CommandExecutionException if the exit command cannot complete (shouldn't happen)
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    try {
      // if the exit command was valid, exit the program
      view.renderMessage("Exiting calendar application. Goodbye!");
    } catch (IOException e) {
      // otherwise throw an error message
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