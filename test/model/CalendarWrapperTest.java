// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package model;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link CalendarWrapper} class.
 * Verifies correct delegation, construction, and defensive behavior.
 */
public class CalendarWrapperTest {

  private CalendarWrapper wrapper;
  private ICalendar dummyCalendar;

  /**
   * Sets up a basic CalendarWrapper and a dummy calendar for testing.
   */
  @Before
  public void setUp() {
    dummyCalendar = new CalendarModel();
    wrapper = new CalendarWrapper("MyCalendar", ZoneId.of("America/New_York"),
            dummyCalendar);
  }

  /**
   * Tests that the wrapper correctly returns its name.
   */
  @Test
  public void testGetName() {
    assertEquals("MyCalendar", wrapper.getName());
  }

  /**
   * Tests that the wrapper correctly returns its timezone.
   */
  @Test
  public void testGetTimeZone() {
    assertEquals(ZoneId.of("America/New_York"), wrapper.getTimeZone());
  }

  /**
   * Tests that the wrapper returns the calendar it wraps.
   */
  @Test
  public void testGetCalendar() {
    assertSame(dummyCalendar, wrapper.getCalendar());
  }

  /**
   * Tests that the toString method of the wrapper returns the correct format.
   */
  @Test
  public void testToStringFormat() {
    String expected = "Calendar[name=MyCalendar, zone=America/New_York]";
    assertEquals(expected, wrapper.toString());
  }

  /**
   * Tests that multiple instances of CalendarWrapper are independent.
   */
  @Test
  public void testMultipleInstancesAreIndependent() {
    CalendarWrapper other = new CalendarWrapper("Other", ZoneId.of("UTC"), new
            CalendarModel());
    assertNotEquals(wrapper.getName(), other.getName());
    assertNotEquals(wrapper.getTimeZone(), other.getTimeZone());
    assertNotSame(wrapper.getCalendar(), other.getCalendar());
  }

  /**
   * Tests that constructing a wrapper with a null calendar throws an exception.
   */
  @Test(expected = NullPointerException.class)
  public void testNullCalendarThrows() {
    new CalendarWrapper("Bad", ZoneId.of("UTC"),
            null);
  }

  /**
   * Tests that constructing a wrapper with a null zone throws an exception.
   */
  @Test(expected = NullPointerException.class)
  public void testNullZoneThrows() {
    new CalendarWrapper("Bad", null, new
            CalendarModel());
  }

  /**
   * Tests that constructing a wrapper with a null name throws an exception.
   */
  @Test(expected = NullPointerException.class)
  public void testNullNameThrows() {
    new CalendarWrapper(null, ZoneId.of("UTC"), new CalendarModel());
  }
}