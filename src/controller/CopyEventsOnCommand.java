// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import java.io.IOException;

import exceptions.CommandExecutionException;
import model.ICalendar;
import view.IView;

/**
 * Command for copying all events from a specific date to a new calendar/date.
 */
public class CopyEventsOnCommand implements ICommand {
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException,
          IOException {

  }
}