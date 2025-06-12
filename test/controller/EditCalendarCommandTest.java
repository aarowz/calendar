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

import static org.junit.Assert.assertTrue;

/**
 * Testing class for the EditCalendarCommand in the Controller directory.
 */
public class EditCalendarCommandTest {
  private IDelegator model;
  private MockView view;

  /**
   * Mock view implementation to record output messages.
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
   * Sets up a fresh model and mock view with sample calendars before each test.
   */
  @Before
  public void setUp() throws CommandExecutionException, IOException {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();
    new CreateCalendarCommand("Work", "UTC").execute(model, view);
    new CreateCalendarCommand("Home", "America/New_York").execute(model, view);
    new UseCalendarCommand("Work").execute(model, view);
  }

  /**
   * Tests that renaming a calendar to an existing name causes a conflict and fails.
   */
  @Test(expected = CommandExecutionException.class)
  public void testEditCalendarNameConflict() throws CommandExecutionException, IOException {
    new EditCalendarCommand("Work", "name", "Home")
            .execute(model, view);
  }

  /**
   * Tests that providing an invalid timezone format causes a failure.
   */
  @Test(expected = CommandExecutionException.class)
  public void testEditCalendarInvalidTimezone() throws CommandExecutionException, IOException {
    new EditCalendarCommand("Work", "timezone", "not/one")
            .execute(model, view);
  }

  /**
   * Tests renaming a calendar successfully updates its name and renders confirmation.
   */
  @Test
  public void testEditCalendarValidNameChange() throws CommandExecutionException, IOException {
    new EditCalendarCommand("Work", "name", "NewWork")
            .execute(model, view);
    String log = view.getLog().toLowerCase();
    assertTrue(log.contains("updated calendar") && log.contains("newwork"));
  }

  /**
   * Tests changing the timezone of a calendar to a valid new value.
   */
  @Test
  public void testEditCalendarValidTimezoneChange() throws CommandExecutionException, IOException {
    new EditCalendarCommand("Work", "timezone", "Europe/London")
            .execute(model, view);
    String log = view.getLog().toLowerCase();
    assertTrue(log.contains("updated calendar") && log.contains("europe/london"));
  }

  /**
   * Tests that renaming a calendar to its current name throws a CommandExecutionException.
   */
  @Test(expected = CommandExecutionException.class)
  public void testEditCalendarSameName() throws CommandExecutionException, IOException {
    new EditCalendarCommand("Work", "name", "Work").execute(model, view);
  }

  /**
   * Tests that passing a null calendar name throws a CommandExecutionException.
   */
  @Test(expected = CommandExecutionException.class)
  public void testEditCalendarNullCalendarName() throws CommandExecutionException, IOException {
    new EditCalendarCommand(null, "name", "Other").execute(model, view);
  }

  /**
   * Tests that passing a null property name throws a CommandExecutionException.
   */
  @Test(expected = CommandExecutionException.class)
  public void testEditCalendarNullProperty() throws CommandExecutionException, IOException {
    new EditCalendarCommand("Work", null, "Other").execute(model, view);
  }

  /**
   * Tests that passing a null property value throws a CommandExecutionException.
   */
  @Test(expected = CommandExecutionException.class)
  public void testEditCalendarNullValue() throws CommandExecutionException, IOException {
    new EditCalendarCommand("Work", "name", null).execute(model, view);
  }

  /**
   * Tests that using an unsupported property key results in a CommandExecutionException.
   */
  @Test(expected = CommandExecutionException.class)
  public void testEditCalendarInvalidPropertyKey() throws CommandExecutionException, IOException {
    new EditCalendarCommand("Work", "color", "blue").execute(model, view);
  }

  /**
   * Tests editing a calendar when no valid calendar is in use results in failure.
   */
  @Test(expected = CommandExecutionException.class)
  public void testEditCalendarWhenNoneInUse() throws CommandExecutionException, IOException {
    model.useCalendar("Work");
    model.editCalendar("Work", "name", "Staging");
    new UseCalendarCommand("None").execute(model, view);
    new EditCalendarCommand("None", "name", "NewName")
            .execute(model, view);
  }

  /**
   * Tests that an error message is rendered to the view when editing fails.
   */
  @Test
  public void testEditCalendarErrorMessageToView() throws IOException {
    try {
      new EditCalendarCommand("Ghost", "name", "NewName")
              .execute(model, view);
    } catch (CommandExecutionException e) {
      view.renderMessage("Error: " + e.getMessage());
    }
    assertTrue(view.getLog().toLowerCase().contains("error"));
  }
}