// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package app;

import controller.CalendarGuiController;
import model.CalendarMulti;
import model.DelegatorImpl;
import model.IDelegator;
import view.CalendarGuiView;

/**
 * Entry point to launch the calendar in GUI mode or dispatch CLI modes.
 */
public class CalendarGuiApp {

  /**
   * Launches the calendar application in GUI mode by default.
   * Supports command-line override via:
   * --mode headless path/to/script.txt
   * --mode interactive (optional)
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    if (args.length > 1 && args[0].equals("--mode")) {
      if (args[1].equals("headless") || args[1].equals("interactive")) {
        CalendarApp.main(args);
        return;
      }
    }

    // Default: launch GUI
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    CalendarGuiView view = new CalendarGuiView();
    new CalendarGuiController(model, view);
  }
}