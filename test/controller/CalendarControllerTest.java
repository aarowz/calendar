// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.CalendarMulti;
import model.DelegatorImpl;
import model.ICalendar;
import model.IDelegator;
import view.IView;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Stub test class for testing calendar controller functionality.
 * Each test includes a Javadoc describing its intended purpose.
 */
public class CalendarControllerTest {
  private IDelegator model;

  /**
   * A mock view that captures output rendered by the controller.
   */
  private static class MockView implements IView {
    StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) {
      log.append(message).append("\n");
    }

    public String getLog() {
      return log.toString();
    }
  }

  /**
   * Initializes a fresh calendar model before each test.
   */
  @Before
  public void setup() {
    model = new DelegatorImpl(new CalendarMulti());
  }

  /**
   * Tests that the controller uses an interface or abstract class.
   */
  @Test
  public void testControllerInterfaceExists() {
    assertTrue(ICalendar.class.isAssignableFrom(model.getClass()));
  }

  /**
   * Tests whether controller/view are tightly coupled to I/O.
   */
  @Test
  public void testControllerIOCoupling() {
    try {
      String input = "create event Test from 2025-06-06T10:00 to 2025-06-06T11:00\nexit";
      ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
      MockView view = new MockView();

      CalendarController controller = new CalendarController(model, view, in);
      controller.run();

      assertTrue(view.getLog().toLowerCase().contains("created"));
    } catch (Exception e) {
      fail("Controller should not throw with mock I/O");
    }
  }

  /**
   * Tests that the controller parses and executes valid commands correctly.
   */
  @Test
  public void testControllerCommandExecution() {
    String input = "create event Review from 2025-06-07T13:00 to 2025-06-07T14:00\nexit";
    ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    MockView view = new MockView();

    CalendarController controller = new CalendarController(model, view, in);
    controller.run();

    String log = view.getLog().toLowerCase();
    assertTrue(log.contains("review"));
    assertTrue(log.contains("created"));
  }
}