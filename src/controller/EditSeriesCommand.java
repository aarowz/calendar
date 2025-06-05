// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.*;
import view.IView;
import exceptions.CommandExecutionException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a command to edit a recurring event series in the calendar model.
 * Applies updates to the series using the builder pattern to ensure immutability.
 */
public class EditSeriesCommand implements ICommand {

  private final String originalSubject;
  private final LocalDateTime originalStart;
  private final String newSubject;
  private final LocalDateTime newStart;
  private final LocalDateTime newEnd;
  private final String newDescription;
  private final String newLocation;
  private final EventStatus newStatus;
  private final List<Character> newRepeatDays;
  private final Integer newRepeatCount;
  private final LocalDate newRepeatUntil;

  /**
   * Constructs a command for editing a recurring event series.
   *
   * @param originalSubject the current subject of the series to identify it
   * @param originalStart   the current start time of the series to identify it
   * @param newSubject      new subject (nullable if unchanged)
   * @param newStart        new start time (nullable if unchanged)
   * @param newEnd          new end time (nullable if unchanged)
   * @param newDescription  new description (nullable if unchanged)
   * @param newLocation     new location (nullable if unchanged)
   * @param newStatus       new status (nullable if unchanged)
   * @param newRepeatDays   new recurrence days (nullable if unchanged)
   * @param newRepeatCount  new recurrence count (nullable if unchanged)
   * @param newRepeatUntil  new recurrence until date (nullable if unchanged)
   */
  public EditSeriesCommand(String originalSubject,
                           LocalDateTime originalStart,
                           String newSubject,
                           LocalDateTime newStart,
                           LocalDateTime newEnd,
                           String newDescription,
                           String newLocation,
                           EventStatus newStatus,
                           List<Character> newRepeatDays,
                           Integer newRepeatCount,
                           LocalDate newRepeatUntil) {
    this.originalSubject = originalSubject;
    this.originalStart = originalStart;
    this.newSubject = newSubject;
    this.newStart = newStart;
    this.newEnd = newEnd;
    this.newDescription = newDescription;
    this.newLocation = newLocation;
    this.newStatus = newStatus;
    this.newRepeatDays = newRepeatDays;
    this.newRepeatCount = newRepeatCount;
    this.newRepeatUntil = newRepeatUntil;
  }

  /**
   * Executes the series edit by creating a modified series and replacing the old one.
   *
   * @param calendar the calendar model to apply changes to
   * @param view     the view to display feedback
   * @throws CommandExecutionException if editing fails
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    try {
      // edit the series using the builder updater lambda
      calendar.editSeries(originalSubject, originalStart, builder -> {
        // only apply updates if the new value is provided

        if (newSubject != null) {
          builder.subject(newSubject);
        }

        if (newStart != null) {
          builder.start(newStart);
        }

        if (newEnd != null) {
          builder.end(newEnd);
        }

        if (newDescription != null) {
          builder.description(newDescription);
        }

        if (newLocation != null) {
          builder.location(newLocation);
        }

        if (newStatus != null) {
          builder.status(newStatus);
        }

        if (newRepeatDays != null) {
          builder.setRepeatDays(newRepeatDays);
        }

        if (newRepeatCount != null) {
          builder.setRepeatCount(newRepeatCount);
        }

        if (newRepeatUntil != null) {
          builder.setRepeatUntil(newRepeatUntil);
        }
      });

      // send success message to the view
      view.renderMessage("Recurring event series updated: " + originalSubject);
    } catch (Exception e) {
      // if anything fails, wrap the error
      throw new CommandExecutionException("Failed to update recurring event series: " + e.getMessage(), e);
    }
  }

  /**
   * Returns a short summary of this command.
   *
   * @return string description
   */
  @Override
  public String toString() {
    return "EditSeriesCommand{" + originalSubject + "}";
  }
}