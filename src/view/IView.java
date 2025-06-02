// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package view;

/**
 * Represents a generic view for rendering output messages in a calendar application.
 * This interface allows for flexibility between console, GUI, and test views.
 */
public interface IView {

  /**
   * Renders the given message to the user.
   *
   * @param message the message to display
   */
  void render(String message);
}