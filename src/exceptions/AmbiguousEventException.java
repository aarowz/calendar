// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package exceptions;

/**
 * Exception thrown when a user's input matches more than one event,
 * making it ambiguous to determine which event should be operated on.
 */
public class AmbiguousEventException extends Exception {

  /**
   * Constructs an AmbiguousEventException with no detail message.
   */
  public AmbiguousEventException() {
    super("Multiple events matched the input. Please clarify.");
  }

  /**
   * Constructs an AmbiguousEventException with a specific message.
   *
   * @param message the detail message
   */
  public AmbiguousEventException(String message) {
    super(message);
  }
}