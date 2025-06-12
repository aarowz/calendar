// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package model;

/**
 * Top-level model interface that exposes all calendar and system operations.

 * IDelegator acts as the main way for the controller to create, edit, copy, and query events,
 * without needing to know how the calendar system works inside. So, the controller communicates
 * exclusively with this interface.
 */
public interface IDelegator extends ICalendar, ICalendarMulti {
}