// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import exceptions.InvalidCommandException;
import model.EventStatus;
import model.ROIEvent;
import model.IDelegator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses raw input strings into ICommand objects.
 * This class is responsible only for interpreting user text into structured commands.
 */
public class CommandParser {

  // ===========
  // Main parser
  // ===========

  /**
   * Parses user input into an ICommand to be executed on the model.
   *
   * @param model the calendar model delegator to apply the command on
   * @param input the raw string user input
   * @return the parsed ICommand object
   * @throws InvalidCommandException if the input is malformed or unrecognized
   */
  public static ICommand parse(IDelegator model, String input) throws InvalidCommandException {
    // break the raw input string into individual tokens (words)
    List<String> tokens = tokenize(input);

    // if there are no tokens, the input was empty or whitespace
    if (tokens.isEmpty()) {
      throw new InvalidCommandException("empty input");
    }

    // get the first word to determine the command type
    String commandType = tokens.get(0).toLowerCase();

    // dispatch to the appropriate command parser based on the command type
    switch (commandType) {
      case "create":
        return parseCreate(tokens);  // handles "create calendar" or "create event"
      case "edit":
        return parseEdit(model, tokens);  // handles all edit commands
      case "print":
        return parsePrint(tokens);  // handles "print events on/from"
      case "show":
        return parseShow(tokens);  // handles "show status on"
      case "exit":
        return new ExitCommand();  // terminates the app
      case "use":
        return parseUse(tokens);  // handles "use calendar --name <name>"
      case "copy":
        return parseCopy(tokens);  // handles all copy commands (event/events)
      default:
        // if the command is unknown, throw an error
        throw new InvalidCommandException("unknown or malformed command: " + tokens.get(0));
    }
  }

  /**
   * Parses a create command for either a calendar or an event.
   *
   * @param tokens the tokenized user input
   * @return the corresponding ICommand object
   * @throws InvalidCommandException if the format is invalid
   */
  private static ICommand parseCreate(List<String> tokens) throws InvalidCommandException {
    // check for 'create calendar --name <name> --timezone <zone>'
    if (tokens.size() >= 6 &&
            tokens.get(1).equalsIgnoreCase("calendar") &&
            tokens.get(2).equalsIgnoreCase("--name") &&
            tokens.get(4).equalsIgnoreCase("--timezone")) {
      String name = tokens.get(3);
      String zone = tokens.get(5);
      return new CreateCalendarCommand(name, zone);
    }

    // if not a calendar, it must be 'create event'
    if (tokens.size() < 2 || !tokens.get(1).equalsIgnoreCase("event")) {
      throw new InvalidCommandException("Expected 'create event' or 'create calendar' command.");
    }

    // delegate to event creation parser
    return parseCreateCommand(tokens);
  }

  /**
   * Parses edit-related commands, including calendar, event(s), and series edits.
   */
  private static ICommand parseEdit(IDelegator model, List<String> tokens) throws
          InvalidCommandException {
    // check for minimum tokens for any edit command
    if (tokens.size() < 2) {
      throw new InvalidCommandException("incomplete edit command");
    }

    // handle 'edit calendar --name <name> --property <prop> <value>'
    if (tokens.get(1).equalsIgnoreCase("calendar")) {
      if (tokens.size() >= 6 &&
              tokens.get(2).equalsIgnoreCase("--name") &&
              tokens.get(4).equalsIgnoreCase("--property")) {
        String name = tokens.get(3);
        String property = tokens.get(5);
        String newValue = String.join(" ", tokens.subList(6, tokens.size()))
                .replaceAll("^\"|\"$", "");
        return new EditCalendarCommand(name, property, newValue);
      } else {
        throw new InvalidCommandException("Expected format: edit calendar --name <name> " +
                "--property <prop> <value>");
      }
    }

    // dispatch based on subtype: event, events, or series
    switch (tokens.get(1).toLowerCase()) {
      case "event":
        return parseEditCommand(model, tokens);
      case "events":
        return parseEditsCommand(model, tokens);
      case "series":
        return parseEditSeries(model, tokens);
      default:
        throw new InvalidCommandException("unknown edit command subtype: " + tokens.get(1));
    }
  }

