//// Dreshta Boghra & Aaron Zhou
//// CS3500 HW5
//
//package controller;
//
//import model.IDelegator;
//import org.junit.Before;
//import org.junit.Test;
//import view.IView;
//
//import java.time.LocalDateTime;
//
//
///**
// * Unit tests for CopyEventCommand.
// */
//public class CopyEventCommandTest {
//
//  private IDelegator mockDelegator;
//  private IView mockView;
//
//  @Before
//  public void setUp() {
//    mockDelegator = mock(IDelegator.class);
//    mockView = mock(IView.class);
//  }
//
//  /** Tests copying an event when all inputs are valid. */
//  @Test
//  public void testCopyEventSuccess() {
//    // TODO
//  }
//
//  /** Tests copying an event that does not exist in the source calendar. */
//  @Test(expected = IllegalArgumentException.class)
//  public void testCopyEventNameNotFound() {
//    // TODO
//  }
//
//  /** Tests copying an event to a calendar that does not exist. */
//  @Test(expected = IllegalArgumentException.class)
//  public void testCopyEventTargetCalendarNotFound() {
//    // TODO
//  }
//
//  /** Tests event name matches but start time does not match. */
//  @Test(expected = IllegalArgumentException.class)
//  public void testCopyEventTimeMismatch() {
//    // TODO
//  }
//
//  /** Tests copying with a null event name. */
//  @Test(expected = IllegalArgumentException.class)
//  public void testCopyEventNullName() {
//    // TODO
//  }
//
//  /** Tests copying with a null source start time. */
//  @Test(expected = IllegalArgumentException.class)
//  public void testCopyEventNullSourceStart() {
//    // TODO
//  }
//
//  /** Tests copying with a null target start time. */
//  @Test(expected = IllegalArgumentException.class)
//  public void testCopyEventNullTargetStart() {
//    // TODO
//  }
//
//  /** Tests copying with a null target calendar name. */
//  @Test(expected = IllegalArgumentException.class)
//  public void testCopyEventNullTargetCalendar() {
//    // TODO
//  }
//
//  /** Tests that a duplicate conflict in the target calendar is detected. */
//  @Test(expected = IllegalArgumentException.class)
//  public void testCopyEventCreatesDuplicateInTarget() {
//    // TODO
//  }
//
//  /** Tests that an error message is rendered when copying fails. */
//  @Test
//  public void testCopyEventErrorMessageToView() {
//    // TODO
//  }
//
//  /** Tests that series metadata is preserved in the copied event (if applicable). */
//  @Test
//  public void testCopyEventPreservesSeriesId() {
//    // TODO
//  }
//
//  /** Tests that timezone conversion is handled correctly in copy. */
//  @Test
//  public void testCopyEventTimezoneConversion() {
//    // TODO
//  }
//}
