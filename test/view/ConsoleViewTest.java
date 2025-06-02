// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package view;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Unit tests for the ConsoleView class.
 */
public class ConsoleViewTest {

  private StringBuilder output;
  private ConsoleView view;

  @Before
  public void setUp() {
    output = new StringBuilder();
    view = new ConsoleView(output);
  }

  @Test
  public void testRenderSingleMessage() throws IOException {
    view.renderMessage("Hello, calendar!");
    assertEquals("Hello, calendar!", output.toString().trim());
  }

  @Test
  public void testRenderMultipleMessages() throws IOException {
    view.renderMessage("First message.\n");
    view.renderMessage("Second message.\n");
    assertTrue(output.toString().contains("First message."));
    assertTrue(output.toString().contains("Second message."));
  }

  @Test(expected = IOException.class)
  public void testRenderMessageThrowsIOException() throws IOException {
    Appendable brokenOutput = new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        throw new IOException("Broken output");
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException("Broken output");
      }

      @Override
      public Appendable append(char c) throws IOException {
        throw new IOException("Broken output");
      }
    };

    IView brokenView = new ConsoleView(brokenOutput);
    brokenView.renderMessage("This should throw");
  }
}