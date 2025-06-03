// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
/**
 * Unit tests for the CalendarEvent class.
 * Validates immutability, correct field access through getters,
 * all-day event handling, equality and hash code behavior,
 * and the functionality of the with-methods used for editing.
 */
public class CalendarEventTest {
  IEvent meeting = new CalendarEvent.Builder()
          .subject("meeting")
          .start(LocalDateTime.of(2025, 6, 5, 10, 30))
          .end(LocalDateTime.of(2025, 6, 5, 11, 30))
          .description("its a meeting")
          .location("Zoom")
          .status(EventStatus.PRIVATE)
          .build();

  IEvent meeting1 = new CalendarEvent.Builder()
          .subject("meeting1")
          .start(LocalDateTime.of(2025, 6, 5, 10, 30))
          .end(LocalDateTime.of(2025, 6, 5, 12, 30))
          .description("its a meeting")
          .location("Zoom")
          .status(EventStatus.PRIVATE)
          .build();

  IEvent meeting2 = new CalendarEvent.Builder()
          .subject("meeting1")
          .start(LocalDateTime.of(2025, 2, 5, 10, 30))
          .end(LocalDateTime.of(2025, 2, 5, 12, 30))
          .description("its a meeting")
          .location("Zoom")
          .status(EventStatus.PRIVATE)
          .build();

  //make a try catch if there is no subject
  //make a try catch if there is no start
  //make a try catch if they try to add optional data but no required data,
  // //one for subject one for start
  //invalid input for all the fields
  //test same event, same subject and start


  @Test
  public void testGetMethods() {
    assertEquals("meeting", meeting.getSubject());
    assertEquals(LocalDateTime.of(2025, 6, 5, 10, 30), meeting.getStart());
    assertEquals(LocalDateTime.of(2025, 6, 5, 11, 30), meeting.getEnd());
    assertEquals("its a meeting", meeting.getDescription());
    assertEquals(EventStatus.PRIVATE, meeting.getStatus());
    assertEquals("Zoom", meeting.getLocation());
  }

  @Test
  public void testOverlapsWith() {
    assertEquals(true, meeting1.overlapsWith(meeting));
    assertEquals(true, meeting.overlapsWith(meeting1));

    assertEquals(false, meeting1.overlapsWith(meeting2));
    assertEquals(false, meeting2.overlapsWith(meeting1));
  }
}