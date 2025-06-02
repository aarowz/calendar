// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package exceptions;

/**
 * Exception thrown when a command fails to execute properly.
 * This may be due to issues such as invalid state, model errors, or user input mismatches.
 */
public class CommandExecutionException extends Exception {

  /**
   * Constructs a CommandExecutionException with no detail message.
   */
  public CommandExecutionException() {
    super("Command failed to execute.");
  }

  /**
   * Constructs a CommandExecutionException with a specific message.
   *
   * @param message the detail message
   */
  public CommandExecutionException(String message) {
    super(message);
  }

  /**
   * Constructs a CommandExecutionException with a specific message and cause.
   *
   * @param message the detail message
   * @param cause   the underlying cause of the exception
   */
  public CommandExecutionException(String message, Throwable cause) {
    super(message, cause);
  }
}