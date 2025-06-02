// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package exceptions;

/**
 * Thrown when a command fails during its execution on the model.
 * Wraps underlying logic errors (e.g., invalid state, parsing issues, or model constraints)
 * and delivers them back to the controller to display a meaningful message to the user.
 */
public class CommandExecutionException extends Throwable {
}
