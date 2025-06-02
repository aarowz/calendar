// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package exceptions;

/**
 * Exception thrown when an attempt is made to add an event or event series
 * that already exists in the calendar.
 */
public class DuplicateEventException extends Exception {

  /**
   * Constructs a DuplicateEventException with a default message.
   */
  public DuplicateEventException() {
    super("An event with the same subject, start, and end time already exists.");
  }

  /**
   * Constructs a DuplicateEventException with a specific message.
   *
   * @param message the detail message
   */
  public DuplicateEventException(String message) {
    super(message);
  }
}