// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package view;

import controller.CommandParser;
import controller.ICommand;
import model.CalendarMulti;
import model.DelegatorImpl;
import model.IDelegator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;

import static org.junit.Assert.assertTrue;

/**
 * Test class for CalendarView functionality in the calendar application.
 * Verifies the output behavior of the view across various display scenarios.
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
   * Verifies that the 'print events on date' command displays correct event details.
   */
  @Test
  public void testPrintCommandDisplaysEventDetails() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    MockView view = new MockView();

    // Set up calendar context
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    // Create an event on a specific date
    String createCmd = "create event Workshop from 2025-06-25T14:00 to 2025-06-25T16:00";
    ICommand create = CommandParser.parse(model, createCmd);
    create.execute(model, view);

    // Query events for that date
    String printCmd = "print events on 2025-06-25";
    ICommand print = CommandParser.parse(model, printCmd);
    print.execute(model, view);

    // Verify output
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