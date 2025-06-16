// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package app;

import controller.CalendarGuiController;
import model.CalendarMulti;
import model.DelegatorImpl;
import model.IDelegator;
import util.DefaultCalendarInitializerUtil;
import view.CalendarGuiView;

/**
 * Entry point to launch the calendar in GUI mode.
 */
public class CalendarGuiApp {

  /**
   * Launches the GUI version of the calendar app.
   *
   * @param args command-line arguments (unused)
   */
  public static void main(String[] args) {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    String defaultCalendar = DefaultCalendarInitializerUtil.createDefaultCalendar(model);

    CalendarGuiView view = new CalendarGuiView();
    new CalendarGuiController(model, view);
  }
}