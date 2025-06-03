// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package view;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * A builder for constructing {@link ConsoleView} instances.
 * This builder allows for flexible configuration of output destinations.
 */
public class ConsoleViewBuilder {
  private Appendable output = new OutputStreamWriter(System.out); // default output

  /**
   * Sets a custom output destination for the view.
   *
   * @param output the Appendable to use (e.g., PrintWriter, FileWriter)
   * @return this builder
   */
  public ConsoleViewBuilder setOutput(Appendable output) {
    this.output = output;
    return this;
  }

  /**
   * Builds the {@link ConsoleView} instance using the current configuration.
   *
   * @return a new ConsoleView
   */
  public IView build() {
    return new ConsoleView(output);
  }
}