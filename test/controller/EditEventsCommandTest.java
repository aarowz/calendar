// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import model.CalendarMulti;
import model.DelegatorImpl;
import model.EventStatus;
import model.IDelegator;
import model.ROIEvent;
import view.IView;

import org.junit.Before;
import org.junit.Test;

import exceptions.CommandExecutionException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for the EditEventsCommand.
 * This test suite verifies batch-edit behavior on multiple calendar events,
 * ensuring consistent updates across targeted events and correct error handling.
 */
public class EditEventsCommandTest {
  private IDelegator model;
  private MockView view;

  /**
   * A mock view that records messages passed to renderMessage.
   */
  private static class MockView implements IView {
    StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) {
      log.append(message).append("\n");
    }
  }

  /**
   * Sets up the calendar and creates a recurring event series before each test.
   */
  @Before
  public void setup() throws CommandExecutionException {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    List<Character> days = List.of('M', 'W', 'F');
    CreateEventCommand cmd = new CreateEventCommand(
            "Standup",
            LocalDateTime.of(2025, 6, 2, 9, 0),
            LocalDateTime.of(2025, 6, 2, 9, 30),
            "Daily sync",
            "Zoom",
            EventStatus.PUBLIC,
            days,
            6,
            null
    );
    cmd.execute(model, view);
  }

  /**
   * Verifies that a property (e.g. subject) of all events in a series can be edited
   * starting from a specific date and time.
   */
  @Test
  public void testEditEventsFromSpecificStartTime() throws Exception {
    EditEventsCommand edit = new EditEventsCommand(
            "Standup",
            LocalDateTime.of(2025, 6, 6, 9, 0),
            "Standup v2",
            null, null, null, null,
            "public"
    );
    edit.execute(model, view);

    List<ROIEvent> updatedEvents = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      updatedEvents.addAll(model.getEventsOn(date));
    }

    boolean foundOld = false;
    boolean foundNew = false;
    for (ROIEvent e : updatedEvents) {
      if (e.getSubject().equals("Standup")) foundOld = true;
      if (e.getSubject().equals("Standup v2")) foundNew = true;
    }

    assertTrue(foundOld); // before June 6
    assertTrue(foundNew); // from June 6 onward
  }

  /**
   * Confirms that attempting to apply invalid changes to multiple events throws an exception.
   */
  @Test(expected = CommandExecutionException.class)
  public void testInvalidBatchEditThrowsException() throws CommandExecutionException, IOException {
    EditEventsCommand edit = new EditEventsCommand(
            "Standup",
            LocalDateTime.of(2025, 6, 4, 9, 0),
            "Standup",
            LocalDateTime.of(2025, 6, 4, 10, 0), // duplicate timing
            LocalDateTime.of(2025, 6, 4, 9, 0),
            null,
            null,
            "public"
    );
    edit.execute(model, view);
  }

  /**
   * Verifies that a property of all events with the same subject (in a series) can be edited.
   */
  @Test
  public void testEditAllEventsWithSameSubject() throws Exception {
    EditEventsCommand edit = new EditEventsCommand(
            "Standup",
            LocalDateTime.of(2025, 6, 2, 9, 0),
            "Standup",
            null,
            null,
            null,
            null,
            "public"
    );
    edit.execute(model, view);

    List<ROIEvent> allEvents = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      allEvents.addAll(model.getEventsOn(date));
    }

    assertEquals(12, allEvents.size());
    for (ROIEvent event : allEvents) {
      assertEquals("Standup", event.getSubject());
    }
  }

  /**
   * Verifies that the command’s string representation reflects a multi-event edit.
   */
  @Test
  public void testToStringReflectsBatchEdit() {
    EditEventsCommand edit = new EditEventsCommand(
            "Standup",
            LocalDateTime.of(2025, 6, 4, 9, 0),
            "GroupEdit",
            null,
            null,
            null,
            null,
            "public"
    );
    String desc = edit.toString().toLowerCase();
    assertTrue(desc.contains("standup"));
    assertTrue(desc.contains("edit"));
  }
}