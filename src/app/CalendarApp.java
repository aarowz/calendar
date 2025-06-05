// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package app;

import controller.CalendarController;
import model.CalendarModel;
import model.ICalendar;
import view.CalendarView;
import view.IView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * The main entry point for the Calendar application.
 * Supports both interactive and headless script modes based on command-line input.
 */
public class CalendarApp {

  /**
   * Launches the calendar application based on mode and optional input file.
   *
   * @param args command-line arguments, starting with --mode
   */
  public static void main(String[] args) {

    // check for minimum argument requirements and valid mode flag
    if (args.length < 2 || !args[0].equalsIgnoreCase("--mode")) {
      System.err.println("Usage: --mode [interactive|headless <file>]");
      return;
    }

    // create the model
    ICalendar model = new CalendarModel();

    // set up the view with output directed to stdout
    IView view = new CalendarView.Builder()
            .setOutput(new PrintWriter(System.out, true))
            .build();

    try {
      // if mode is interactive, just use standard input
      if (args[1].equalsIgnoreCase("interactive")) {
        CalendarController controller = new CalendarController(model, view, System.in);
        controller.run();
      }
      // if headless, make sure the input file is provided
      else if (args[1].equalsIgnoreCase("headless") && args.length > 2) {
        InputStream in = new FileInputStream(args[2]); // use input file
        CalendarController controller = new CalendarController(model, view, in);
        controller.run();
      }
      // if mode is unrecognized or file is missing
      else {
        System.err.println("Invalid mode or missing file for headless mode.");
      }
    } catch (Exception e) {
      // catch anything else that goes wrong
      System.err.println("Application error: " + e.getMessage());
    }
  }
}