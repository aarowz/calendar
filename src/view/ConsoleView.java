// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package view;

import java.io.PrintStream;

/**
 * A simple console-based implementation of the IView interface.
 * Renders messages to the user via a PrintStream (e.g., System.out).
 */
public class ConsoleView implements IView {

  private final PrintStream out;

  /**
   * Constructs a ConsoleView that writes to the given PrintStream.
   *
   * @param out the output stream to write messages to
   */
  public ConsoleView(PrintStream out) {
    this.out = out;
  }

  /**
   * Renders a message to the output stream.
   *
   * @param message the message to display
   */
  @Override
  public void render(String message) {
    // TODO: write the message to the output stream
  }
}