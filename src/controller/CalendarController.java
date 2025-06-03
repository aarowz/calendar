// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.ICalendar;
import view.IView;
import exceptions.*;

import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Controls the interaction between user input, the calendar model, and the view.
 * Supports both interactive and headless execution modes.
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
   * Continues until an "exit" command is issued or input ends.
   */
  public void run() {
    while (input.hasNextLine()) {
      String line = input.nextLine().trim();
      if (line.isEmpty()) {
        continue;
      }
      try {
        ICommand command = parseCommand(line);
        executeCommand(command);
      } catch (InvalidCommandException e) {
        renderError("Invalid command: " + e.getMessage());
      }
    }
  }

  /**
   * Parses a line of input into an ICommand using the CommandParser.
   *
   * @param line the user input line
   * @return a parsed ICommand
   * @throws InvalidCommandException if the command is malformed
   */
  private ICommand parseCommand(String line) throws InvalidCommandException {
    return CommandParser.parse(line);
  }

  /**
   * Executes a given command using the model and view.
   *
   * @param command the command to execute
   */
  private void executeCommand(ICommand command) {
    try {
      command.execute(model, view);
    } catch (CommandExecutionException e) {
      renderError("Command failed: " + e.getMessage());
    }
  }

  /**
   * Renders an error message to the user.
   *
   * @param message the message to display
   */
  private void renderError(String message) {
    try {
      view.renderMessage("[ERROR] " + message);
    } catch (IOException e) {
      throw new UncheckedIOException("View failed to render error message", e);
    }
  }

  /**
   * Renders a general message to the user.
   *
   * @param message the message to display
   */
  private void renderMessage(String message) {
    try {
      view.renderMessage(message);
    } catch (IOException e) {
      throw new UncheckedIOException("View failed to render message", e);
    }
  }
}