// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package view;

import java.io.IOException;

/**
 * A simple implementation of IView that writes messages to an Appendable output.
 * This class is compatible with ConsoleViewBuilder for flexible construction.
 */
public class ConsoleView implements IView {

  private final Appendable out;

  /**
   * Constructs a ConsoleView with the specified output target.
   *
   * @param out the output destination (e.g., System.out, PrintWriter, etc.)
   */
  public ConsoleView(Appendable out) {
    this.out = out;
  }

  /**
   * Renders a plain text message to the configured output destination.
   *
   * @param message the message to render
   * @throws IOException if writing fails
   */
  @Override
  public void renderMessage(String message) throws IOException {
    out.append(message).append(System.lineSeparator());
  }
}