  /**
   * Parses "print events on" and "print events from ... to ...".
   */
  private static ICommand parsePrint(List<String> tokens) throws InvalidCommandException {
    // handle 'print events on <date>'
    if (tokens.size() >= 4 && tokens.get(1).equals("events") && tokens.get(2).equals("on")) {
      LocalDate date = LocalDate.parse(tokens.get(3));
      return new QueryEventsCommand(date);
    }
    // handle 'print events from <start> to <end>'
    else if (tokens.size() >= 6 && tokens.get(1).equals("events") && tokens.get(2).equals("from")) {
      LocalDateTime start = LocalDateTime.parse(tokens.get(3).trim());
      LocalDateTime end = LocalDateTime.parse(tokens.get(5).trim());
      return new QueryEventsCommand(start, end);
    }

    // throw error for unsupported print format
    throw new InvalidCommandException("unknown or malformed print command");
  }

  /**
   * Parses show status on datetime command.
   */
  private static ICommand parseShow(List<String> tokens) throws InvalidCommandException {
    if (tokens.size() >= 4 && tokens.get(1).equals("status") && tokens.get(2).equals("on")) {
      LocalDateTime time = LocalDateTime.parse(tokens.get(3).trim());
      return new QueryEventsCommand(time, time);
    }
    throw new InvalidCommandException("unknown or malformed show command");
  }

  /**
   * Parses use calendar --name with input name command.
   */
  private static ICommand parseUse(List<String> tokens) throws InvalidCommandException {
    if (tokens.size() >= 4 &&
            tokens.get(1).equalsIgnoreCase("calendar") &&
            tokens.get(2).equalsIgnoreCase("--name")) {
      String name = tokens.get(3);
      return new UseCalendarCommand(name);
    }
    throw new InvalidCommandException("Expected format: use calendar --name <name>");
  }

  /**
   * Parses all variants of the "copy" command.
   *
   * @param tokens the tokenized user input
   * @return the appropriate ICommand representing the copy action
   * @throws InvalidCommandException if the command format is invalid
   */
  private static ICommand parseCopy(List<String> tokens) throws InvalidCommandException {
    // handle 'copy event <name> on <start> --target <calendar> to <targetStart>'
    if (tokens.size() >= 8 && tokens.get(1).equalsIgnoreCase("event")) {
      return parseCopySingleEvent(tokens);
    }

    // handle 'copy events on <date> --target <calendar> to <targetDate>'
    if (tokens.size() >= 8 &&
            tokens.get(1).equalsIgnoreCase("events") &&
            tokens.get(2).equalsIgnoreCase("on") &&
            tokens.get(4).equalsIgnoreCase("--target") &&
            tokens.get(6).equalsIgnoreCase("to")) {
      return parseCopyEventsOn(tokens);
    }

    // handle 'copy events between <startDate> and <endDate> --target <calendar> to <targetDate>'
    if (tokens.size() >= 10 &&
            tokens.get(1).equalsIgnoreCase("events") &&
            tokens.get(2).equalsIgnoreCase("between") &&
            tokens.get(4).equalsIgnoreCase("and") &&
            tokens.get(6).equalsIgnoreCase("--target") &&
            tokens.get(8).equalsIgnoreCase("to")) {
      return parseCopyEventsBetween(tokens);
    }

    // reject unrecognized copy command formats
    throw new InvalidCommandException("unknown or malformed copy command");
  }

  /**
   * Parses a "copy event name on start --target calendar to targetStart" command.
   *
   * @param tokens the tokenized user input
   * @return a CopyEventCommand with extracted values
   * @throws InvalidCommandException if the command format is invalid
   */
  private static ICommand parseCopySingleEvent(List<String> tokens) throws
          InvalidCommandException {
    // locate keyword positions
    int onIndex = tokens.indexOf("on");
    int targetIndex = tokens.indexOf("--target");
    int toIndex = tokens.indexOf("to");

    // validate structure of the copy event command
    if (onIndex < 2 || targetIndex < onIndex || toIndex < targetIndex) {
      throw new InvalidCommandException("Expected format: copy event <name> on <start> " +
              "--target <calendar> to <targetStart>");
    }

    // extract event name
    String eventName = String.join(" ", tokens.subList(2, onIndex))
            .replaceAll("^\"|\"$", "").trim();

    // parse datetime and calendar fields
    LocalDateTime sourceStart = LocalDateTime.parse(tokens.get(onIndex + 1).trim());
    String targetCalendar = tokens.get(targetIndex + 1).trim();
    LocalDateTime targetStart = LocalDateTime.parse(tokens.get(toIndex + 1).trim());

    // return the parsed copy command
    return new CopyEventCommand(eventName, sourceStart, targetCalendar, targetStart);
  }

