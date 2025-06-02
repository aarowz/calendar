// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.CalendarEvent;
import model.CalendarModel;

import org.junit.Before;
import org.junit.Test;

import view.IView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for QueryEventsCommand.
 */
public class QueryEventsCommandTest {

  private CalendarModel model;
  private MockView view;
  private List<CalendarEvent> sampleEvents;

  @Before
  public void setUp() {
    model = new CalendarModel();
    view = new MockView();
    sampleEvents = Arrays.asList(
            new CalendarEvent("Meeting", LocalDateTime.of(2025, 6, 10, 10, 0)),
            new CalendarEvent("Lunch", LocalDateTime.of(2025, 6, 11, 12, 0))
    );
    for (CalendarEvent e : sampleEvents) {
      model.addEvent(e);
    }
  }

  @Test
  public void testQueryWithResults() {
    ICommand cmd = new QueryEventsCommand(
            LocalDateTime.of(2025, 6, 9, 0, 0),
            LocalDateTime.of(2025, 6, 12, 0, 0)
    );
    cmd.execute(model, view);

    String output = view.getOutput();
    assertTrue(output.contains("Meeting"));
    assertTrue(output.contains("Lunch"));
  }

  @Test
  public void testQueryNoResults() {
    ICommand cmd = new QueryEventsCommand(
            LocalDateTime.of(2025, 7, 1, 0, 0),
            LocalDateTime.of(2025, 7, 5, 0, 0)
    );
    cmd.execute(model, view);

    String output = view.getOutput();
    assertTrue(output.toLowerCase().contains("no events"));
  }

  @Test
  public void testQueryWithExactMatch() {
    ICommand cmd = new QueryEventsCommand(
            LocalDateTime.of(2025, 6, 10, 10, 0),
            LocalDateTime.of(2025, 6, 10, 10, 0)
    );
    cmd.execute(model, view);

    String output = view.getOutput();
    assertTrue(output.contains("Meeting"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testQueryWithNullModel() {
    ICommand cmd = new QueryEventsCommand(
            LocalDateTime.of(2025, 6, 10, 10, 0),
            LocalDateTime.of(2025, 6, 10, 10, 0)
    );
    cmd.execute(null, view);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testQueryWithNullView() {
    ICommand cmd = new QueryEventsCommand(
            LocalDateTime.of(2025, 6, 10, 10, 0),
            LocalDateTime.of(2025, 6, 10, 10, 0)
    );
    cmd.execute(model, null);
  }

  /**
   * Mock view that captures messages sent to it.
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