// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package model;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;

import static org.junit.Assert.*;

/**
 * Testing class for the CalendarWrapper class in the Model directory.
 */
public class CalendarWrapperTest {

  private CalendarWrapper wrapper;
  private ICalendar dummyCalendar;

  @Before
  public void setUp() {
    dummyCalendar = new CalendarModel();
    wrapper = new CalendarWrapper("MyCalendar", ZoneId.of("America/New_York"), dummyCalendar);
  }

  @Test
  public void testGetName() {
    assertEquals("MyCalendar", wrapper.getName());
  }

  @Test
  public void testGetTimeZone() {
    assertEquals(ZoneId.of("America/New_York"), wrapper.getTimeZone());
  }

  @Test
  public void testGetCalendar() {
    assertSame(dummyCalendar, wrapper.getCalendar());
  }

  @Test
  public void testToStringFormat() {
    String expected = "Calendar[name=MyCalendar, zone=America/New_York]";
    assertEquals(expected, wrapper.toString());
  }

  @Test
  public void testMultipleInstancesAreIndependent() {
    CalendarWrapper other = new CalendarWrapper("Other", ZoneId.of("UTC"), new CalendarModel());
    assertNotEquals(wrapper.getName(), other.getName());
    assertNotEquals(wrapper.getTimeZone(), other.getTimeZone());
    assertNotSame(wrapper.getCalendar(), other.getCalendar());
  }

  @Test(expected = NullPointerException.class)
  public void testNullCalendarThrows() {
    new CalendarWrapper("Bad", ZoneId.of("UTC"), null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullZoneThrows() {
    new CalendarWrapper("Bad", null, new CalendarModel());
  }

  @Test(expected = NullPointerException.class)
  public void testNullNameThrows() {
    new CalendarWrapper(null, ZoneId.of("UTC"), new CalendarModel());
  }
}
