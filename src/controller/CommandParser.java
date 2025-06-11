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
   * Parses all given commands.
   *
   * @param input the given input
   * @return the current command state after parsing
   * @throws InvalidCommandException when the command is invalid
   */
  public static ICommand parse(IDelegator model, String input) throws InvalidCommandException {
    // split input into tokens
    List<String> tokens = tokenize(input);

    // if there are no tokens
    if (tokens.isEmpty()) {
      // display empty input
      throw new InvalidCommandException("empty input");
    }

    // identify command type
    String commandType = tokens.get(0).toLowerCase();

    // apply logic to command type
    switch (commandType) {
      case "create": // if create
        // handle 'create calendar --name <name> --timezone <zone>'
        if (tokens.size() >= 6 &&
                tokens.get(1).equalsIgnoreCase("calendar") &&
                tokens.get(2).equalsIgnoreCase("--name") &&
                tokens.get(4).equalsIgnoreCase("--timezone")) {
          String name = tokens.get(3);
          String zone = tokens.get(5);
          return new CreateCalendarCommand(name, zone);
        }

        // fall back to 'create event'
        if (tokens.size() < 2 || !tokens.get(1).equalsIgnoreCase("event")) {
          throw new InvalidCommandException("Expected 'create event' or 'create calendar' " +
                  "command.");
        }
        return parseCreateCommand(tokens);
      case "edit":
        // ensure there's at least a second token
        if (tokens.size() < 2) {
          throw new InvalidCommandException("incomplete edit command");
        }

        // check for 'edit calendar' before checking subtypes
        if (tokens.get(1).equalsIgnoreCase("calendar")) {
          if (tokens.size() >= 6 &&
                  tokens.get(2).equalsIgnoreCase("--name") &&
                  tokens.get(4).equalsIgnoreCase("--property")) {
            // grab the name, property, and new value
            String name = tokens.get(3);
            String property = tokens.get(5);
            String newValue = String.join(" ", tokens.subList(6, tokens.size()))
                    .replaceAll("^\"|\"$", "");
            // return the new edited calendar
            return new EditCalendarCommand(name, property, newValue);
          } else {
            throw new InvalidCommandException("Expected format: edit calendar --name <name> " +
                    "--property <prop> <value>");
          }
        }

        // fallback to event/series editing
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
      case "print": // if print
        if (tokens.size() >= 4 && tokens.get(1).equals("events") && tokens.get(2).equals("on")) {
          // print events on <date>
          LocalDate date = LocalDate.parse(tokens.get(3));
          return new QueryEventsCommand(date);
        } else if (tokens.size() >= 6 && tokens.get(1).equals("events") &&
                tokens.get(2).equals("from")) {
          // print events from <start> to <end>
          LocalDateTime start = LocalDateTime.parse(tokens.get(3).trim());
          LocalDateTime end = LocalDateTime.parse(tokens.get(5).trim());
          return new QueryEventsCommand(start, end);
        }
        break;
      case "show": // if show
        if (tokens.size() >= 4 && tokens.get(1).equals("status") && tokens.get(2).equals("on")) {
          // show status on <datetime>
          LocalDateTime time = LocalDateTime.parse(tokens.get(3).trim());
          return new QueryEventsCommand(time, time);
        }
        break;
      case "exit": // if exit
        // apply exit logic
        return new ExitCommand();
      case "use": // if use
        if (tokens.size() >= 4 &&
                tokens.get(1).equalsIgnoreCase("calendar") &&
                tokens.get(2).equalsIgnoreCase("--name")) {
          // use the name
          String name = tokens.get(3);
          return new UseCalendarCommand(name);
        }
        throw new InvalidCommandException("Expected format: use calendar --name <name>");
      case "copy": // if copy
        if (tokens.size() >= 8 && tokens.get(1).equalsIgnoreCase("event")) {
          int onIndex = tokens.indexOf("on");
          int targetIndex = tokens.indexOf("--target");
          int toIndex = tokens.indexOf("to");

          // if wrong copy format
          if (onIndex < 2 || targetIndex < onIndex || toIndex < targetIndex) {
            throw new InvalidCommandException("Expected format: copy event <name> on <start> " +
                    "--target <calendar> to <targetStart>");
          }

          // parse event name from tokens between 'event' and 'on'
          String eventName = String.join(" ", tokens.subList(2, onIndex))
                  .replaceAll("^\"|\"$", "")
                  .trim();

          // parse date times and target name
          LocalDateTime sourceStart = LocalDateTime.parse(tokens.get(onIndex + 1).trim());
          String targetCalendar = tokens.get(targetIndex + 1).trim();
          LocalDateTime targetStart = LocalDateTime.parse(tokens.get(toIndex + 1).trim());

          return new CopyEventCommand(eventName, sourceStart, targetCalendar, targetStart);
        }

        // check for copy events on
        if (tokens.size() >= 8 &&
                tokens.get(1).equalsIgnoreCase("events") &&
                tokens.get(2).equalsIgnoreCase("on") &&
                tokens.get(4).equalsIgnoreCase("--target") &&
                tokens.get(6).equalsIgnoreCase("to")) {

          LocalDate sourceDate = LocalDate.parse(tokens.get(3));
          String targetCalendar = tokens.get(5);
          LocalDate targetDate = LocalDate.parse(tokens.get(7));

          return new CopyEventsOnCommand(sourceDate, targetCalendar, targetDate);
        }

        // check for: copy events between
        if (tokens.size() >= 10 &&
                tokens.get(1).equalsIgnoreCase("events") &&
                tokens.get(2).equalsIgnoreCase("between") &&
                tokens.get(4).equalsIgnoreCase("and") &&
                tokens.get(6).equalsIgnoreCase("--target") &&
                tokens.get(8).equalsIgnoreCase("to")) {

          LocalDate startDate = LocalDate.parse(tokens.get(3));
          LocalDate endDate = LocalDate.parse(tokens.get(5));
          String targetCalendar = tokens.get(7);
          LocalDate targetStart = LocalDate.parse(tokens.get(9));

          return new CopyEventsBetweenCommand(startDate, endDate, targetCalendar, targetStart);
        }

        // fallthrough: copy events on / between will go here later
        throw new InvalidCommandException("unknown or malformed copy command");

      default:
        // otherwise throw exception
        throw new InvalidCommandException("unknown or malformed command: " + tokens.get(0));
    }
    return null;
  }

  // ===============================
  // Create event parser and helpers
  // ===============================

  /**
   * Parses a request to create a command.
   *
   * @param tokens the given commands token
   * @return the desired output after creating a command
   * @throws InvalidCommandException when the command is invalid
   */
  private static ICommand parseCreateCommand(List<String> tokens) throws InvalidCommandException {
    try {
      // case: create event "Subject" on YYYY-MM-DD
      for (int i = 2; i < tokens.size(); i++) {
        // if keyword is on
        if (tokens.get(i).equalsIgnoreCase("on")) {
          // process fields
          String subject = String.join(" ", tokens.subList(2, i))
                  .replaceAll("^\"|\"$", "");
          LocalDate date = LocalDate.parse(tokens.get(i + 1));
          LocalDateTime start = date.atTime(8, 0);
          LocalDateTime end = date.atTime(17, 0);

          // handle all-day recurring series
          List<Character> repeatDays = null;
          Integer repeatCount = null;
          LocalDate repeatUntil = null;

          // if keyword repeats exists
          if (tokens.contains("repeats")) {
            // apply repeat logic
            int repeatsIndex = tokens.indexOf("repeats");
            String dayString = tokens.get(repeatsIndex + 1);

            // accumulate repeat days
            repeatDays = toCharList(dayString);

            // if keyword for exists
            if (tokens.contains("for")) {
              // apply for logic
              repeatCount = Integer.parseInt(tokens.get(tokens.indexOf("for") + 1));
            } else if (tokens.contains("until")) { // if keyword until exists
              // apply until logic
              repeatUntil = LocalDate.parse(tokens.get(tokens.indexOf("until") + 1));
            }
          }

          // give back the created event
          return new CreateEventCommand(subject, start, end, "", null,
                  EventStatus.PUBLIC, repeatDays, repeatCount, repeatUntil);
        }
      }

      // case: create event "Subject" from DATETIME to DATETIME
      int fromIndex = tokens.indexOf("from");
      // if "to" is not placed properly
      if (fromIndex == -1 || !tokens.get(fromIndex + 2).equalsIgnoreCase("to")) {
        // throw exception
        throw new InvalidCommandException("expected format: create event " +
                "<subject> from <start> to <end>");
      }

      // find repeat index
      int repeatsIndex = tokens.indexOf("repeats");

      // process basic fields
      String subject = String.join(" ", tokens.subList(2, fromIndex))
              .replaceAll("^\"|\"$", "");
      LocalDateTime start = LocalDateTime.parse(tokens.get(fromIndex + 1).trim());
      LocalDateTime end = LocalDateTime.parse(tokens.get(fromIndex + 3).trim());

      // process repeat fields
      List<Character> repeatDays = null;
      Integer repeatCount = null;
      LocalDate repeatUntil = null;

      // if repeat exists
      if (repeatsIndex != -1) {
        // apply repeat logic
        String dayString = tokens.get(repeatsIndex + 1);
        repeatDays = toCharList(dayString);

        // repeat case
        if (tokens.contains("for")) {
          // set repeat count to number after for
          repeatCount = Integer.parseInt(tokens.get(tokens.indexOf("for") + 1));
        } else if (tokens.contains("until")) { // until case
          // otherwise set repeat count to number after until
          repeatUntil = LocalDate.parse(tokens.get(tokens.indexOf("until") + 1));
        }
      }

      // return newly created event
      return new CreateEventCommand(subject, start, end, "", null,
              EventStatus.PUBLIC, repeatDays, repeatCount, repeatUntil);

    } catch (DateTimeParseException e) {
      // otherwise throw exception
      throw new InvalidCommandException("invalid date/time format: " + e.getMessage());
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
   * Parser for an edit command request.
   *
   * @param tokens the given edit command tokens
   * @return the desired edit output
   * @throws InvalidCommandException if the command is invalid
   */
  private static ICommand parseEditCommand(IDelegator model, List<String> tokens) throws InvalidCommandException {
    // a bunch of formatting exceptions
    if (tokens.size() < 9 || !tokens.contains("from") || !tokens.contains("to") ||
            !tokens.contains("with")) {
      throw new InvalidCommandException("Expected format: edit event <property> <subject> from " +
              "<start> to <end> with <newValue>");
    }

    // otherwise
    try {
      // grab the default fields
      int fromIndex = tokens.indexOf("from");
      int withIndex = tokens.indexOf("with");

      // parse property and subject
      String property = tokens.get(2).toLowerCase();
      String subject = String.join(" ", tokens.subList(3, fromIndex))
              .replaceAll("^\"|\"$", "");

      // parse times and new value
      LocalDateTime start = LocalDateTime.parse(tokens.get(fromIndex + 1).trim());
      String newValue = String.join(" ", tokens.subList(withIndex + 1, tokens.size()))
              .replaceAll("^\"|\"$", "");

      // look up original event to pre-fill existing fields
      ROIEvent eventToEdit = getRoiEvent(model, subject, start);
      String newSubject = eventToEdit.getSubject();
      LocalDateTime newStart = eventToEdit.getStart();
      LocalDateTime newEnd = eventToEdit.getEnd();
      String newDescription = eventToEdit.getDescription();
      String newLocation = eventToEdit.getLocation();
      String newStatus = eventToEdit.getStatus().toString();

      // update only the specified property
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

      // return the event
      return new EditEventCommand(subject, start, newSubject, newStart, newEnd,
              newDescription, newLocation, newStatus);

    } catch (Exception e) {
      throw new InvalidCommandException("invalid format in edit event: " + e.getMessage());
    }
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
   * Parser for editing multiple events.
   *
   * @param tokens the given command
   * @return the desired output after editing multiple events
   * @throws InvalidCommandException if the command is invalid
   */
  private static ICommand parseEditsCommand(IDelegator model, List<String> tokens) throws InvalidCommandException {
    // index from and with
    int fromIndex = tokens.indexOf("from");
    int withIndex = tokens.indexOf("with");

    // exceptions for from and with
    if (fromIndex == -1 || withIndex == -1 || fromIndex + 1 >= tokens.size() ||
            withIndex + 1 >= tokens.size()) {
      throw new InvalidCommandException("Expected format: edit events <property> " +
              "<subject> from <start> with <newValue>");
    }

    // parse the basic calendar fields
    String property = tokens.get(2).toLowerCase();
    String subject = String.join(" ", tokens.subList(3, fromIndex))
            .replaceAll("^\"|\"$", "");
    LocalDateTime start = LocalDateTime.parse(tokens.get(fromIndex + 1).trim());
    String newValue = String.join(" ", tokens.subList(withIndex + 1, tokens.size()))
            .replaceAll("^\"|\"$", "");

    // store the event to edit
    ROIEvent eventToEdit = getRoiEvent(model, subject, start);

    // create new field instances for property
    String newSubject = eventToEdit.getSubject();
    LocalDateTime newStart = eventToEdit.getStart();
    LocalDateTime newEnd = eventToEdit.getEnd();
    String newDescription = eventToEdit.getDescription();
    String newLocation = eventToEdit.getLocation();
    String newStatus = eventToEdit.getStatus().toString();

    // apply property logic
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
        // throw an exception for default
        throw new InvalidCommandException("Unknown property: " + property);
    }

    // return the edits for each event
    return new EditEventsCommand(subject, start, newSubject, newStart, newEnd,
            newDescription, newLocation, newStatus);
  }

  // ====================================
  // Edit event series parser and helpers
  // ====================================

  /**
   * Parse a command to edit a series of events.
   *
   * @param tokens the given command
   * @return the updated series
   * @throws InvalidCommandException if the command is invalid
   */
  private static ICommand parseEditSeries(IDelegator model, List<String> tokens) throws InvalidCommandException {
    // find the with index
    int withIndex = tokens.indexOf("with");

    // apply exceptions if with index is wrong
    if (tokens.size() < 6 || withIndex == -1) {
      throw new InvalidCommandException("edit series requires property, subject, " +
              "start time, and new value");
    }

    // find the property
    String property = tokens.get(2);

    // find from index
    int fromIndex = tokens.indexOf("from");

    // throw exception if from index is invalid
    if (fromIndex <= 3) {
      throw new InvalidCommandException("Missing or malformed 'from' clause in " +
              "edit series command");
    }

    // parse the default constructor fields
    String subject = String.join(" ", tokens.subList(3, fromIndex))
            .replaceAll("^\"|\"$", "");
    LocalDateTime start = LocalDateTime.parse(tokens.get(fromIndex + 1).trim());
    String newValue = String.join(" ", tokens.subList(withIndex + 1, tokens.size()))
            .replaceAll("^\"|\"$", "");

    // store the event to edit
    ROIEvent eventToEdit = getRoiEvent(model, subject, start);

    // create new instances for property
    String newSubject = eventToEdit.getSubject();
    LocalDateTime newStart = eventToEdit.getStart();
    LocalDateTime newEnd = eventToEdit.getEnd();
    String newDescription = eventToEdit.getDescription();
    String newLocation = eventToEdit.getLocation();
    String newStatus = eventToEdit.getStatus().toString();

    // apply property logic
    switch (property.toLowerCase()) {
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
        // throw exception for unknown property
        throw new InvalidCommandException("Unknown property: " + property);
    }

    // return the edited series
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