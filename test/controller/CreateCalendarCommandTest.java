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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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

  /**
   * Initializes a fresh delegator model and mock view before each test.
   */
  @Before
  public void setup() {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();
  }

  /**
   * Tests creating a calendar with a valid name and timezone.
   */
  @Test
  public void testCreateCalendarValid() throws CommandExecutionException, IOException {
    ZoneId works = ZoneId.of("America/New_York");
    String zoneString = works.toString();
    CreateCalendarCommand cmd = new CreateCalendarCommand("work", zoneString);
    cmd.execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("created"));
  }

  /**
   * Tests that an invalid timezone string causes a CommandExecutionException.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCreateCalendarInvalidZone() throws CommandExecutionException, IOException {
    new CreateCalendarCommand("fail", "Bad/Zone").execute(model, view);
  }

  /**
   * Tests that creating two calendars with the same name causes a CommandExecutionException.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCreateCalendarDuplicateName() throws CommandExecutionException, IOException {
    String zoneString = ZoneId.of("UTC").toString();
    CreateCalendarCommand cmd1 = new CreateCalendarCommand("calendar", zoneString);
    CreateCalendarCommand cmd2 = new CreateCalendarCommand("calendar", zoneString);
    cmd1.execute(model, view);
    cmd2.execute(model, view); // should throw
  }

  /**
   * Tests that providing a null calendar name causes a CommandExecutionException.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCreateCalendarNullName() throws CommandExecutionException, IOException {
    String zoneString = ZoneId.of("UTC").toString();
    new CreateCalendarCommand(null, zoneString).execute(model, view);
  }

  /**
   * Tests that providing an empty calendar name causes a CommandExecutionException.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCreateCalendarEmptyName() throws CommandExecutionException, IOException {
    new CreateCalendarCommand("", "UTC").execute(model, view);
  }

  /**
   * Tests that providing a null timezone string causes a CommandExecutionException.
   */
  @Test(expected = CommandExecutionException.class)
  public void testCreateCalendarNullZone() throws CommandExecutionException, IOException {
    new CreateCalendarCommand("nullzone", null).execute(model, view);
  }

  /**
   * Tests that calendar names are case-sensitive and treated as distinct.
   */
  @Test
  public void testCreateCalendarCaseSensitive() throws CommandExecutionException, IOException {
    String zoneString = ZoneId.of("UTC").toString();
    new CreateCalendarCommand("A", zoneString).execute(model, view);
    new CreateCalendarCommand("a", zoneString).execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("created"));
  }

  /**
   * Tests that multiple calendars can be created without error if their names are unique.
   */
  @Test
  public void testMultipleCalendars() throws CommandExecutionException, IOException {
    String zoneString = ZoneId.of("UTC").toString();
    new CreateCalendarCommand("a", zoneString).execute(model, view);
    new CreateCalendarCommand("b", zoneString).execute(model, view);
    new CreateCalendarCommand("c", zoneString).execute(model, view);
    assertTrue(view.getLog().split("\n").length >= 3);
  }

  /**
   * Tests that a newly created calendar is not automatically selected.
   */
  @Test
  public void testNotAutoSelectedAfterCreate() throws CommandExecutionException, IOException {
    String zoneString = ZoneId.of("UTC").toString();
    new CreateCalendarCommand("solo", zoneString).execute(model, view);
    assertFalse(view.getLog().toLowerCase().contains("using calendar"));
  }

  /**
   * Tests that a CommandExecutionException message is rendered to the view when caught.
   */
  @Test
  public void testErrorRenderedToView() throws IOException {
    CreateCalendarCommand cmd = new CreateCalendarCommand(null, "UTC");
    try {
      cmd.execute(model, view);
    } catch (CommandExecutionException e) {
      view.renderMessage("Error: " + e.getMessage());
    }
    assertTrue(view.getLog().toLowerCase().contains("error"));
  }

  /**
   * Tests that the toString method includes the calendar name.
   */
  @Test
  public void testToStringIncludesName() {
    String zoneString = ZoneId.of("UTC").toString();
    CreateCalendarCommand cmd = new CreateCalendarCommand("summer", zoneString);
    assertTrue(cmd.toString().contains("summer"));
  }
}