  /**
   * Parses "copy events on date --target calendar to date".
   */
  private static ICommand parseCopyEventsOn(List<String> tokens) {
    LocalDate sourceDate = LocalDate.parse(tokens.get(3));
    String targetCalendar = tokens.get(5);
    LocalDate targetDate = LocalDate.parse(tokens.get(7));

    return new CopyEventsOnCommand(sourceDate, targetCalendar, targetDate);
  }

  /**
   * Parses "copy events between start and end --target calendar to target".
   */
  private static ICommand parseCopyEventsBetween(List<String> tokens) {
    LocalDate startDate = LocalDate.parse(tokens.get(3));
    LocalDate endDate = LocalDate.parse(tokens.get(5));
    String targetCalendar = tokens.get(7);
    LocalDate targetStart = LocalDate.parse(tokens.get(9));

    return new CopyEventsBetweenCommand(startDate, endDate, targetCalendar, targetStart);
  }

  // ===============================
  // Create event parser and helpers
  // ===============================

  /**
   * Parses a request to create an event command from tokens.
   *
   * @param tokens the input tokens
   * @return a CreateEventCommand representing the parsed event
   * @throws InvalidCommandException if the command is malformed
   */
  private static ICommand parseCreateCommand(List<String> tokens) throws InvalidCommandException {
    try {
      for (int i = 2; i < tokens.size(); i++) {
        if (tokens.get(i).equalsIgnoreCase("on")) {
          return parseAllDayEvent(tokens, i);
        }
      }
      return parseTimedEvent(tokens);
    } catch (DateTimeParseException e) {
      throw new InvalidCommandException("invalid date/time format: " + e.getMessage());
    }
  }

  /**
   * Parses an all-day event command of the particular given form.
   * Create event subject on date [repeats days for n | until date].
   */
  private static ICommand parseAllDayEvent(List<String> tokens, int onIndex) {
    String subject = extractSubject(tokens, onIndex);
    LocalDate date = LocalDate.parse(tokens.get(onIndex + 1));
    LocalDateTime start = date.atTime(8, 0);
    LocalDateTime end = date.atTime(17, 0);

    RepeatMetadata meta = extractRepeatMetadata(tokens);
    return new CreateEventCommand(subject, start, end, "", null,
            EventStatus.PUBLIC, meta.days, meta.count, meta.until);
  }

  /**
   * Parses a timed event command of the given form.
   * Create event subject from start to end [repeats days for n | until date].
   */

  private static ICommand parseTimedEvent(List<String> tokens) throws InvalidCommandException {
    int fromIndex = tokens.indexOf("from");
    if (fromIndex == -1 || !tokens.get(fromIndex + 2).equalsIgnoreCase("to")) {
      throw new InvalidCommandException("expected format: create event <subject> from <start> " +
              "to <end>");
    }

    String subject = extractSubject(tokens, fromIndex);
    LocalDateTime start = LocalDateTime.parse(tokens.get(fromIndex + 1).trim());
    LocalDateTime end = LocalDateTime.parse(tokens.get(fromIndex + 3).trim());

    // Default values
    String location = "";
    String description = "";
    EventStatus status = EventStatus.PUBLIC;

    RepeatMetadata meta = extractRepeatMetadata(tokens);

    // Extract location if "at" is present
    int atIndex = tokens.indexOf("at");
    if (atIndex != -1 && atIndex > fromIndex + 3) {
      location = String.join(" ", tokens.subList(atIndex + 1, tokens.size()));
    }

    return new CreateEventCommand(subject, start, end, description, location, status, meta.days,
            meta.count, meta.until);
  }

  /**
   * Extracts the subject string from a range of tokens.
   */
  private static String extractSubject(List<String> tokens, int end) {
    return String.join(" ", tokens.subList(2, end))
            .replaceAll("^\"|\"$", "");
  }

  /**
   * Extracts recurrence metadata such as days, count, or end date.
   */
  private static RepeatMetadata extractRepeatMetadata(List<String> tokens) {
    List<Character> repeatDays = null;
    Integer repeatCount = null;
    LocalDate repeatUntil = null;

    if (tokens.contains("repeats")) {
      int repeatsIndex = tokens.indexOf("repeats");
      String dayString = tokens.get(repeatsIndex + 1);
      repeatDays = toCharList(dayString);

      for (char c : repeatDays) {
        if ("MTWRFSU".indexOf(c) == -1) {
          throw new IllegalArgumentException("Invalid weekday character: " + c);
        }
      }

      if (tokens.contains("for")) {
        repeatCount = Integer.parseInt(tokens.get(tokens.indexOf("for") + 1));
      } else if (tokens.contains("until")) {
        repeatUntil = LocalDate.parse(tokens.get(tokens.indexOf("until") + 1));
      }
    }

    return new RepeatMetadata(repeatDays, repeatCount, repeatUntil);
  }

