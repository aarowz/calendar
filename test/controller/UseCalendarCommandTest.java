// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import model.IDelegator;
import org.junit.Before;
import org.junit.Test;
import view.IView;


/**
 * Unit tests for UseCalendarCommand.
 */
public class UseCalendarCommandTest {

  private IDelegator mockDelegator;
  private IView mockView;

  @Before
  public void setUp() {
    mockDelegator = mock(IDelegator.class);
    mockView = mock(IView.class);
  }

  /** Tests successfully switching to an existing calendar. */
  @Test
  public void testUseValidCalendar() {
    // TODO
  }

  /** Tests attempting to use a calendar that does not exist. */
  @Test(expected = IllegalArgumentException.class)
  public void testUseNonexistentCalendar() {
    // TODO
  }

  /** Tests using a null calendar name. */
  @Test(expected = IllegalArgumentException.class)
  public void testUseNullCalendarName() {
    // TODO
  }

  /** Tests using an empty string as a calendar name. */
  @Test(expected = IllegalArgumentException.class)
  public void testUseEmptyCalendarName() {
    // TODO
  }

  /** Tests that calendar names are case-sensitive. */
  @Test(expected = IllegalArgumentException.class)
  public void testUseCalendarCaseSensitivity() {
    // TODO
  }


}
