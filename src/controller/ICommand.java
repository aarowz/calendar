// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import java.io.IOException;

import model.IDelegator;
import view.IView;
import exceptions.CommandExecutionException;

/**
 * Represents a generic command that can be executed on a calendar model.
 * All specific commands (create, edit, query, exit) must implement this interface.
 * Commands encapsulate specific user actions and must support interaction with both
 * the model and view layers.
 */
public interface ICommand {

  /**
   * Executes this command using the given calendar model and view.
   *
   * @param model the calendar model to apply the command to
   * @param view     the view to display any output or error messages
   * @throws CommandExecutionException if the command fails to execute properly
   */
  void execute(IDelegator model, IView view) throws CommandExecutionException, IOException;
}