  /**
   * Holds recurrence parameters for an event.
   */
  private static class RepeatMetadata {
    List<Character> days;
    Integer count;
    LocalDate until;

    RepeatMetadata(List<Character> days, Integer count, LocalDate until) {
      this.days = days;
      this.count = count;
      this.until = until;
    }
  }

  /**
   * Converts the given String to a list of characters.
   *
   * @param s the given string
   * @return the character list
   */
  private static List<Character> toCharList(String s) {
    // convert the given string to a list of characters
    List<Character> result = new ArrayList<>();
    for (char c : s.toCharArray()) {
      result.add(c);
    }
    return result;
  }

  // =============================
  // Edit event parser and helpers
  // =============================

  /**
   * Parses an edit command that modifies a specific event's property.
   *
   * @param model  the calendar model delegator
   * @param tokens the input tokens of the command
   * @return an EditEventCommand representing the update
   * @throws InvalidCommandException if the format is invalid
   */
  private static ICommand parseEditCommand(IDelegator model, List<String> tokens)
          throws InvalidCommandException {
    // check for required structure: at least 9 tokens and must include from, to, and with
    if (tokens.size() < 9 || !tokens.contains("from") || !tokens.contains("to")
            || !tokens.contains("with")) {
      throw new InvalidCommandException("Expected format: edit event <property> <subject> " +
              "from <start> to <end> with <newValue>");
    }

    try {
      // locate key token indices
      int fromIndex = tokens.indexOf("from");
      int withIndex = tokens.indexOf("with");

      // extract command parts
      String property = tokens.get(2).toLowerCase();
      String subject = String.join(" ", tokens.subList(3, fromIndex))
              .replaceAll("^\"|\"$", "");
      LocalDateTime originalStart = LocalDateTime.parse(tokens.get(fromIndex + 1).trim());
      String newValue = String.join(" ", tokens.subList(withIndex + 1, tokens.size()))
              .replaceAll("^\"|\"$", "");

      // delegate to command constructor
      return constructEditedEvent(model, property, subject, originalStart, newValue);

    } catch (Exception e) {
      // catch all parsing issues and wrap in InvalidCommandException
      throw new InvalidCommandException("invalid format in edit event: " + e.getMessage());
    }
  }

  /**
   * Constructs the EditEventCommand by applying only the specified property update.
   *
   * @param model         the calendar delegator
   * @param property      the property to update
   * @param subject       the original subject of the event
   * @param originalStart the original start time
   * @param newValue      the new value to apply
   * @return a command to edit the event
   * @throws InvalidCommandException if the property is unknown or value format is bad
   */
  private static ICommand constructEditedEvent(IDelegator model, String property, String subject,
                                               LocalDateTime originalStart, String newValue)
          throws InvalidCommandException {
    // look up the original event to get current values
    ROIEvent event = getRoiEvent(model, subject, originalStart);

    // initialize fields with existing values
    String newSubject = event.getSubject();
    LocalDateTime newStart = event.getStart();
    LocalDateTime newEnd = event.getEnd();
    String newDescription = event.getDescription();
    String newLocation = event.getLocation();
    String newStatus = event.getStatus().toString();

    // apply the update to the specified property
    switch (property) {
      case "subject":
        newSubject = newValue;
        break;
      case "start":
        newStart = LocalDateTime.parse(newValue.trim());
        break;
      case "end":
        newEnd = LocalDateTime.parse(newValue.trim());
        break;
      case "description":
        newDescription = newValue;
        break;
      case "location":
        newLocation = newValue;
        break;
      case "status":
        newStatus = newValue;
        break;
      default:
        throw new InvalidCommandException("Unknown property: " + property);
    }

    // return the fully constructed edit command
    return new EditEventCommand(subject, originalStart, newSubject, newStart, newEnd,
            newDescription, newLocation, newStatus);
  }

