// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package integration;

import model.CalendarEvent;
import model.CalendarModel;

import org.junit.Before;
import org.junit.Test;

import view.IView;

import java.io.StringReader;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Tests for valid command scripts in headless mode.
 */
public class HeadlessModeValidScriptsTest {

  private CalendarModel model;
  private MockView view;

  @Before
  public void setUp() {
    model = new CalendarModel();
    view = new MockView();
  }

  @Test
  public void testCreateEvent() {
    String script = "create-event 2025-06-10T14:00 Project Discussion\nexit";
    CalendarController controller = new CalendarController(model, view, new StringReader(script));
    controller.run();

    assertEquals(1, model.getEventsBetween(
            LocalDateTime.of(2025, 6, 10, 0, 0),
            LocalDateTime.of(2025, 6, 10, 23, 59)
    ).size());

    assertTrue(view.getOutput().toLowerCase().contains("event created"));
  }

  @Test
  public void testEditEvent() {
    CalendarEvent original = new CalendarEvent("Team Sync", LocalDateTime.of(2025, 6, 11, 9, 0));
    model.addEvent(original);

    String script = "edit-event 2025-06-11T09:00 Team Sync Updated 2025-06-11T10:00\nexit";
    CalendarController controller = new CalendarController(model, view, new StringReader(script));
    controller.run();

    assertTrue(view.getOutput().toLowerCase().contains("event edited"));

    assertTrue(model.getEventsBetween(
            LocalDateTime.of(2025, 6, 11, 10, 0),
            LocalDateTime.of(2025, 6, 11, 10, 0)
    ).stream().anyMatch(e -> e.getTitle().equals("Team Sync Updated")));
  }

  @Test
  public void testQueryEvents() {
    model.addEvent(new CalendarEvent("Meeting A", LocalDateTime.of(2025, 6, 12, 15, 0)));
    model.addEvent(new CalendarEvent("Meeting B", LocalDateTime.of(2025, 6, 13, 11, 0)));

    String script = "query-events 2025-06-12T00:00 2025-06-13T23:59\nexit";
    CalendarController controller = new CalendarController(model, view, new StringReader(script));
    controller.run();

    String output = view.getOutput();
    assertTrue(output.contains("Meeting A"));
    assertTrue(output.contains("Meeting B"));
  }

  @Test
  public void testExitCommandTerminatesScript() {
    String script = "exit\ncreate-event 2025-07-01T08:00 ShouldNotBeCreated";
    CalendarController controller = new CalendarController(model, view, new StringReader(script));
    controller.run();

    assertEquals(0, model.getEventsBetween(
            LocalDateTime.of(2025, 7, 1, 0, 0),
            LocalDateTime.of(2025, 7, 1, 23, 59)
    ).size());

    assertTrue(view.getOutput().toLowerCase().contains("exiting"));
  }

  /**
   * Mock view to capture output.
   */
  private static class MockView implements IView {
    private final StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) throws IOException {
      log.append(message).append("\n");
    }

    public String getOutput() {
      return log.toString();
    }
  }
}