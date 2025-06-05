// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

/**
 * Enum representing editable properties of an event.
 * Used in edit commands to specify which field should be modified (e.g., subject, time, location).
 */
public enum EventProperty {
  SUBJECT,      // the event's title
  START,        // the start time
  END,          // the end time
  DESCRIPTION,  // the event's description
  LOCATION,     // the event's location
  STATUS        // the event's public/private status
}