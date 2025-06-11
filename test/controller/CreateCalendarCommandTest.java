// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import model.IDelegator;
import org.junit.Before;
import org.junit.Test;
import view.IView;

import java.time.ZoneId;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CreateCalendarCommand.
 */
public class CreateCalendarCommandTest {

  private IDelegator mockDelegator;
  private IView mockView;

  @Before
  public void setUp() {
    mockDelegator = mock(IDelegator.class);
    mockView = mock(IView.class);
  }

  /** Tests successful creation of a calendar with valid name and zone. */
  @Test
  public void testCreateCalendarValid() {
    // TODO
  }

  /** Tests creating a calendar with an invalid timezone string. */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCalendarInvalidZone() {
    // TODO
  }

  /** Tests creating a calendar with a duplicate name. */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCalendarDuplicateName() {
    // TODO
  }

  /** Tests that null calendar name is rejected. */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCalendarNullName() {
    // TODO
  }

  /** Tests that empty calendar name is rejected. */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCalendarEmptyName() {
    // TODO
  }

  /** Tests that a null timezone is rejected. */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCalendarNullZone() {
    // TODO
  }

  /** Tests that names with different casing are treated as unique. */
  @Test
  public void testCreateCalendarCaseSensitiveName() {
    // TODO
  }

  /** Tests that multiple calendars can be created successfully. */
  @Test
  public void testMultipleValidCalendarsCreated() {
    // TODO
  }

  /** Tests that calendar is not automatically selected after creation. */
  @Test
  public void testCalendarNotAutoSelectedAfterCreate() {
    // TODO
  }

  /** Tests that an invalid command string structure throws or fails gracefully. */
  @Test(expected = IllegalArgumentException.class)
  public void testMalformedCommandString() {
    // TODO
  }

  /** Tests that error messages are passed to the view on failure. */
  @Test
  public void testErrorMessageRenderedToView() {
    // TODO
  }

  /** Tests running create calendar in headless (batch) mode using a command file. */
  @Test
  public void testHeadlessCreateCalendarCommand() {
    // TODO
  }


}
