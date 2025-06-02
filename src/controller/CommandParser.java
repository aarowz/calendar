// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import exceptions.InvalidCommandException;

/**
 * Parses raw input strings into ICommand objects.
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
    // TODO: analyze the input string and return a new instance of the correct command
    // e.g., return new CreateEventCommand(...) or throw new InvalidCommandException(...)
    return null;
  }

  /**
   * Helper method to parse event creation parameters.
   *
   * @param tokens an array of input tokens
   * @return a constructed CreateEventCommand
   * @throws InvalidCommandException if the arguments are invalid
   */
  private static ICommand parseCreateCommand(String[] tokens) throws InvalidCommandException {
    // TODO: parse arguments and instantiate CreateEventCommand
    return null;
  }

  /**
   * Helper method to parse event editing parameters.
   *
   * @param tokens an array of input tokens
   * @return a constructed EditEventCommand
   * @throws InvalidCommandException if the arguments are invalid
   */
  private static ICommand parseEditCommand(String[] tokens) throws InvalidCommandException {
    // TODO: parse arguments and instantiate EditEventCommand
    return null;
  }

  /**
   * Helper method to parse event query parameters.
   *
   * @param tokens an array of input tokens
   * @return a constructed QueryEventsCommand
   * @throws InvalidCommandException if the arguments are invalid
   */
  private static ICommand parseQueryCommand(String[] tokens) throws InvalidCommandException {
    // TODO: parse arguments and instantiate QueryEventsCommand
    return null;
  }

  /**
   * Helper method to parse an exit command.
   *
   * @return an ExitCommand
   */
  private static ICommand parseExitCommand() {
    // TODO: return a new ExitCommand
    return null;
  }
}