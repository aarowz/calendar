// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package controller;

import model.CalendarEvent;
import model.EventStatus;
import model.ICalendar;
import model.IDelegator;
import model.IEvent;
import model.ROIEvent;
import view.CalendarGuiView;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;


/**
 * Proper test for CalendarGuiController based on actual methods.
 */
public class CalendarGuiControllerTest {
  private CalendarGuiController controller;
  private StringBuilder log;


  /**
   * Mock model to log all method calls for testing.
   */
  private static class MockDelegator implements IDelegator {
    final StringBuilder log;

    MockDelegator(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void createEvent(String subject, LocalDateTime start, LocalDateTime end,
                            String description, EventStatus status, String location) {
      log.append("Model: createEvent called with subject=").append(subject).append("\n");
    }

    @Override
    public void createEventSeries(String subject, String description, String location,
                                  EventStatus status, LocalDate startDate, LocalDate endDate,
                                  LocalTime startTime, LocalTime endTime,
                                  Set<DayOfWeek> repeatDays, int count) {
      log.append("Model: createEventSeries called with subject=").append(subject).append("\n");
    }

    @Override
    public void editEvent(String subject, LocalDateTime originalStart, String newSubject,
                          LocalDateTime newStart, LocalDateTime newEnd, String newDescription,
                          EventStatus newStatus, String newLocation) {
      log.append("Model: editEvent called with subject=").append(subject).append("\n");
    }

    @Override
    public void editEvents(String subject, LocalDateTime originalStart, String newSubject,
                           LocalDateTime newStart, LocalDateTime newEnd, String newDescription,
                           EventStatus newStatus, String newLocation) {
      log.append("Model: editEvents called with subject=").append(subject).append("\n");
    }

    @Override
    public void editEventSeries(String subject, LocalDateTime originalStart, String newSubject,
                                LocalDateTime newStart, LocalDateTime newEnd, String newDescription,
                                EventStatus newStatus, String newLocation) {
      log.append("Model: editEventSeries called with subject=").append(subject).append("\n");
    }

    @Override
    public List<ROIEvent> getEventsOn(LocalDate date) {
      log.append("Model: getEventsOn called for date=").append(date).append("\n");
      return List.of();
    }

    @Override
    public List<ROIEvent> getEventsBetween(LocalDateTime start, LocalDateTime end) {
      log.append("Model: getEventsBetween called\n");
      ROIEvent event = new CalendarEvent.Builder()
              .subject("Mock Event")
              .start(start)
              .end(start.plusHours(1))
              .description("Test description")
              .status(EventStatus.PUBLIC)
              .location("Mock Location")
              .build();
      return Collections.singletonList(event);
    }

    @Override
    public boolean isBusyAt(LocalDateTime time) {
      log.append("Model: isBusyAt called for time=").append(time).append("\n");
      return false;
    }

    @Override
    public boolean isDuplicate(String subject, LocalDateTime start, LocalDateTime end) {
      log.append("Model: isDuplicate called for subject=").append(subject).append("\n");
      return false;
    }

    @Override
    public IEvent getSpecificEvent(String subject, LocalDateTime start) {
      log.append("Model: getSpecificEvent called for subject=").append(subject).append("\n");
      return null;
    }

    @Override
    public void addEvent(IEvent event) {
      log.append("Model: addEvent called\n");
    }

    @Override
    public void createCalendar(String name, ZoneId timezone) {
      log.append("Model: createCalendar called for name=").append(name).append("\n");
    }

    @Override
    public void editCalendar(String name, String property, String newValue) {
      log.append("Model: editCalendar called for name=").append(name).append("\n");
    }

    @Override
    public void useCalendar(String name) {
      log.append("Model: useCalendar called for name=").append(name).append("\n");
    }

    @Override
    public ICalendar getCurrentCalendar() {
      log.append("Model: getCurrentCalendar called\n");
      return null;
    }

    @Override
    public void copyEvent(String eventName, LocalDateTime sourceStart, String targetCalendar,
                          LocalDateTime targetStart) {
      log.append("Model: copyEvent called for event=").append(eventName).append("\n");
    }

    @Override
    public void copyEventsOn(LocalDate date, String targetCalendar, LocalDate targetDate) {
      log.append("Model: copyEventsOn called for date=").append(date).append("\n");
    }

    @Override
    public void copyEventsBetween(LocalDate start, LocalDate end, String targetCalendar,
                                  LocalDate targetStartDate) {
      log.append("Model: copyEventsBetween called for start=").append(start)
              .append(", end=").append(end).append("\n");
    }

    @Override
    public List<String> getCalendarNames() {
      log.append("Model: getCalendarNames called\n");
      return List.of("MockCalendar1", "MockCalendar2");
    }
  }

