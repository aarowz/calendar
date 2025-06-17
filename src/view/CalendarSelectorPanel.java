// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
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
    this.calendarDropdown.removeAllItems(); // Clear existing items

    if (calendarNames != null) {
      for (String name : calendarNames) {
        this.calendarDropdown.addItem(name);
      }
    }

    // Optional: select the first calendar by default if any exist
    if (this.calendarDropdown.getItemCount() > 0) {
      this.calendarDropdown.setSelectedIndex(0);
    }
  }

  /**
   * Returns the name of the selected calendar.
   *
   * @return the selected calendar name
   */
  public String getSelectedCalendar() {
    Object selected = this.calendarDropdown.getSelectedItem();
    return selected != null ? selected.toString() : "";
  }

  /**
   * Clears the calendar selection dropdown.
   */
  public void reset() {
    this.calendarDropdown.removeAllItems();
  }
}