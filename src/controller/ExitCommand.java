// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.ICalendar;
import view.IView;
import exceptions.CommandExecutionException;

/**
 * Represents a command to exit the calendar application.
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
    // TODO: render a message and signal to the controller that the app should terminate
  }

  /**
   * Returns a string representation of the command.
   */
  @Override
  public String toString() {
    return "ExitCommand{}";
  }
}