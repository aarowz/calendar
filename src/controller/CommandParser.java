// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import exceptions.InvalidCommandException;
import model.EventStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
    String[] tokens = input.trim().split("\\s+");
    if (tokens.length == 0) {
      throw new InvalidCommandException("Empty input");
    }

    String commandType = tokens[0].toLowerCase();
    switch (commandType) {
      case "create event":
        return parseCreateCommand(tokens);
      case "edit event":
        return parseEditCommand(tokens);
      case "edit events":
        return parseEditsCommand(tokens);
      case "edit series":
        return parseEditSeries(tokens);
      case "query events":
        return parseQueryCommand(tokens);
      case "exit":
        return new ExitCommand();
      default:
        throw new InvalidCommandException("Unknown command: " + tokens[0]);
    }
  }

  /**
   * Parses a create-event command using builders.
   */
  private static ICommand parseCreateCommand(String[] tokens) throws InvalidCommandException {
    if (tokens.length < 4) {
      throw new InvalidCommandException("create-event requires at least subject, start, and end");
    }
    try {
      String subject = tokens[1];
      LocalDateTime start = LocalDateTime.parse(tokens[2]);
      LocalDateTime end = LocalDateTime.parse(tokens[3]);
      String description = tokens.length > 4 ? tokens[4] : "";
      String location = tokens.length > 5 ? tokens[5] : null;
      EventStatus status = tokens.length > 6 ? EventStatus.valueOf(tokens[6].toUpperCase()) : EventStatus.PUBLIC;

      List<Character> repeatDays = null;
      Integer repeatCount = null;
      LocalDate repeatUntil = null;
      if (tokens.length > 7) {
        for (int i = 7; i < tokens.length; i++) {
          if (tokens[i].equals("repeats")) {
            repeatDays = new ArrayList<>();
            for (char c : tokens[++i].toCharArray()) {
              repeatDays.add(c);
            }
          } else if (tokens[i].equals("for")) {
            repeatCount = Integer.parseInt(tokens[++i]);
          } else if (tokens[i].equals("until")) {
            repeatUntil = LocalDate.parse(tokens[++i]);
          }
        }
      }

      return new CreateEventCommand(
              subject, start, end, description, location, status,
              repeatDays, repeatCount, repeatUntil);

    } catch (DateTimeParseException | IllegalArgumentException e) {
      throw new InvalidCommandException("Invalid input format for create-event: " + e.getMessage());
    }
  }

  /**
   * Parses an edit-event command.
   */
  private static ICommand parseEditCommand(String[] tokens) throws InvalidCommandException {
    if (tokens.length < 5) {
      throw new InvalidCommandException("edit event requires original subject and start time plus at least one new value");
    }
    try {
      // take in the subject and original start time
      String originalSubject = tokens[1];
      LocalDateTime originalStart = LocalDateTime.parse(tokens[2]);

      // create new instances of the edited state
      String newSubject = !tokens[3].equals("null") ? tokens[3] : null;
      LocalDateTime newStart = !tokens[4].equals("null") ? LocalDateTime.parse(tokens[4]) : null;
      LocalDateTime newEnd = tokens.length > 5 && !tokens[5].equals("null") ? LocalDateTime.parse(tokens[5]) : null;
      String newDescription = tokens.length > 6 && !tokens[6].equals("null") ? tokens[6] : null;
      String newLocation = tokens.length > 7 && !tokens[7].equals("null") ? tokens[7] : null;
      EventStatus newStatus = tokens.length > 8 && !tokens[8].equals("null") ? EventStatus.valueOf(tokens[8].toUpperCase()) : null;

      // return a new single edit with the instances created above
      // NOTE: the execute() function is called in CalendarController instead of here because
      // we defined execute as an interface method that applies for six commands
      return new EditEventCommand(
              originalSubject, originalStart,
              newSubject, newStart, newEnd,
              newDescription, newLocation, newStatus);
    } catch (DateTimeParseException | IllegalArgumentException e) {
      // if the formatting does not match the ones given in instructions, throw an exception
      throw new InvalidCommandException("Invalid input format for edit event: " + e.getMessage());
    }
  }

  /**
   * Parses a query-events command.
   */
  private static ICommand parseQueryCommand(String[] tokens) throws InvalidCommandException {
    if (tokens.length < 2) {
      throw new InvalidCommandException("query-events requires at least a date");
    }
    try {
      LocalDate date = LocalDate.parse(tokens[1]);
      return new QueryEventsCommand(date);
    } catch (DateTimeParseException e) {
      throw new InvalidCommandException("Invalid date format for query-events");
    }
  }
}