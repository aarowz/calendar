// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

/**
 * A panel for creating a new calendar by specifying a name and timezone.
 */
public class NewCalendarPanel extends JPanel {

  private final JTextField calendarNameField;
  private final JComboBox<String> timezoneDropdown;

  /**
   * Constructs a panel for creating a new calendar.
   */
  public NewCalendarPanel() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBorder(BorderFactory.createTitledBorder("Create New Calendar"));

    this.calendarNameField = new JTextField();
    this.timezoneDropdown = new JComboBox<>();

    this.add(new JLabel("Calendar Name:"));
    this.add(this.calendarNameField);

    this.add(new JLabel("Timezone:"));
    this.add(this.timezoneDropdown);
  }

  /**
   * Populates the timezone dropdown with supported timezones.
   */
  public void loadTimezones() {
    // stub
  }

  /**
   * Returns the entered calendar name.
   *
   * @return the name entered by the user
   */
  public String getCalendarName() {
    return ""; // stub
  }

  /**
   * Returns the selected timezone.
   *
   * @return the selected timezone ID
   */
  public String getSelectedTimezone() {
    return ""; // stub
  }

  /**
   * Clears all input fields.
   */
  public void reset() {
    // stub
  }
}