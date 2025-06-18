package view;

import java.util.List;

/**
 * Interface for a panel that allows selecting an existing calendar
 * from a list of calendar names.
 * Provides methods to load available calendars and get the user's selection.
 */
public interface ICalendarSelectorPanel {

  /**
   * Loads the given list of calendar names into the dropdown.
   * @param calendarNames the list of calendar names
   */
  void loadCalendars(List<String> calendarNames);

  /**
   * Returns the name of the selected calendar.
   * @return the selected calendar name
   */
  String getSelectedCalendar();
}
