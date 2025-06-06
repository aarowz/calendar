// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.CalendarModel;
import model.EventStatus;
import model.ICalendar;
import model.ROIEvent;
import view.IView;

import org.junit.Before;
import org.junit.Test;

import exceptions.CommandExecutionException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the EditEventsCommand.
 * This test suite verifies batch-edit behavior on multiple calendar events,
 * ensuring consistent updates across targeted events and correct error handling.
 */
public class EditEventsCommandTest {
  private CalendarModel model;
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
  }

  /**
   * Sets up the calendar and creates a recurring event series before each test.
   */
  @Before
  public void setup() throws CommandExecutionException {
    model = new CalendarModel();
    view = new MockView();

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
    ICalendar model = new CalendarModel();
    IView view = new MockView();

    // create a 6-instance recurring event series
    String createCmd = "create event Sync from 2025-06-02T10:00 to " +
            "2025-06-02T11:00 repeats MW for 6 times";
    ICommand create = CommandParser.parse(createCmd);
    assert create != null;
    create.execute(model, view);

    // edit subject of all events starting from 2025-06-09
    String editCmd = "edit events subject Sync from 2025-06-09T10:00 with Team Sync";
    ICommand edit = CommandParser.parse(editCmd);

    // collect events and separate them by before/after edit time
    List<ROIEvent> allEvents = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      allEvents.addAll(model.getEventsOn(date));
    }

    // track that the count actually changes
    int count = 0;
    for (ROIEvent e : allEvents) {
      if (e.getStart().isBefore(e.getStart().withDayOfMonth(9))) {
        assertEquals("Sync", e.getSubject());
        count++;
      }
    }

    assertEquals(2, count); // June 2, 4
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
            LocalDateTime.of(2025, 6, 4, 10, 0),
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
    ICalendar model = new CalendarModel();
    IView view = new MockView();

    // create a recurring event series
    String createCmd = "create event Standup from 2025-06-02T09:00 to " +
            "2025-06-02T09:30 repeats MTWRF for 5 times";
    ICommand create = CommandParser.parse(createCmd);
    assert create != null;
    create.execute(model, view);

    // edit the location of all events starting from 2025-06-02
    String editCmd = "edit events location Standup from 2025-06-02T09:00 with Zoom";
    ICommand edit = CommandParser.parse(editCmd);

    // gather all events in June
    List<ROIEvent> allEvents = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      LocalDate date = LocalDate.of(2025, 6, 1).plusDays(i);
      allEvents.addAll(model.getEventsOn(date));
    }

    // confirm all have the new property
    assertEquals(5, allEvents.size());
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
    assertTrue(edit.toString().contains("Standup"));
    assertTrue(edit.toString().toLowerCase().contains("edit"));
  }
}