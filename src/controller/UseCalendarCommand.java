// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import java.io.IOException;

import exceptions.CommandExecutionException;
import model.ICalendar;
import view.IView;

/**
 * Command for setting the active calendar context.
 */
public class UseCalendarCommand implements ICommand {
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException,
          IOException {

  }
}
