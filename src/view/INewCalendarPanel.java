package view;

/**
 * Interface for a panel that allows creating a new calendar
 * by specifying a name and a timezone.
 * Provides methods to load available timezones, access user input,
 * and reset the form.
 */
public interface INewCalendarPanel {

  /**
   * Gets the timezone dropdown with supported timezones.
   * By default, loads common zones sorted alphabetically.
   */
  void loadTimezones();

  /**
   * Returns the entered calendar name.
   * @return the name entered by the user
   */
  String getCalendarName();

  /**
   * Returns the selected timezone.
   * @return the selected timezone ID
   */
  String getSelectedTimezone();

  /**
   * Clears all input fields.
   */
  void reset();
}
