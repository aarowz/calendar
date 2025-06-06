// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package exceptions;

/**
 * Exception thrown when the input command is malformed or unrecognized by the parser.
 */
public class InvalidCommandException extends Exception {
  /**
   * Constructs an InvalidCommandException with a specific message.
   *
   * @param message the detail message
   */
  public InvalidCommandException(String message) {
    super(message);
  }
}