  private static ROIEvent getRoiEvent(IDelegator model, String subject, LocalDateTime start)
          throws InvalidCommandException {
    ROIEvent eventToEdit = null;
    // for each event
    for (ROIEvent e : model.getCurrentCalendar().getEventsOn(start.toLocalDate())) {
      // if the event contains a start and subject
      if (e.getSubject().equals(subject) &&
              e.getStart().withNano(0).equals(start.withNano(0))) {

        // store the event
        eventToEdit = e;
        break;
      }
    }

    // if no events exist
    if (eventToEdit == null) {
      // throw exception
      throw new InvalidCommandException("Event to edit not found.");
    }
    return eventToEdit;
  }

  // =======================================
  // Edit multiple events parser and helpers
  // =======================================

  /**
   * Parses a command to edit all events in a series starting from a given event.
   *
   * @param model  the calendar model delegator
   * @param tokens the list of command tokens
   * @return an EditEventsCommand representing the batch edit
   * @throws InvalidCommandException if the command format is invalid
   */
  private static ICommand parseEditsCommand(IDelegator model, List<String> tokens) throws
          InvalidCommandException {
    validateEditSeriesTokens(tokens);

    int fromIndex = tokens.indexOf("from");
    int withIndex = tokens.indexOf("with");

    String property = tokens.get(2).toLowerCase();
    String subject = extractQuoted(tokens.subList(3, fromIndex));
    LocalDateTime start = LocalDateTime.parse(tokens.get(fromIndex + 1).trim());
    String newValue = extractQuoted(tokens.subList(withIndex + 1, tokens.size()));

    return buildEditEventsCommand(model, property, subject, start, newValue);
  }

  /**
   * Validates the token structure of an edit events/series command.
   *
   * @param tokens the full token list
   * @throws InvalidCommandException if format is malformed
   */
  private static void validateEditSeriesTokens(List<String> tokens) throws
          InvalidCommandException {
    if (tokens.size() < 7 || !tokens.contains("from") || !tokens.contains("with")) {
      throw new InvalidCommandException("Expected format: edit " + "events" +
              " <property> <subject> from <start> with <newValue>");
    }

    int fromIndex = tokens.indexOf("from");
    int withIndex = tokens.indexOf("with");

    if (fromIndex + 1 >= tokens.size() || withIndex + 1 >= tokens.size()) {
      throw new InvalidCommandException("Expected format: edit " + "events" +
              " <property> <subject> from <start> with <newValue>");
    }
  }

  /**
   * Validates the structure of an edit series or events command.
   *
   * @param tokens      the list of command tokens
   * @param keywordType either "series" or "events" for error context
   * @throws InvalidCommandException if required structure is missing
   */
  private static void validateEditSeriesTokens(List<String> tokens, String keywordType) throws
          InvalidCommandException {
    String base = "edit " + keywordType;

    if (tokens.size() < 7 || !tokens.contains("from") || !tokens.contains("with")) {
      throw new InvalidCommandException("Expected format: " + base + " <property> <subject> " +
              "from <start> with <newValue>");
    }

    int fromIndex = tokens.indexOf("from");
    int withIndex = tokens.indexOf("with");

    if (fromIndex <= 3 || fromIndex + 1 >= tokens.size() || withIndex + 1 >= tokens.size()) {
      throw new InvalidCommandException("Missing or malformed 'from' or 'with' clause in " +
              base + " command");
    }
  }

  /**
   * Extracts a quoted or unquoted string from a list of tokens.
   *
   * @param tokens the sublist of tokens to join and clean
   * @return the unquoted combined string
   */
  private static String extractQuoted(List<String> tokens) {
    return String.join(" ", tokens).replaceAll("^\"|\"$", "");
  }

