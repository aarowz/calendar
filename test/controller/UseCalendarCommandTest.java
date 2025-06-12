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
 * Test class for the UseCalendarCommand.
 * Verifies that calendars can be selected successfully,
 * and that invalid or malformed names are handled properly.
 */
public class UseCalendarCommandTest {
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
   * Set up two calendars for all test cases.
   */
  @Before
  public void setup() throws CommandExecutionException, IOException {
    model = new DelegatorImpl(new CalendarMulti());
    view = new MockView();
    new CreateCalendarCommand("Work", "UTC").execute(model, view);
    new CreateCalendarCommand("Personal", "America/New_York").execute(model, view);
  }

  /**
   * Tests switching to a valid existing calendar.
   */
  @Test
  public void testUseValidCalendar() throws CommandExecutionException, IOException {
    UseCalendarCommand cmd = new UseCalendarCommand("Work");
    cmd.execute(model, view);
    assertTrue(view.getLog().toLowerCase().contains("using calendar"));
  }

  /**
   * Tests using a calendar that does not exist.
   */
  @Test(expected = CommandExecutionException.class)
  public void testUseNonexistentCalendar() throws CommandExecutionException, IOException {
    new UseCalendarCommand("Nonexistent").execute(model, view);
  }

  /**
   * Tests using a null calendar name.
   */
  @Test(expected = CommandExecutionException.class)
  public void testUseNullCalendarName() throws CommandExecutionException, IOException {
    new UseCalendarCommand(null).execute(model, view);
  }

  /**
   * Tests using an empty string as the calendar name.
   */
  @Test(expected = CommandExecutionException.class)
  public void testUseEmptyCalendarName() throws CommandExecutionException, IOException {
    new UseCalendarCommand("").execute(model, view);
  }

  /**
   * Tests cases when selecting calendars. Work does not equal work.
   */
  @Test(expected = CommandExecutionException.class)
  public void testUseCalendarCaseSensitivity() throws CommandExecutionException, IOException {
    new UseCalendarCommand("work").execute(model, view);
  }

  /**
   * Tests switching between two valid calendars.
   */
  @Test
  public void testSwitchBetweenCalendars() throws CommandExecutionException, IOException {
    // Act: switch between calendars
    new UseCalendarCommand("Work").execute(model, view);
    new UseCalendarCommand("Personal").execute(model, view);

    // Assert: log confirms each switch
    String log = view.getLog();
    assertTrue(log.contains("Now using calendar 'Work'"));
    assertTrue(log.contains("Now using calendar 'Personal'"));
  }

  /**
   * Verifies toString includes calendar name.
   */
  @Test
  public void testToStringContainsName() {
    UseCalendarCommand cmd = new UseCalendarCommand("Gym");
    assertTrue(cmd.toString().contains("Gym"));
  }

  /**
   * Tests that an IOException in the view is handled properly during use calendar.
   */
  @Test(expected = CommandExecutionException.class)
  public void testUseCalendarViewThrowsIOException() throws CommandExecutionException,
          IOException {
    //always throws IOException
    IView failingView = new IView() {
      @Override
      public void renderMessage(String message) throws IOException {
        throw new IOException("Simulated failure");
      }
    };
    new UseCalendarCommand("Work").execute(model, failingView);
  }
}