// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package view;

import java.io.IOException;
import java.util.Objects;

/**
 * CalendarView is a text-based implementation of IView that renders messages
 * to an Appendable (e.g., System out). It uses a Builder to allow flexible
 * configuration of the view.
 */
public class CalendarView implements IView {
  private final Appendable output;

  /**
   * Private constructor for the view.
   *
   * @param output the output view
   */
  private CalendarView(Appendable output) {
    this.output = output;
  }

  @Override
  public void renderMessage(String message) throws IOException {
    output.append(message).append(System.lineSeparator());
    if (output instanceof java.io.Flushable) {
      ((java.io.Flushable) output).flush();
    }
  }

  /**
   * Builder class for CalendarView that allows optional configuration.
   */
  public static class Builder {
    private Appendable output = System.out;

    /**
     * Set a custom output destination.
     *
     * @param output the output appendable
     * @return this builder
     */
    public Builder setOutput(Appendable output) {
      this.output = Objects.requireNonNull(output);
      return this;
    }

    /**
     * Build and return the configured CalendarView.
     *
     * @return the CalendarView instance
     */
    public CalendarView build() {
      return new CalendarView(output);
    }
  }
}