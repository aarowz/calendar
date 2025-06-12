// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import exceptions.InvalidCommandException;
import model.CalendarMulti;
import model.DelegatorImpl;
import model.IDelegator;
import view.CalendarView;
import view.IView;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.ZoneId;

import static org.junit.Assert.*;

/**
 * Test class for the CommandParser.
 * Verifies correct parsing of valid commands, recognition of syntax rules,
 * and proper rejection of invalid or malformed input.
 */
public class CommandParserTest {

  @Test
  public void testParseCreateEventCommand() throws Exception {
    // Arrange: Set up delegator and calendar
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    // Act: Parse and evaluate command
    String input = "create event Meeting from 2025-06-03T10:00 to 2025-06-03T11:00";
    ICommand cmd = CommandParser.parse(model, input);

    // Assert: Confirm command type and content
    assertTrue("Expected command to be a CreateEventCommand", cmd instanceof
            CreateEventCommand);
    assertTrue("Expected command to contain 'meeting'", cmd.toString().toLowerCase()
            .contains("meeting"));
  }

  @Test
  public void testParseRecurringEventCommand() throws Exception {
    // Arrange: Create and select a calendar
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    // Act: Parse the recurring event creation command
    String input = "create event Standup from 2025-06-02T09:00 to 2025-06-02T09:30 repeats MWF " +
            "for 3 times";
    ICommand cmd = CommandParser.parse(model, input);

    // Assert: Validate command type and content
    assertTrue("Expected a CreateEventCommand", cmd instanceof CreateEventCommand);
    assertTrue("Expected command to include 'standup'", cmd.toString().toLowerCase()
            .contains("standup"));
  }

  @Test(expected = InvalidCommandException.class)
  public void testInvalidCommandThrows() throws InvalidCommandException {
    // Arrange: create a delegator and use a calendar (even though it's not needed for
    // this invalid input)
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    // Act: parse an invalid command
    CommandParser.parse(model, "launch rocket now"); // should throw
  }

  @Test(expected = InvalidCommandException.class)
  public void testRejectInvalidWeekdayCharacters() throws InvalidCommandException {
    // Arrange: Create model and calendar
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    // Act: parse command with invalid weekday 'Z' — should throw
    String input = "create event Practice from 2025-06-02T08:00 to 2025-06-02T09:00 " +
            "repeats MZ for 3 times";
    CommandParser.parse(model, input); // should throw
  }

  @Test(expected = InvalidCommandException.class)
  public void testMissingRequiredFields() throws InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    CommandParser.parse(model, "create event Lunch");
  }

  @Test(expected = InvalidCommandException.class)
  public void testUnknownCommandFails() throws InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    CommandParser.parse(model, "explode event on 2025-06-06");
  }

  @Test(expected = InvalidCommandException.class)
  public void testRejectFromAfterToDates() throws InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event InvalidEvent from 2025-06-10T15:00 to 2025-06-10T14:00";
    CommandParser.parse(model, input);
  }

  @Test
  public void testQuotedArgumentPreserved() throws InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event \"Daily Check In\" from 2025-06-01T08:00 to 2025-06-01T08:15";
    ICommand cmd = CommandParser.parse(model, input);
    assertTrue(cmd instanceof CreateEventCommand);
    assertTrue(cmd.toString().contains("Daily Check In"));
  }

  @Test(expected = InvalidCommandException.class)
  public void testEmptyInputFails() throws InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    CommandParser.parse(model, " ");
  }

  /**
   * Ensures that the program handles an invalid command in a multi-line script,
   * executes valid commands before it, and reports the error meaningfully.
   */
  @Test
  public void testInvalidCommandInScriptIsHandledAndReported() {
    String input = String.join("\n",
            "create calendar --name testcal --timezone America/New_York",
            "use calendar --name testcal",
            "create event Meeting from 2025-06-06T09:00 to 2025-06-06T10:00",
            "explode event on 2025-06-06",
            "exit"
    );

    // Wrap byte input stream with InputStreamReader to satisfy Readable
    ByteArrayInputStream byteInput = new ByteArrayInputStream(input.getBytes());
    Readable inputReader = new java.io.InputStreamReader(byteInput);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(outputStream, true);

    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new CalendarView.Builder()
            .setOutput(writer)
            .build();

    CalendarController controller = new CalendarController(model, view, inputReader);
    controller.run();

    String output = outputStream.toString().toLowerCase();

    assertTrue(output.contains("calendar created: testcal"));
    assertTrue(output.contains("using calendar: testcal"));
    assertTrue(output.contains("event created: meeting"));
    assertTrue(output.contains("error: unknown or malformed command: explode"));
  }
}