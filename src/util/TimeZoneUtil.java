// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package util;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import model.CalendarEvent;
import model.IEvent;

/**
 * Utility class for timezone validation and conversions between timezones.
 */
public class TimeZoneUtil {

  /**
   * Converts a local date time to a zone date time because our single calendar classes use a local
   * date time, while all of our classes that deal with multiple calendars use zone date time.
   *
   * @param ldt  the given local date time
   * @param zone the given zone date time
   * @return the converted zone date time
   */
  public static ZonedDateTime toZonedDateTime(LocalDateTime ldt, ZoneId zone) {
    return ldt.atZone(zone);
  }

  /**
   * Converts a zone date time to a local date time because our single calendar classes use a local
   * date time, while all of our classes that deal with multiple calendars use zone date time.
   *
   * @param zdt the given local date time
   * @return the converted local date time
   */
  public static LocalDateTime toLocalDateTime(ZonedDateTime zdt) {
    return zdt.toLocalDateTime();
  }

  /**
   * Converts a list of events to a different timezone.
   * Adjusts event start and end times to represent the same instant in the new zone.
   *
   * @param events   list of source events (with LocalDateTime times)
   * @param fromZone the original zone of the events
   * @param toZone   the target timezone
   * @return a list of copied events with adjusted times
   */
  public static List<IEvent> convertEventsToZone(List<IEvent> events,
                                                 ZoneId fromZone,
                                                 ZoneId toZone) {
    List<IEvent> newZoneEventsList = new ArrayList<>();

    for (IEvent event : events) {
      LocalDateTime originalStart = event.getStart();
      LocalDateTime originalEnd = event.getEnd();

      ZonedDateTime startZoned = originalStart.atZone(fromZone).withZoneSameInstant(toZone);
      ZonedDateTime endZoned = originalEnd.atZone(fromZone).withZoneSameInstant(toZone);

      LocalDateTime newStart = startZoned.toLocalDateTime();
      LocalDateTime newEnd = endZoned.toLocalDateTime();

      IEvent convertedEvent = copyWithNewTimes(event, newStart, newEnd);
      newZoneEventsList.add(convertedEvent);
    }

    return newZoneEventsList;
  }


  /**
   * Checks if a given string is a valid IANA time zone.
   *
   * @param zoneId the string to validate as a timezone
   * @return true if the zoneId is a valid IANA time zone, false otherwise
   */
  public static boolean isValidZone(String zoneId) {
    try {
      ZoneId.of(zoneId);
      return true;
    } catch (DateTimeException e) {
      return false;
    }
  }

  /**
   * Creates a new IEvent based on an existing one, but with updated start and end times.
   *
   * @param original the original event to copy
   * @param newStart the new start time
   * @param newEnd   the new end time
   * @return a new IEvent with updated times and identical other fields
   */
  public static IEvent copyWithNewTimes(IEvent original, LocalDateTime newStart, LocalDateTime
          newEnd) {
    return new CalendarEvent.Builder()
            .subject(original.getSubject())
            .start(newStart)
            .end(newEnd)
            .description(original.getDescription())
            .location(original.getLocation())
            .status(original.getStatus())
            .seriesId(original.getSeriesId())
            .build();
  }

}