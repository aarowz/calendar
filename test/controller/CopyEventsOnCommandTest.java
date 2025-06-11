// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import model.IDelegator;
import org.junit.Before;
import org.junit.Test;
import view.IView;

import java.time.LocalDate;

//import static org.mockito.Mockito.*;

/**
 * Unit tests for CopyEventsOnCommand.
 */
public class CopyEventsOnCommandTest {

  private IDelegator mockDelegator;
  private IView mockView;

  @Before
  public void setUp() {
    mockDelegator = mock(IDelegator.class);
    mockView = mock(IView.class);
  }

  /** Tests copying events on a valid date with events present. */
  @Test
  public void testCopyEventsOnWithEvents() {
    // TODO
  }

  /** Tests copying events when the source date has no events. */
  @Test
  public void testCopyEventsOnNoEventsOnDate() {
    // TODO
  }

  /** Tests copying events to a calendar that does not exist. */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventsOnTargetCalendarMissing() {
    // TODO
  }

  /** Tests copying when target date is invalid (null). */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventsOnNullTargetDate() {
    // TODO
  }

  /** Tests copying when source date is null. */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventsOnNullSourceDate() {
    // TODO
  }

  /** Tests copying when no calendar is currently in use. */
  @Test(expected = IllegalStateException.class)
  public void testCopyEventsOnNoCalendarInUse() {
    // TODO
  }

  /** Tests copying to the same calendar (source == target). */
  @Test
  public void testCopyEventsOnSameCalendar() {
    // TODO
  }

  /** Tests that series events are copied and retain their series metadata. */
  @Test
  public void testCopyEventsOnPreservesSeriesId() {
    // TODO
  }

  /** Tests that times are adjusted correctly when copying between timezones. */
  @Test
  public void testCopyEventsOnTimezoneConversion() {
    // TODO
  }

  /** Tests that an error message is rendered to the view on failure. */
  @Test
  public void testCopyEventsOnErrorMessageToView() {
    // TODO
  }

  /** Tests that null or empty calendar name is rejected. */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventsOnNullCalendarName() {
    // TODO
  }
}
