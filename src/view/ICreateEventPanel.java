package view;

import java.time.LocalDateTime;

/**
 * Interface for a form panel used to collect input for creating a new calendar event.
 * Provides accessors for user input and allows the form to be reset.
 */
public interface ICreateEventPanel {

  /**
   * Clears all fields in the form.
   */
  void resetForm();

  /**
   * Returns the subject entered in the form.
   * @return the event subject
   */
  String getSubject();

  /**
   * Returns the location entered in the form.
   * @return the event location
   */
  String getLocationText();

  /**
   * Returns the description entered in the form.
   * @return the event description
   */
  String getDescription();

  /**
   * Returns the start date and time selected in the form.
   * @return the event start LocalDateTime
   */
  LocalDateTime getStartDateTime();

  /**
   * Returns the end date and time selected in the form.
   * @return the event end LocalDateTime
   */
  LocalDateTime getEndDateTime();

  /**
   * Returns whether the all-day event checkbox is selected.
   * @return true if the event is all-day
   */
  boolean isAllDayEvent();

  /**
   * Returns whether the private event checkbox is selected.
   * @return true if the event is private
   */
  boolean isPrivateEvent();
}
