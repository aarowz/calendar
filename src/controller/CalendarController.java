// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.IDelegator;
import view.IView;

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
  private final IDelegator model;

  /**
   * Constructs the CalendarController with model, view, and readable input.
   *
   * @param model       the calendar model
   * @param view        the view used to render output
   * @param inputStream the input stream for user input
   */
  public CalendarController(IDelegator model, IView view, InputStream inputStream) {
    this.model = model; // store the calendar model
    this.view = view; // store the view object
    this.scanner = new Scanner(inputStream); // initialize scanner for input
  }

  /**
   * Runs the main input-processing loop for the controller.
   * Reads input line-by-line, parses commands, and executes them using the model and view.
   * Terminates when the user enters "exit".
   */
  public void run() {
    // continue reading input as long as lines are available
    while (scanner.hasNextLine()) {
      // read and trim the next input line
      String line = scanner.nextLine().trim();

      // exit command ends the loop
      if (line.equalsIgnoreCase("exit")) {
        break;
      }

      try {
        // parse the input into a command object
        ICommand command = CommandParser.parse(line);

        // execute the parsed command using the model and view
        assert command != null;
        command.execute(model, view);

      } catch (Exception e) {
        try {
          // display any error messages through the view
          view.renderMessage("Error: " + e.getMessage());
        } catch (IOException io) {
          // if rendering the error fails, escalate to an unchecked exception
          throw new IllegalStateException("view failed: " + io.getMessage());
        }
      }
    }
  }
}