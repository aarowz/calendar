// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import java.io.IOException;

import exceptions.CommandExecutionException;
import model.IDelegator;
import view.IView;

/**
 * Command for editing a calendar’s name or timezone.
 * This command delegates to the model to update a given calendar’s metadata.
 */
public class EditCalendarCommand implements ICommand {
  private final String name;
  private final String property;
  private final String newValue;

  /**
   * Constructs an EditCalendarCommand with the specified fields.
   *
   * @param name     the name of the calendar to edit
   * @param property the property to update ("name" or "timezone")
   * @param newValue the new value for the specified property
   */
  public EditCalendarCommand(String name, String property, String newValue) {
    this.name = name;
    this.property = property;
    this.newValue = newValue;
  }

  @Override
  public void execute(IDelegator model, IView view) throws CommandExecutionException, IOException {
    try {
      // tell the model to edit the calendar with the given arguments
      model.editCalendar(name, property, newValue);

      // confirm the update to the user
      view.renderMessage("Updated calendar '" + name + "': " +
              property + " set to '" + newValue + "'");
    } catch (Exception e) {
      // throw a wrapped error if something goes wrong
      throw new CommandExecutionException("Failed to edit calendar: " + e.getMessage());
    }
  }
}