// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package app;

import controller.CalendarController;
import model.CalendarModel;
import model.ICalendar;
import view.ConsoleViewBuilder;
import view.IView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * The main entry point for the Calendar application.
 * Supports interactive and headless modes as specified via command line args.
 */
public class CalendarApp {

  /**
   * Main method for launching the calendar application.
   *
   * @param args command-line arguments specifying mode and input source
   */
  public static void main(String[] args) {
    if (args.length < 2 || !args[0].equalsIgnoreCase("--mode")) {
      System.err.println("Usage: --mode [interactive|headless <file>]");
      return;
    }

    ICalendar model = new CalendarModel();
    IView view = new ConsoleViewBuilder()
            .setOutput(new PrintWriter(System.out, true))
            .build();

    try {
      if (args[1].equalsIgnoreCase("interactive")) {
        CalendarController controller = new CalendarController(model, view, System.in);
        controller.run();
      } else if (args[1].equalsIgnoreCase("headless") && args.length > 2) {
        InputStream in = new FileInputStream(args[2]);
        CalendarController controller = new CalendarController(model, view, in);
        controller.run();
      } else {
        System.err.println("Invalid mode or missing file for headless mode.");
      }
    } catch (Exception e) {
      System.err.println("Application error: " + e.getMessage());
    }
  }
}