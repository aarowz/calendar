// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package app;

import controller.CalendarController;
import model.CalendarMulti;
import model.DelegatorImpl;
import model.IDelegator;
import view.CalendarView;
import view.IView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileReader;

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
    // check for valid mode argument
    if (args.length < 2 || !args[0].equalsIgnoreCase("--mode")) {
      System.err.println("Usage: --mode [interactive | headless <filename>]");
      return;
    }

    // initialize model
    IDelegator model = new DelegatorImpl(new CalendarMulti());

    // initialize view using System.out with auto flush
    IView view = new CalendarView.Builder()
            .setOutput(new PrintWriter(System.out, true))
            .build();

    try {
      // interactive mode: read from console
      if (args[1].equalsIgnoreCase("interactive")) {
        System.out.println("Calendar application started in interactive mode. Type a command:");
        CalendarController controller =
                new CalendarController(model, view, new InputStreamReader(System.in));
        controller.run();
      }

      // headless mode: read commands from file
      else if (args[1].equalsIgnoreCase("headless") && args.length > 2) {
        System.out.println("Calendar application started in headless mode using file: " + args[2]);
        try (FileReader fileReader = new FileReader(args[2])) {
          CalendarController controller = new CalendarController(model, view, fileReader);
          controller.run();
        } catch (IOException e) {
          System.err.println("Failed to open file: " + e.getMessage());
        }
      }

      // missing file for headless mode
      else {
        System.err.println("Invalid mode or missing file for headless mode.");
      }

    } catch (Exception e) {
      // unexpected error during execution
      System.err.println("Application error: " + e.getMessage());
    }
  }
}