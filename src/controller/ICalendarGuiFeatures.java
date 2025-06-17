// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package controller;

/**
 * Represents the set of interactive features supported by the calendar GUI controller.
 * These methods are triggered by user actions from the view (e.g., button clicks).
 */
public interface ICalendarGuiFeatures {

  /**
   * Handles a request to view the schedule from a selected date.
   */
  void handleViewSchedule();

  /**
   * Handles a request to open the event creation form.
   */
  void handleAddEvent();

  /**
   * Handles a request to edit an existing event.
   */
  void handleEditEvent();

  /**
   * Handles a request to switch the active calendar.
   */
  void handleSwitchCalendar();

  /**
   * Handles a request to switch the active calendar.
   */
  void handleCreateCalendar();

  /**
   * Refreshes the schedule of the GUI.
   */
  void refreshSchedule();
}