package view;

import controller.ICalendarGuiFeatures;
import model.ROIEvent;
import java.util.List;

/**
 * Interface for the graphical user interface of the calendar application.
 * Provides methods for displaying events, showing messages,
 * updating UI text, and connecting to the controller.
 */
public interface ICalendarGuiView {

  /**
   * Attaches controller features to all interactive components in the view.
   * @param features the controller features
   */
  void setFeatures(ICalendarGuiFeatures features);

  /**
   * Displays the given list of events in the scrollable event panel.
   * @param events the events to display
   */
  void displayEvents(List<ROIEvent> events);

  /**
   * Displays an error message dialog.
   *
   * @param message the error message to show
   */
  void showError(String message);

  /**
   * Updates the header text in the event view panel.
   * @param text the message to display above the event list
   */
  void setHeaderText(String text);

  /**
   * Gets the button panel within the view.
   * @return the button panel
   */
  ButtonPanel getButtonPanel();
}
