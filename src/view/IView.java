// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package view;

import java.io.IOException;

/**
 * Represents a generic view interface that outputs messages to a user.
 * Implementations should handle formatting and displaying information
 * from the calendar controller.
 */
public interface IView {

  /**
   * Renders the given message to the output target.
   *
   * @param message the message to display
   * @throws IOException if output fails
   */
  void renderMessage(String message) throws IOException;
}