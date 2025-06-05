// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import exceptions.InvalidCommandException;
import model.ICalendar;
import view.IView;
import exceptions.CommandExecutionException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Represents the main controller for running the calendar program.
 * Responsible for reading input, parsing commands, executing them,
 * and displaying output through the view.
 */
public class CalendarController {

  // scanner for reading input
  private final Scanner scanner;

  // view for rendering output
  private final IView view;

  // model for executing calendar operations
  private final ICalendar model;

  /**
   * Constructs the CalendarController with model, view, and readable input.
   *
   * @param model       the calendar model
   * @param view        the view used to render output
   * @param inputStream the input stream for user input
   */
  public CalendarController(ICalendar model, IView view, InputStream inputStream) {
    this.model = model; // store the calendar model
    this.view = view; // store the view object
    this.scanner = new Scanner(inputStream); // initialize scanner for input
  }

  /**
   * Runs the main control loop: reads commands, parses and executes them.
   * Stops when the user types "exit".
   */
  public void run() {
    // keep reading input while it exists
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim(); // get the line and trim whitespace

      if (line.equalsIgnoreCase("exit")) {
        break; // stop the loop if the user types exit
      }

      try {
        ICommand command = CommandParser.parse(line); // try parsing the input line into a command
        command.execute(model, view); // run the command on model and view
      }
      // there are three different exceptions that can happen here
      catch (IllegalArgumentException | CommandExecutionException | InvalidCommandException e) {
        try {
          // print the error to the view
          view.renderMessage("Error: " + e.getMessage());
        } catch (IOException io) {
          // escalate if rendering fails
          throw new IllegalStateException("View failed: " + io.getMessage());
        }
      }
    }
  }
}