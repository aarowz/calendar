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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Stub test class for testing calendar controller functionality.
 * Each test includes a Javadoc describing its intended purpose.
 */
public class CalendarControllerTest {
  private CalendarModel model;

  /**
   * A mock view that captures output rendered by the controller.
   */
  private static class MockView implements IView {
    StringBuilder log = new StringBuilder();

    @Override
    public void renderMessage(String message) throws IOException {
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
    model = new CalendarModel();
  }

  /**
   * A mock model that records if createEvent was called.
   */
  private static class MockModel implements ICalendar {
    boolean createCalled = false;

    @Override
    public void createEvent(String subject, java.time.LocalDateTime start,
                            java.time.LocalDateTime end, String description,
                            EventStatus status, String location) {
      createCalled = true;
    }

    @Override
    public void createEventSeries(String subject, String description, String location,
                                  EventStatus status, LocalDate startDate, LocalDate endDate,
                                  LocalTime startTime, LocalTime endTime,
                                  Set<DayOfWeek> repeatDays, int count) {
      // stub
    }

    @Override
    public void editEvent(String subject, LocalDateTime originalStart, String newSubject,
                          LocalDateTime newStart, LocalDateTime newEnd, String newDescription,
                          EventStatus newStatus, String newLocation) {
      // stub
    }

    @Override
    public void editEvents(String subject, LocalDateTime originalStart, String newSubject,
                           LocalDateTime newStart, LocalDateTime newEnd, String newDescription,
                           EventStatus newStatus, String newLocation) {
      // stub
    }

    @Override
    public void editEventSeries(String subject, LocalDateTime originalStart, String newSubject,
                                LocalDateTime newStart, LocalDateTime newEnd,
                                String newDescription, EventStatus newStatus,
                                String newLocation) {
      // stub
    }

    @Override
    public List<ROIEvent> getEventsOn(LocalDate date) {
      return null;
    }

    @Override
    public List<ROIEvent> getEventsBetween(LocalDateTime from, LocalDateTime to) {
      return null;
    }

    @Override
    public boolean isBusyAt(LocalDateTime time) {
      return false;
    }

    @Override
    public boolean isDuplicate(String subject, LocalDateTime start, LocalDateTime end) {
      return false;
    }

    // other ICalendar methods can be left unimplemented for this test
  }

  /**
   * A simple mock view that does nothing.
   */
  private static class DummyView implements IView {
    @Override
    public void renderMessage(String message) throws IOException {
      // no-op
    }
  }

  /**
   * Verifies that user input is parsed by the controller and passed to the model.
   */
  @Test
  public void testControllerToModelConnection() {
    String input = "create event Meeting from 2025-06-10T09:00 to 2025-06-10T10:00\nexit\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());

    MockModel model = new MockModel();
    DummyView view = new DummyView();

    CalendarController controller = new CalendarController(model, view, inputStream);
    controller.run();

    assertTrue("createEvent should have been called on the model", model.createCalled);
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