// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import exceptions.CommandExecutionException;
import model.CalendarMulti;
import model.DelegatorImpl;
import model.IDelegator;
import org.junit.Before;
import org.junit.Test;
import view.IView;

import java.io.IOException;
import java.time.ZoneId;

import static org.junit.Assert.*;

/**
 * Test class for the CreateCalendarCommand.
 * Verifies that calendars are created with correct names and timezones,
 * while invalid inputs and error cases are handled properly.
 */
public class CreateCalendarCommandTest {

  private IDelegator model;
  private MockView view;

  /**
   * A simple mock view that logs messages for testing.
   */
  private static class MockView implements IView {
    private final StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) throws IOException {
      log.append(message).append("\n");
    }

    public String getLog() {
      return log.toString();
    }
  }

  @Before
  public void setup() {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();
  }

  @Test
  public void testCreateCalendarValid() throws CommandExecutionException, IOException {
    ZoneId works = ZoneId.of("America/New_York"); // should throw error internally
    String zoneString = works.toString();
    CreateCalendarCommand cmd = new CreateCalendarCommand("work", zoneString);
    cmd.execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("created"));
  }

  @Test(expected = CommandExecutionException.class)
  public void testCreateCalendarInvalidZone() throws CommandExecutionException, IOException {
    ZoneId bad = ZoneId.of("Bad/Zone"); // should throw error internally
    String zoneString = bad.toString();
    new CreateCalendarCommand("fail", zoneString).execute(model, view);
  }

  @Test(expected = CommandExecutionException.class)
  public void testCreateCalendarDuplicateName() throws CommandExecutionException, IOException {
    ZoneId zone1 = ZoneId.of("UTC"); // should throw error internally
    String zoneString = zone1.toString();
    CreateCalendarCommand cmd1 = new CreateCalendarCommand("calendar", zoneString);
    CreateCalendarCommand cmd2 = new CreateCalendarCommand("calendar", zoneString);
    cmd1.execute(model, view);
    cmd2.execute(model, view); // should throw
  }

  @Test(expected = CommandExecutionException.class)
  public void testCreateCalendarNullName() throws CommandExecutionException, IOException {
    ZoneId zone1 = ZoneId.of("UTC"); // should throw error internally
    String zoneString = zone1.toString();
    new CreateCalendarCommand(null, zoneString).execute(model, view);
  }

  @Test(expected = CommandExecutionException.class)
  public void testCreateCalendarEmptyName() throws CommandExecutionException, IOException {
    ZoneId zone1 = ZoneId.of("UTC"); // should throw error internally
    String zoneString = zone1.toString();
    new CreateCalendarCommand("", zoneString).execute(model, view);
  }

  @Test(expected = CommandExecutionException.class)
  public void testCreateCalendarNullZone() throws CommandExecutionException, IOException {
    new CreateCalendarCommand("nullzone", null).execute(model, view);
  }

  @Test
  public void testCreateCalendarCaseSensitive() throws CommandExecutionException, IOException {
    ZoneId zone1 = ZoneId.of("UTC"); // should throw error internally
    String zoneString = zone1.toString();
    new CreateCalendarCommand("A", zoneString).execute(model, view);
    new CreateCalendarCommand("a", zoneString).execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("created"));
  }

  @Test
  public void testMultipleCalendars() throws CommandExecutionException, IOException {
    ZoneId zone1 = ZoneId.of("UTC"); // should throw error internally
    String zoneString1 = zone1.toString();
    ZoneId zone2 = ZoneId.of("America/Chicago"); // should throw error internally
    String zoneString2 = zone1.toString();
    ZoneId zone3 = ZoneId.of("Europe/London"); // should throw error internally
    String zoneString3 = zone1.toString();
    new CreateCalendarCommand("a", zoneString1).execute(model, view);
    new CreateCalendarCommand("b", zoneString2).execute(model, view);
    new CreateCalendarCommand("c", zoneString3).execute(model, view);
    assertTrue(view.getLog().split("\n").length >= 3);
  }

  @Test
  public void testNotAutoSelectedAfterCreate() throws CommandExecutionException, IOException {
    ZoneId zone1 = ZoneId.of("UTC"); // should throw error internally
    String zoneString1 = zone1.toString();
    new CreateCalendarCommand("solo", zoneString1).execute(model, view);
    assertFalse(view.getLog().toLowerCase().contains("using calendar"));
  }

  @Test
  public void testErrorRenderedToView() throws IOException {
    ZoneId zone1 = ZoneId.of("UTC"); // should throw error internally
    String zoneString1 = zone1.toString();
    CreateCalendarCommand cmd = new CreateCalendarCommand(null, zoneString1);
    try {
      cmd.execute(model, view);
    } catch (CommandExecutionException ignored) {
    }
    assertTrue(view.getLog().toLowerCase().contains("error"));
  }

  @Test
  public void testToStringIncludesName() {
    ZoneId zone1 = ZoneId.of("UTC"); // should throw error internally
    String zoneString1 = zone1.toString();
    CreateCalendarCommand cmd = new CreateCalendarCommand("summer", zoneString1);
    assertTrue(cmd.toString().contains("summer"));
  }
}
