package view;

import model.ROIEvent;
import java.util.List;

/**
 * Interface for a panel that displays a scrollable list of calendar events.
 * Provides methods to display events and update the header text.
 */
public interface IScrollEventsPanel {

  /**
   * Updates the displayed text to show the given list of events.
   * @param events the list of events to display
   */
  void displayEvents(List<ROIEvent> events);

  /**
   * Sets the header for displaying events based on the current start day.
   * @param text the current start day
   */
  void setHeaderText(String text);
}
