// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import java.io.IOException;
import java.time.ZoneId;

import exceptions.CommandExecutionException;
import model.IDelegator;
import view.IView;

/**
 * Command for creating a new calendar with a unique name and timezone.
 */
public class CreateCalendarCommand implements ICommand {
  private final String name;
  private final String zoneId;

  /**
   * constructor that takes the calendar name and its timezone as strings
   *
   * @param name   the name of the calendar to create
   * @param zoneId the IANA timezone string for the new calendar
   */
  public CreateCalendarCommand(String name, String zoneId) {
    this.name = name;
    this.zoneId = zoneId;
  }

  @Override
  public void execute(IDelegator model, IView view) throws CommandExecutionException, IOException {
    try {
      // parse the zone string into a ZoneId object
      ZoneId zone = ZoneId.of(zoneId);

      // create the calendar using the model
      model.createCalendar(name, zone);

      // confirm to the user that the calendar was created
      view.renderMessage("Created calendar '" + name + "' with timezone '" + zoneId + "'");
    } catch (Exception e) {
      // wrap and rethrow any exceptions as a command-level error
      throw new CommandExecutionException("Failed to create calendar: " + e.getMessage());
    }
  }
}