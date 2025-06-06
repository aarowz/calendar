// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package view;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import controller.CommandParser;
import controller.ICommand;
import model.CalendarModel;
import model.ICalendar;

import static org.junit.Assert.assertTrue;

/**
 * Stub test class for testing view functionality in the calendar application.
 * Each method includes a Javadoc describing its purpose according to spec.
 */
public class CalendarViewTest {
  private StringBuilder log;
  private CalendarView view;

  /**
   * Sets up a CalendarView backed by a StringBuilder before each test.
   */
  @Before
  public void setup() {
    log = new StringBuilder();
    view = new CalendarView.Builder().setOutput(log).build();
  }

  /**
   * A mock view that captures output via renderMessage().
   */
  private static class MockView implements IView {
    private final StringWriter output = new StringWriter();
    private final PrintWriter writer = new PrintWriter(output, true);

    @Override
    public void renderMessage(String message) throws IOException {
      writer.println(message);
    }

    public String getOutput() {
      return output.toString();
    }
  }

  /**
   * Verifies that the 'print events on date command displays correct event details.
   */
  @Test
  public void testPrintCommandDisplaysEventDetails() throws Exception {
    ICalendar model = new CalendarModel();
    MockView view = new MockView();

    // create an event on a specific date
    String createCmd = "create event Workshop from 2025-06-25T14:00 to 2025-06-25T16:00";
    ICommand create = CommandParser.parse(createCmd);
    assert create != null;
    create.execute(model, view);

    // now query for events on that date
    String printCmd = "print events on 2025-06-25";
    ICommand print = CommandParser.parse(printCmd);
    assert print != null;
    print.execute(model, view);

    // verify output
    String output = view.getOutput().toLowerCase();
    assertTrue(output.contains("workshop"));
    assertTrue(output.contains("14:00"));
    assertTrue(output.contains("16:00"));
  }

  /**
   * Tests that the view properly displays prompts for expected date formats.
   */
  @Test
  public void testDisplaysDateFormatPrompt() throws IOException {
    view.renderMessage("Please use format YYYY-MM-DDThh:mm");
    assertTrue(log.toString().contains("YYYY-MM-DDThh:mm"));
  }

  /**
   * Tests that the view prints events for a specific date clearly.
   */
  @Test
  public void testDisplaysEventsForDate() throws IOException {
    view.renderMessage("Events on 2025-06-06:\n- Meeting at 10:00");
    assertTrue(log.toString().contains("Events on 2025-06-06"));
    assertTrue(log.toString().contains("Meeting"));
  }

  /**
   * Tests that the view handles invalid event input and shows an error.
   */
  @Test
  public void testDisplaysInvalidInputMessage() throws IOException {
    view.renderMessage("Error: Invalid command syntax");
    assertTrue(log.toString().toLowerCase().contains("error"));
  }

  /**
   * Tests that the view indicates busy/available status correctly.
   */
  @Test
  public void testDisplaysBusyStatus() throws IOException {
    view.renderMessage("Status: You are busy at this time.");
    assertTrue(log.toString().contains("busy"));
  }

  /**
   * Tests that the view gracefully displays exit confirmation.
   */
  @Test
  public void testDisplaysExitMessage() throws IOException {
    view.renderMessage("Goodbye!");
    assertTrue(log.toString().contains("Goodbye"));
  }

  /**
   * Tests that the view outputs messages in response to controller commands.
   */
  @Test
  public void testRendersControllerFeedback() throws IOException {
    view.renderMessage("Event created: Standup");
    assertTrue(log.toString().contains("Standup"));
  }

  /**
   * Tests view behavior when no events exist for a queried day.
   */
  @Test
  public void testNoEventsMessage() throws IOException {
    view.renderMessage("No events found on this date.");
    assertTrue(log.toString().toLowerCase().contains("no events"));
  }
}