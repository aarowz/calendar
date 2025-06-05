// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import exceptions.InvalidCommandException;
import model.EventStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses raw input strings into ICommand objects using the Builder pattern.
 * This class is responsible only for interpreting user text into executable commands.
 */
public class CommandParser {

  /**
   * Parses a single line of input and returns the corresponding command.
   *
   * @param input the input string
   * @return a valid ICommand object
   * @throws InvalidCommandException if the input is malformed or unrecognized
   */
  public static ICommand parse(String input) throws InvalidCommandException {
    // split input by spaces after trimming leading and trailing space
    String[] tokens = input.trim().split("\\s+");

    // if nothing was typed, throw an error
    if (tokens.length == 0) {
      throw new InvalidCommandException("Empty input");
    }

    // get the command name in lowercase to avoid case issues
    String commandType = tokens[0].toLowerCase();

    // figure out what command to build based on the keyword
    switch (commandType) {
      case "create event":
        // create an event
        return parseCreateCommand(tokens);
      case "edit event":
        // single event edit
        return parseEditCommand(tokens);
      case "edit events":
        // batch edit
        return parseEditsCommand(tokens);
      case "edit series":
        // edit a whole series
        return parseEditSeries(tokens);
      case "query events":
        // ask about event specifics
        return parseQueryCommand(tokens);
      case "exit":
        // exit the program
        return new ExitCommand();
      default:
        // process unknown command
        throw new InvalidCommandException("Unknown command: " + tokens[0]);
    }
  }

  /**
   * Parses a create-event command using builders.
   */
  private static ICommand parseCreateCommand(String[] tokens) throws InvalidCommandException {
    // we need at least the name, start time, and end time
    if (tokens.length < 4) {
      throw new InvalidCommandException("create-event requires at least subject, start, and end");
    }

    try {
      // get required inputs
      String subject = tokens[1]; // subject
      LocalDateTime start = LocalDateTime.parse(tokens[2]); // start
      LocalDateTime end = LocalDateTime.parse(tokens[3]); // end

      // get optional inputs if it's there
      String description = ""; // description
      if (tokens.length > 4) {
        description = tokens[4];
      }

      String location = null; // location
      if (tokens.length > 5) {
        location = tokens[5];
      }

      EventStatus status = EventStatus.PUBLIC; // status
      if (tokens.length > 6) {
        status = EventStatus.valueOf(tokens[6].toUpperCase());
      }

      // set up recurrence defaults
      List<Character> repeatDays = null;
      Integer repeatCount = null;
      LocalDate repeatUntil = null;

      // check for extra keywords like "repeats", "for", or "until"
      if (tokens.length > 7) {
        for (int i = 7; i < tokens.length; i++) {
          switch (tokens[i]) {
            case "repeats": // if there are repeats
              repeatDays = new ArrayList<>();
              for (char c : tokens[++i].toCharArray()) {
                repeatDays.add(c); // add each char to repeatDays
              }
              break;
            case "for": // if there is a for
              repeatCount = Integer.parseInt(tokens[++i]); // number of times to repeat
              break;
            case "until": // if there is an until
              repeatUntil = LocalDate.parse(tokens[++i]); // repeat until this day
              break;
          }
        }
      }

      // build the actual command
      return new CreateEventCommand(
              subject, start, end, description, location, status,
              repeatDays, repeatCount, repeatUntil);

    } catch (DateTimeParseException | IllegalArgumentException e) {
      // something went wrong with date formatting or input
      throw new InvalidCommandException("Invalid input format for create-event: "
              + e.getMessage());
    }
  }

  /**
   * Parses an edit-event command.
   */
  private static ICommand parseEditCommand(String[] tokens) throws InvalidCommandException {
    // make sure we have original + at least some new info
    if (tokens.length < 5) {
      throw new InvalidCommandException("edit event requires original subject and " +
              "start time plus at least one new value");
    }

    try {
      // grab original event details
      String originalSubject = tokens[1];
      LocalDateTime originalStart = LocalDateTime.parse(tokens[2]);

      // grab updated values only if they're not "null"
      // initialize optional edit values
      String newSubject = null;
      if (!tokens[3].equals("null")) {
        newSubject = tokens[3]; // only assign if not the string "null"
      }

      LocalDateTime newStart = null;
      if (!tokens[4].equals("null")) {
        newStart = LocalDateTime.parse(tokens[4]); // parse start if it's provided
      }

      LocalDateTime newEnd = null;
      if (tokens.length > 5 && !tokens[5].equals("null")) {
        newEnd = LocalDateTime.parse(tokens[5]); // only parse end if not "null"
      }

      String newDescription = null;
      if (tokens.length > 6 && !tokens[6].equals("null")) {
        newDescription = tokens[6]; // set description if available
      }

      String newLocation = null;
      if (tokens.length > 7 && !tokens[7].equals("null")) {
        newLocation = tokens[7]; // set location if given
      }

      EventStatus newStatus = null;
      if (tokens.length > 8 && !tokens[8].equals("null")) {
        newStatus = EventStatus.valueOf(tokens[8].toUpperCase()); // convert status string to enum
      }

      // make the edit command with everything we got
      return new EditEventCommand(
              originalSubject, originalStart,
              newSubject, newStart, newEnd,
              newDescription, newLocation, newStatus);
    } catch (DateTimeParseException | IllegalArgumentException e) {
      // something was badly formatted
      throw new InvalidCommandException("Invalid input format for edit event: " + e.getMessage());
    }
  }

  /**
   * Parses a query-events command.
   */
  private static ICommand parseQueryCommand(String[] tokens) throws InvalidCommandException {
    // must have a date
    if (tokens.length < 2) {
      throw new InvalidCommandException("query-events requires at least a date");
    }

    try {
      // try to parse that date
      LocalDate date = LocalDate.parse(tokens[1]);
      return new QueryEventsCommand(date);
    } catch (DateTimeParseException e) {
      // couldn't understand the date
      throw new InvalidCommandException("Invalid date format for query-events");
    }
  }

  // The following stubs need to be implemented

  /**
   * parses a bulk edit (edit events) command — not done yet
   */
  private static ICommand parseEditsCommand(String[] tokens) throws InvalidCommandException {
    throw new InvalidCommandException("edit events command not implemented yet");
  }

  /**
   * parses a series edit command — also not done yet
   */
  private static ICommand parseEditSeries(String[] tokens) throws InvalidCommandException {
    throw new InvalidCommandException("edit series command not implemented yet");
  }
}