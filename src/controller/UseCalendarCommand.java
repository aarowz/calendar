// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import java.io.IOException;

import exceptions.CommandExecutionException;
import model.IDelegator;
import view.IView;

/**
 * Command for setting the active calendar context.
 * This command tells the model to use the specified calendar by name.
 */
public class UseCalendarCommand implements ICommand {
  private final String name;

  /**
   * Constructs a UseCalendarCommand with the given calendar name.
   *
   * @param name the name of the calendar to switch to
   */
  public UseCalendarCommand(String name) {
    this.name = name;
  }

  @Override
  public void execute(IDelegator model, IView view) throws CommandExecutionException, IOException {
    try {
      // tell the model to set the active calendar
      model.useCalendar(name);

      // confirm to the user which calendar is now in use
      view.renderMessage("Now using calendar '" + name + "'");
    } catch (Exception e) {
      // wrap and throw any failure as a command-level error
      throw new CommandExecutionException("Failed to switch calendar: " + e.getMessage());
    }
  }

  @Override
  public String toString() {
    return "UseCalendarCommand: " + name;
  }
}