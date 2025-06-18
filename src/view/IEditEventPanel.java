package view;

import model.IEvent;
import model.ROIEvent;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for a form panel for selecting and editing an existing event.
 * Provides methods for loading events, accessing user input,
 * and resetting the form.
 */
public interface IEditEventPanel {

  /**
   * Populates the event selector with a list of events.
   *
   * @param events the list of events to edit
   */
  void loadEvents(List<ROIEvent> events);

  /**
   * Clears the form and selection.
   */
  void resetForm();

  /**
   * Returns the selected event to edit.
   *
   * @return the selected event
   */
  IEvent getSelectedEvent();

  /**
   * Returns the updated subject entered in the form.
   *
   * @return the new subject
   */
  String getSubject();

  /**
   * Returns the updated location entered in the form.
   *
   * @return the new location
   */
  String getLocationText();

  /**
   * Returns the updated description entered in the form.
   *
   * @return the new description
   */
  String getDescription();

  /**
   * Returns the new start date and time selected.
   *
   * @return the new start LocalDateTime
   */
  LocalDateTime getStartDateTime();

  /**
   * Returns the new end date and time selected.
   *
   * @return the new end LocalDateTime
   */
  LocalDateTime getEndDateTime();

  /**
   * Returns whether the all-day event checkbox is selected.
   *
   * @return true if the event is all-day
   */
  boolean isAllDayEvent();

  /**
   * Returns whether the private event checkbox is selected.
   *
   * @return true if the event is private
   */
  boolean isPrivateEvent();
}
