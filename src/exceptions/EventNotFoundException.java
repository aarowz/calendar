// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package exceptions;

/**
 * Exception thrown when an event cannot be found in the calendar,
 * typically during edit or delete operations.
 */
public class EventNotFoundException extends Exception {

  /**
   * Constructs an EventNotFoundException with a default message.
   */
  public EventNotFoundException() {
    super("The specified event could not be found.");
  }

  /**
   * Constructs an EventNotFoundException with a specific message.
   *
   * @param message the detail message
   */
  public EventNotFoundException(String message) {
    super(message);
  }
}