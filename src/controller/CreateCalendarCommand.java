// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import java.io.IOException;

import exceptions.CommandExecutionException;
import model.IDelegator;
import view.IView;

/**
 * Command for creating a new calendar with a unique name and timezone.
 */
public class CreateCalendarCommand implements ICommand {
  @Override
  public void execute(IDelegator model, IView view) throws CommandExecutionException,
          IOException {

  }
}