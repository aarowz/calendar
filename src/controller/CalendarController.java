// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.ICalendar;
import view.IView;
import exceptions.*;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Controls the interaction between user input, the calendar model, and the view.
 */
public class CalendarController {
  private final ICalendar model;
  private final IView view;
  private final Scanner input;

  /**
   * Constructs a CalendarController with the given model, view, and input stream.
   *
   * @param model the calendar model
   * @param view  the output view
   * @param in    the input stream for user input
   */
  public CalendarController(ICalendar model, IView view, InputStream in) {
    this.model = model;
    this.view = view;
    this.input = new Scanner(in);
  }

  /**
   * Starts the controller loop that reads and processes user commands.
   */
  public void run() {
    // TODO: implement main loop that reads input, parses commands, and executes them
  }

  /**
   * Parses a line of input into an ICommand.
   *
   * @param line the user input line
   * @return a parsed ICommand
   * @throws InvalidCommandException if the command is malformed
   */
  private ICommand parseCommand(String line) throws InvalidCommandException {
    // TODO: delegate to CommandParser.parse(line)
    return null;
  }

  /**
   * Executes a given command using the model and view.
   *
   * @param command the command to execute
   */
  private void executeCommand(ICommand command) {
    // TODO: execute the command and handle CommandExecutionException
  }

  /**
   * Renders an error message to the user.
   *
   * @param message the message to display
   */
  private void renderError(String message) {
    // TODO: render the message using the view
  }

  /**
   * Renders a general message to the user.
   *
   * @param message the message to display
   */
  private void renderMessage(String message) {
    // TODO: render the message using the view
  }
}