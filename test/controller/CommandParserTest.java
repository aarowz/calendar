// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import exceptions.InvalidCommandException;
import model.CalendarModel;
import model.ICalendar;
import view.CalendarView;
import view.IView;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the CommandParser.
 * This class includes tests to verify correct parsing of valid commands,
 * handling of invalid input formats, recognition of known commands, and
 * validation of required arguments and edge cases.
 */
public class CommandParserTest {

  /**
   * Verifies that a well-formed 'create-event' command is parsed successfully.
   */
  @Test
  public void testParseCreateEventCommand() throws InvalidCommandException {
    String input = "create event Meeting from 2025-06-03T10:00 to 2025-06-03T11:00";
    ICommand cmd = CommandParser.parse(input);
    assertTrue(cmd instanceof CreateEventCommand);
    assertTrue(cmd.toString().toLowerCase().contains("meeting"));
  }

  /**
   * Ensures that a recurring event creation command with repeat parameters is parsed as expected.
   */
  @Test
  public void testParseRecurringEventCommand() throws InvalidCommandException {
    String input = "create event Standup from 2025-06-02T09:00 to 2025-06-02T09:30 " +
            "repeats MWF for 3 times";
    ICommand cmd = CommandParser.parse(input);
    assertTrue(cmd instanceof CreateEventCommand);
    assertTrue(cmd.toString().toLowerCase().contains("standup"));
  }

  /**
   * Verifies that invalid commands result in an appropriate exception.
   */
  @Test(expected = InvalidCommandException.class)
  public void testInvalidCommandThrows() throws InvalidCommandException {
    CommandParser.parse("launch rocket now");
  }

  /**
   * Verifies that a command with an invalid weekday character (e.g. 'Z') is rejected.
   */
  @Test(expected = InvalidCommandException.class)
  public void testRejectInvalidWeekdayCharacters() throws InvalidCommandException {
    String input = "created event Practice from 2025-06-02T08:00 to 2025-06-02T09:00 " +
            "repeat MZ for 3 times"; // MZ is not valid since Z is not a weekday character
    ICommand command = CommandParser.parse(input);
  }

  /**
   * Ensures the parser reports an error when required fields are missing.
   */
  @Test(expected = InvalidCommandException.class)
  public void testMissingRequiredFields() throws InvalidCommandException {
    CommandParser.parse("created event onlysubject");
  }

  /**
   * Confirms that unknown commands are rejected properly.
   */
  @Test(expected = InvalidCommandException.class)
  public void testUnknownCommandFails() throws InvalidCommandException {
    CommandParser.parse("explode event on 2025-06-06");
  }

  /**
   * Verifies that a command is rejected if the 'from' time is after the 'to' time.
   */
  @Test(expected = InvalidCommandException.class)
  public void testRejectFromAfterToDates() throws InvalidCommandException {
    // This should fail once validation is added to your parser
    String input = "created event InvalidEvent from 2025-06-10T15:00 to 2025-06-10T14:00";
    ICommand command = CommandParser.parse(input); // should throw
  }

  /**
   * Verifies that quoted strings are correctly preserved during parsing.
   */
  @Test
  public void testQuotedArgumentPreserved() throws InvalidCommandException {
    String input = "create event \"Daily Check In\" from 2025-06-01T08:00 to 2025-06-01T08:15";
    ICommand cmd = CommandParser.parse(input);
    assertTrue(cmd instanceof CreateEventCommand);
    assertTrue(cmd.toString().contains("Daily Check In"));
  }

  /**
   * Ensures that empty or null inputs result in a parsing error.
   */
  @Test(expected = InvalidCommandException.class)
  public void testEmptyInputFails() throws InvalidCommandException {
    CommandParser.parse(" ");
  }

  /**
   * Ensures that the program stops running and identifies the error that caused it to stop.
   */
  @Test
  public void testInvalidCommandInScriptIsHandledAndReported() {
    String input = String.join("\n",
            "create event Meeting from 2025-06-06T09:00 to 2025-06-06T10:00",
            "explode event on 2025-06-06",
            "exit"
    );

    // set up printing
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(outputStream, true);

    // create new model and view instances
    ICalendar model = new CalendarModel();
    IView view = new CalendarView.Builder()
            .setOutput(writer)
            .build();

    // run the program
    CalendarController controller = new CalendarController(model, view, inputStream);
    controller.run();

    String output = outputStream.toString();

    // check that the invalid command and a meaningful error message is printed
    assertEquals(output, "Event created: Meeting\n" +
            "\n" +
            "Error: unknown or malformed command: explode\n");
  }
}