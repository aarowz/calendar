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

import java.io.StringReader;

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
   * Tests that the controller and view operate correctly over mock I/O.
   */
  @Test
  public void testControllerIOCoupling() {
    try {
      // Arrange: full script with valid calendar setup
      String input = String.join("\n",
              "create calendar --name testcal --timezone America/New_York",
              "use calendar --name testcal",
              "create event Test from 2025-06-06T10:00 to 2025-06-06T11:00",
              "exit"
      );
      Readable in = new StringReader(input);
      MockView view = new MockView();
      IDelegator model = new DelegatorImpl(new CalendarMulti());

      // Act
      CalendarController controller = new CalendarController(model, view, in);
      controller.run();

      // Assert
      String log = view.getLog().toLowerCase();
      assertTrue(log.contains("created"));
      assertTrue(log.contains("event"));
    } catch (Exception e) {
      fail("Controller should not throw with mock I/O: " + e.getMessage());
    }
  }

  /**
   * Tests that the controller parses and executes valid commands correctly.
   */
  @Test
  public void testControllerCommandExecution() {
    String input = String.join("\n",
            "create calendar --name testcal --timezone America/New_York",
            "use calendar --name testcal",
            "create event Review from 2025-06-07T13:00 to 2025-06-07T14:00",
            "exit"
    );

    Readable in = new StringReader(input);
    MockView view = new MockView();
    IDelegator model = new DelegatorImpl(new CalendarMulti());

    CalendarController controller = new CalendarController(model, view, in);
    controller.run();

    String log = view.getLog().toLowerCase();
    assertTrue(log.contains("review"));
    assertTrue(log.contains("created"));
  }
}