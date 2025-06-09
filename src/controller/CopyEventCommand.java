// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import java.io.IOException;

import exceptions.CommandExecutionException;
import model.IDelegator;
import view.IView;

/**
 * Command for copying a single event from one calendar to another.
 */
public class CopyEventCommand implements ICommand {
  @Override
  public void execute(IDelegator model, IView view) throws CommandExecutionException,
          IOException {

  }
}