  /**
   * Constructs the EditEventsCommand with the appropriate field updated.
   *
   * @param model    the calendar model
   * @param property the field to edit
   * @param subject  the original subject
   * @param start    the original start datetime
   * @param newValue the new property value
   * @return an EditEventsCommand to apply changes across the series
   * @throws InvalidCommandException if the property is unrecognized or has bad format
   */
  private static ICommand buildEditEventsCommand(IDelegator model, String property, String subject,
                                                 LocalDateTime start, String newValue)
          throws InvalidCommandException {
    // look up the original event to get baseline values
    ROIEvent event = getRoiEvent(model, subject, start);

    // initialize fields with current values
    String newSubject = event.getSubject();
    LocalDateTime newStart = event.getStart();
    LocalDateTime newEnd = event.getEnd();
    String newDescription = event.getDescription();
    String newLocation = event.getLocation();
    String newStatus = event.getStatus().toString();

    // update only the specified field
    switch (property) {
      case "subject":
        newSubject = newValue;
        break;
      case "start":
        newStart = LocalDateTime.parse(newValue.trim());
        break;
      case "end":
        newEnd = LocalDateTime.parse(newValue.trim());
        break;
      case "description":
        newDescription = newValue;
        break;
      case "location":
        newLocation = newValue;
        break;
      case "status":
        newStatus = newValue;
        break;
      default:
        throw new InvalidCommandException("Unknown property: " + property);
    }

    // return the edit command that updates all events from this one onward
    return new EditEventsCommand(subject, start, newSubject, newStart, newEnd,
            newDescription, newLocation, newStatus);
  }

  // ====================================
  // Edit event series parser and helpers
  // ====================================

  /**
   * Parses a command to edit an entire series of events.
   *
   * @param model  the calendar model delegator
   * @param tokens the input command tokens
   * @return an EditSeriesCommand representing the full-series update
   * @throws InvalidCommandException if the command is malformed
   */
  private static ICommand parseEditSeries(IDelegator model, List<String> tokens) throws
          InvalidCommandException {
    validateEditSeriesTokens(tokens, "series");

    int fromIndex = tokens.indexOf("from");
    int withIndex = tokens.indexOf("with");

    String property = tokens.get(2).toLowerCase();
    String subject = String.join(" ", tokens.subList(3, fromIndex))
            .replaceAll("^\"|\"$", "");
    LocalDateTime start = LocalDateTime.parse(tokens.get(fromIndex + 1).trim());
    String newValue = String.join(" ", tokens.subList(withIndex + 1, tokens.size()))
            .replaceAll("^\"|\"$", "");

    return buildEditSeriesCommand(model, property, subject, start, newValue);
  }

  /**
   * Constructs the EditSeriesCommand with the specified field updated.
   *
   * @param model    the calendar model
   * @param property the property to update
   * @param subject  the original subject
   * @param start    the start time of the event
   * @param newValue the new property value
   * @return an EditSeriesCommand reflecting the series update
   * @throws InvalidCommandException if the property is invalid or parsing fails
   */
  private static ICommand buildEditSeriesCommand(IDelegator model, String property, String subject,
                                                 LocalDateTime start, String newValue)
          throws InvalidCommandException {
    // look up the original event
    ROIEvent event = getRoiEvent(model, subject, start);

    // initialize fields with current event values
    String newSubject = event.getSubject();
    LocalDateTime newStart = event.getStart();
    LocalDateTime newEnd = event.getEnd();
    String newDescription = event.getDescription();
    String newLocation = event.getLocation();
    String newStatus = event.getStatus().toString();

    // update the specified field
    switch (property) {
      case "subject":
        newSubject = newValue;
        break;
      case "start":
        newStart = LocalDateTime.parse(newValue.trim());
        break;
      case "end":
        newEnd = LocalDateTime.parse(newValue.trim());
        break;
      case "description":
        newDescription = newValue;
        break;
      case "location":
        newLocation = newValue;
        break;
      case "status":
        newStatus = newValue;
        break;
      default:
        throw new InvalidCommandException("Unknown property: " + property);
    }

    // return the full-series edit command
    return new EditSeriesCommand(subject, start, newSubject, newStart, newEnd,
            newDescription, newLocation, newStatus);
  }

  /**
   * Splits up the given string input into abstracted tokens for the parse method.
   *
   * @param input the given String input
   * @return the tokenized inputs of an edit series
   */
  private static List<String> tokenize(String input) {
    // store result
    List<String> result = new ArrayList<>();

    // apply quotes logic
    boolean inQuotes = false;
    StringBuilder current = new StringBuilder();

    // for each character in the input
    for (char c : input.toCharArray()) {
      // if there are quotes
      if (c == '"') {
        // de-quote any quotes
        inQuotes = !inQuotes;
        continue;
      }
      // if there is a space
      if (c == ' ' && !inQuotes) {
        // if there are still words
        if (current.length() > 0) {
          // add the resulting String
          result.add(current.toString().trim());
          current.setLength(0);
        }
      } else {
        // otherwise append the character itself
        current.append(c);
      }
    }
    // if the current display result exists
    if (current.length() > 0) {
      // add the result
      result.add(current.toString().trim());
    }
    return result;
  }
}