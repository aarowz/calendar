// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import java.io.IOException;

import exceptions.CommandExecutionException;
import model.ICalendar;
import view.IView;

/**
 * Command for copying all events in a date range to a new calendar starting at a specified date.
 */
public class CopyEventsBetweenCommand implements ICommand {
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException,
          IOException {

  }
}