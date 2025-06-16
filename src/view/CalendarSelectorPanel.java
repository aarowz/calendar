// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package view;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import java.util.List;

/**
 * A panel for selecting an existing calendar from a list of calendar names.
 */
public class CalendarSelectorPanel extends JPanel {

  private final JComboBox<String> calendarDropdown;

  /**
   * Constructs a panel for selecting from available calendars.
   */
  public CalendarSelectorPanel() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBorder(BorderFactory.createTitledBorder("Switch Calendar"));

    this.calendarDropdown = new JComboBox<>();
    this.add(new JLabel("Choose calendar:"));
    this.add(this.calendarDropdown);
  }

  /**
   * Loads the given list of calendar names into the dropdown.
   *
   * @param calendarNames the list of calendar names
   */
  public void loadCalendars(List<String> calendarNames) {
    // stub
  }

  /**
   * Returns the name of the selected calendar.
   *
   * @return the selected calendar name
   */
  public String getSelectedCalendar() {
    return ""; // stub
  }

  /**
   * Clears the calendar selection dropdown.
   */
  public void reset() {
    // stub
  }
}