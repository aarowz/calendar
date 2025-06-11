// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import java.io.IOException;
import java.time.LocalDateTime;

import exceptions.CommandExecutionException;
import model.IDelegator;
import view.IView;

/**
 * Command for copying a single event from one calendar to another.
 */
public class CopyEventCommand implements ICommand {
  private final String eventName;
  private final LocalDateTime sourceStart;
  private final String targetCalendar;
  private final LocalDateTime targetStart;

  /**
   * Constructs a command to copy a single event across calendars.
   *
   * @param eventName      the subject of the event to copy
   * @param sourceStart    the original start time of the event
   * @param targetCalendar the name of the destination calendar
   * @param targetStart    the new start time in the target calendar
   */
  public CopyEventCommand(String eventName, LocalDateTime sourceStart,
                          String targetCalendar, LocalDateTime targetStart) {
    this.eventName = eventName;
    this.sourceStart = sourceStart;
    this.targetCalendar = targetCalendar;
    this.targetStart = targetStart;
  }

  @Override
  public void execute(IDelegator model, IView view) throws CommandExecutionException, IOException {
    try {
      // delegate to the model to copy the event between calendars
      model.copyEvent(eventName, sourceStart, targetCalendar, targetStart);

      // notify the user of the result
      view.renderMessage("Copied event '" + eventName + "' to calendar '" +
              targetCalendar + "' at " + targetStart);
    } catch (Exception e) {
      // wrap any model-level errors into a command-level failure
      throw new CommandExecutionException("Failed to copy event: " + e.getMessage());
    }
  }
}