  /**
   * Mock view that logs calls.
   */
  private static class MockView extends CalendarGuiView {
    final StringBuilder log;

    MockView(StringBuilder log) {
      super(); // No need to render a real window.
      this.log = log;
      this.setVisible(false);
    }

    @Override
    public void displayEvents(List<ROIEvent> events) {
      log.append("View: displayEvents called with ").append(events.size()).append(" events\n");
    }

    @Override
    public void showError(String message) {
      log.append("View: showError -> ").append(message).append("\n");
    }

    @Override
    public void setHeaderText(String text) {
      log.append("View: setHeaderText -> ").append(text).append("\n");
    }
  }

  @Before
  public void setup() {
    log = new StringBuilder();
    MockDelegator mockModel = new MockDelegator(log);
    MockView mockView = new MockView(log);
    controller = new CalendarGuiController(mockModel, mockView);
  }

  /**
   * Tests that calling handleViewSchedule results in a call to the model's
   * getEventsBetween method and updates the view accordingly.
   */
  @Test
  public void testHandleViewSchedule() {
    controller.handleViewSchedule();
    String output = log.toString();
    assertTrue(output.contains("Model: getEventsBetween"));
    assertTrue(output.contains("View: displayEvents"));
    assertTrue(output.contains("View: setHeaderText"));
  }

  /**
   * Tests that calling handleAddEvent does not immediately trigger any model changes.
   * It should only display a modal dialog and prepare the event form.
   */
  @Test
  public void testHandleAddEvent() {
    controller.handleAddEvent();
    String output = log.toString();

    assertFalse("Should NOT call model.createEvent immediately",
            output.contains("Model: createEvent"));
    assertFalse("Should NOT call model.editEvent immediately",
            output.contains("Model: editEvent"));

  }

  /**
   * Tests that handleEditEvent opens the dialog but does not make immediate changes to the model.
   */
  @Test
  public void testHandleEditEvent() {
    controller.handleEditEvent();
    String output = log.toString();

    assertFalse("Should NOT call model.editEvent immediately",
            output.contains("Model: editEvent"));
  }

  /**
   * Tests that handleSwitchCalendar calls the model to retrieve available calendar names.
   */
  @Test
  public void testHandleSwitchCalendar() {
    controller.handleSwitchCalendar();
    String output = log.toString();
    assertTrue("Should query model for available calendar names",
            output.contains("Model: getCalendarNames"));
  }

  /**
   * Tests that handleCreateCalendar executes safely without throwing an exception.
   * This test validates the method is wired and launches without runtime issues.
   */
  @Test
  public void testHandleCreateCalendar() {
    try {
      controller.handleCreateCalendar();
      System.out.println("handleCreateCalendar executed without exception.");
    } catch (Exception e) {
      System.out.println("handleCreateCalendar threw an exception: " + e.getMessage());
      fail("handleCreateCalendar should not throw an exception");
    }
  }

  /**
   * Tests that refreshSchedule triggers a call to the model's getEventsBetween
   * and updates the view through displayEvents and setHeaderText.
   */
  @Test
  public void testRefreshSchedule() {
    controller.refreshSchedule();
    String output = log.toString();
    assertTrue("Model.getEventsBetween should be called",
            output.contains("Model: getEventsBetween"));
    assertTrue("View.displayEvents should be called",
            output.contains("View: displayEvents"));
    assertTrue("View.setHeaderText should be called",
            output.contains("View: setHeaderText"));
  }
}