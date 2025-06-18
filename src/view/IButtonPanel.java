package view;

import controller.ICalendarGuiFeatures;

/**
 * Interface for the panel that displays control buttons
 * for viewing, adding, editing events, and switching calendars.
 * Provides methods to attach controller features and query user preferences.
 */
public interface IButtonPanel {

  /**
   * Attaches the given feature callbacks to each button.
   * @param features the controller features to attach
   */
  void setFeatures(ICalendarGuiFeatures features);

  /**
   * Returns whether the user wants to display private events.
   * @return true if private events should be shown
   */
  boolean shouldDisplayPrivateEvents();
}
