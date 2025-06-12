// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import exceptions.CommandExecutionException;
import model.*;
import view.IView;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for the EditEventCommand.
 * This suite verifies that the command correctly modifies existing calendar events,
 * handling edge cases such as partial edits, invalid edits, and editing non-existent events.
 */
public class EditEventCommandTest {
  private IDelegator model;
  private MockView view;

  /**
   * A mock view that records messages passed to renderMessage.
   */
  private static class MockView implements IView {
    StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) throws IOException {
      log.append(message).append("\n");
    }

    public String getLog() {
      return log.toString();
    }
  }

  /**
   * Sets up a new calendar and adds a base event before each test.
   */
  @Before
  public void setup() throws CommandExecutionException {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    CreateEventCommand create = new CreateEventCommand(
            "Workshop",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            LocalDateTime.of(2025, 6, 6, 11, 0),
            "Initial desc", "Room 101", EventStatus.PUBLIC, null,
            null, null
    );
    create.execute(model, view);
  }

  /**
   * Verifies that an existing event can be edited with new values.
   */
  @Test
  public void testEditEventManyNewValues() throws CommandExecutionException, IOException {
    EditEventCommand cmd = new EditEventCommand(
            "Workshop",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            "Review",
            LocalDateTime.of(2025, 6, 6, 11, 0),
            LocalDateTime.of(2025, 6, 6, 12, 0),
            "Updated desc",
            "Room 102",
            "private"
    );
    cmd.execute(model, view);

    List<IEvent> events = CalendarModel.getAllEvents();
    assertEquals(1, events.size());

    IEvent e = events.get(0);
    assertEquals("Review", e.getSubject());
    assertEquals("Updated desc", e.getDescription());
    assertEquals("Room 102", e.getLocation());
    assertEquals(EventStatus.PRIVATE, e.getStatus());
  }

  /**
   * Verifies that an exception is thrown when attempting to edit a non-existent event.
   */
  @Test(expected = CommandExecutionException.class)
  public void testEditNonExistentEventThrowsException() throws CommandExecutionException, IOException {
    EditEventCommand cmd = new EditEventCommand(
            "GhostEvent",
            LocalDateTime.of(2025, 6, 1, 8, 0),
            "X",
            null,
            null,
            null,
            null,
            null
    );
    cmd.execute(model, view);
  }

  /**
   * Verifies that attempting to create an EditEventCommand with no changes throws nothing
   * but does nothing meaningful either (depending on implementation).
   */
  @Test
  public void testEditEventNoEffectIsNoopOrValid() throws Exception {
    EditEventCommand cmd = new EditEventCommand(
            "Workshop",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            null, null, null, null, null,
            null
    );
    cmd.execute(model, view); // might succeed silently, or log a warning
  }

  /**
   * Verifies that the success message is properly displayed after a valid edit.
   */
  @Test
  public void testEditSuccessMessageDisplayed() throws CommandExecutionException, IOException {
    EditEventCommand cmd = new EditEventCommand(
            "Workshop",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            "Review",
            null,
            null,
            null,
            null,
            null
    );
    cmd.execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("edited"));
  }

  /**
   * Verifies that a property of an existing calendar event (e.g., subject) can be edited.
   */
  @Test
  public void testEditEventProp() throws Exception {
    // Create event manually
    CreateEventCommand create = new CreateEventCommand(
            "Lunch",
            LocalDateTime.of(2025, 6, 10, 12, 0),
            LocalDateTime.of(2025, 6, 10, 13, 0),
            null, null, null,
            null, null, null
    );
    create.execute(model, view);

    // Edit subject
    EditEventCommand edit = new EditEventCommand(
            "Lunch",
            LocalDateTime.of(2025, 6, 10, 12, 0),
            "TeamLunch",
            null, null, null, null, null
    );
    edit.execute(model, view);

    List<ROIEvent> events = model.getEventsOn(LocalDate.of(2025, 6, 10));
    assertEquals(1, events.size());

    IEvent updated = (IEvent) events.get(0);
    assertEquals("TeamLunch", updated.getSubject());
    assertEquals("2025-06-10T12:00", updated.getStart().toString());
    assertEquals("2025-06-10T13:00", updated.getEnd().toString());
  }

  /**
   * Confirms that the command’s string representation contains the event subject.
   */
  @Test
  public void testToStringContainsSubject() {
    EditEventCommand cmd = new EditEventCommand(
            "Workshop",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            "Updated",
            null,
            null,
            null,
            null,
            null
    );
    assertTrue(cmd.toString().contains("Workshop"));
  }
}