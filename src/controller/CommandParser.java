// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import exceptions.InvalidCommandException;
import model.EventStatus;
import model.ROIEvent;

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

  /**
   * Parses all given commands.
   *
   * @param input the given input
   * @return the current command state after parsing
   * @throws InvalidCommandException when the command is invalid
   */
  public static ICommand parse(String input) throws InvalidCommandException {
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
        // if not enough fields
        if (tokens.size() < 2 || !tokens.get(1).equalsIgnoreCase("event")) {
          // throw error
          throw new InvalidCommandException("Expected 'create event' command.");
        }
        // otherwise parse the create option
        return parseCreateCommand(tokens);
      case "edit": // if edit
        // if not enough fields
        if (tokens.size() < 2) {
          // throw error
          throw new InvalidCommandException("incomplete edit command");
        }
        // determine edit options
        switch (tokens.get(1).toLowerCase()) {
          case "event": // if single event
            // apply single event logic
            return parseEditCommand(tokens);
          case "events": // if multiple events
            // apply multiple events logic
            return parseEditsCommand(tokens);
          case "series": // if series
            // apply series logic
            return parseEditSeries(tokens);
          default:
            // otherwise throw exception
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
          LocalDateTime start = LocalDateTime.parse(tokens.get(3));
          LocalDateTime end = LocalDateTime.parse(tokens.get(5));
          return new QueryEventsCommand(start, end);
        }
        break;
      case "show": // if show
        if (tokens.size() >= 4 && tokens.get(1).equals("status") && tokens.get(2).equals("on")) {
          // show status on <datetime>
          LocalDateTime time = LocalDateTime.parse(tokens.get(3));
          return new QueryEventsCommand(time, time);
        }
        break;
      case "exit": // if exit
        // apply exit logic
        return new ExitCommand();
      default:
        // otherwise throw exception
        throw new InvalidCommandException("unknown or malformed command: " + tokens.get(0));
    }
    return null;
  }

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
      LocalDateTime start = LocalDateTime.parse(tokens.get(fromIndex + 1));
      LocalDateTime end = LocalDateTime.parse(tokens.get(fromIndex + 3));

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

  /**
   * Parser for an edit command request.
   *
   * @param tokens the given edit command tokens
   * @return the desired edit output
   * @throws InvalidCommandException if the command is invalid
   */
  private static ICommand parseEditCommand(List<String> tokens) throws InvalidCommandException {
    // a bunch of formatting exceptions
    if (tokens.size() < 9 || !tokens.get(4).equalsIgnoreCase("from") ||
            !tokens.get(6).equalsIgnoreCase("to") ||
            !tokens.get(8).equalsIgnoreCase("with")) {
      throw new InvalidCommandException("Expected format: edit event <property> <subject>" +
              " from <start> to <end> with <newValue>");
    }

    // otherwise
    try {
      // grab the default fields
      String property = tokens.get(2).toLowerCase();
      String subject = tokens.get(3).replaceAll("^\"|\"$", "");
      LocalDateTime start = LocalDateTime.parse(tokens.get(5));
      String newValue = tokens.get(9);

      // apply placeholder event
      ROIEvent eventToEdit = getRoiEvent(subject, start);

      // apply default properties
      String newSubject = eventToEdit.getSubject();
      LocalDateTime newStart = eventToEdit.getStart();
      LocalDateTime newEnd = eventToEdit.getEnd();
      String newDescription = eventToEdit.getDescription();
      String newLocation = eventToEdit.getLocation();
      String newStatus = eventToEdit.getStatus().toString();

      // update subject on the property
      switch (property) {
        case "subject":
          newSubject = newValue;
          break;
        case "start":
          newStart = LocalDateTime.parse(newValue);
          break;
        case "end":
          newEnd = LocalDateTime.parse(newValue);
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
          // default exception
          throw new InvalidCommandException("Unknown property: " + property);
      }

      // return the event
      return new EditEventCommand(subject, start, newSubject, newStart, newEnd,
              newDescription, newLocation, newStatus);

    } catch (Exception e) {
      // otherwise throw an exception
      throw new InvalidCommandException("invalid format in edit event: " + e.getMessage());
    }
  }

  private static ROIEvent getRoiEvent(String subject, LocalDateTime start)
          throws InvalidCommandException {
    ROIEvent eventToEdit = null;
    // for each event
    for (ROIEvent e : model.CalendarModel.getAllEvents()) {
      // if the event contains a start and subject
      if (e.getSubject().equals(subject) && e.getStart().equals(start)) {
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

  /**
   * Parser for editing multiple events.
   *
   * @param tokens the given command
   * @return the desired output after editing multiple events
   * @throws InvalidCommandException if the command is invalid
   */
  private static ICommand parseEditsCommand(List<String> tokens) throws InvalidCommandException {
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
    LocalDateTime start = LocalDateTime.parse(tokens.get(fromIndex + 1));
    String newValue = String.join(" ", tokens.subList(withIndex + 1, tokens.size()))
            .replaceAll("^\"|\"$", "");

    // store the event to edit
    ROIEvent eventToEdit = getRoiEvent(subject, start);

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
        newStart = LocalDateTime.parse(newValue);
        break;
      case "end":
        newEnd = LocalDateTime.parse(newValue);
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

  /**
   * Parse a command to edit a series of events.
   *
   * @param tokens the given command
   * @return the updated series
   * @throws InvalidCommandException if the command is invalid
   */
  private static ICommand parseEditSeries(List<String> tokens) throws InvalidCommandException {
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
    LocalDateTime start = LocalDateTime.parse(tokens.get(fromIndex + 1));
    String newValue = String.join(" ", tokens.subList(withIndex + 1, tokens.size()))
            .replaceAll("^\"|\"$", "");

    // store the event to edit
    ROIEvent eventToEdit = getRoiEvent(subject, start);

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
        newStart = LocalDateTime.parse(newValue);
        break;
      case "end":
        newEnd = LocalDateTime.parse(newValue);
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
          result.add(current.toString());
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
      result.add(current.toString());
    }
    return result;
  }
}