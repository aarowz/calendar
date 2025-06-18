// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A panel for creating a new calendar by specifying a name and timezone.
 */
public class NewCalendarPanel extends JPanel implements INewCalendarPanel {
  private final JTextField calendarNameField;
  private final JComboBox<String> timezoneDropdown;

  /**
   * Constructs a panel for creating a new calendar.
   */
  public NewCalendarPanel() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBorder(BorderFactory.createTitledBorder("Create Calendar"));

    this.calendarNameField = new JTextField();
    this.timezoneDropdown = new JComboBox<>();

    this.add(new JLabel("Calendar Name:"));
    this.add(this.calendarNameField);

    this.add(new JLabel("Timezone:"));
    this.add(this.timezoneDropdown);
  }

  /**
   * Populates the timezone dropdown with supported timezones.
   * By default, loads common zones sorted alphabetically.
   */
  public void loadTimezones() {
    this.timezoneDropdown.removeAllItems();

    // get all available ZoneIds and sort alphabetically
    List<String> zones = ZoneId.getAvailableZoneIds()
            .stream()
            .sorted()
            .collect(Collectors.toList());

    for (String zone : zones) {
      this.timezoneDropdown.addItem(zone);
    }

    // optional: select system default as a friendly default
    String systemDefault = ZoneId.systemDefault().getId();
    this.timezoneDropdown.setSelectedItem(systemDefault);
  }

  /**
   * Returns the entered calendar name.
   *
   * @return the name entered by the user
   */
  public String getCalendarName() {
    return this.calendarNameField.getText().trim();
  }

  /**
   * Returns the selected timezone.
   *
   * @return the selected timezone ID
   */
  public String getSelectedTimezone() {
    Object selected = this.timezoneDropdown.getSelectedItem();
    return selected != null ? selected.toString() : "";
  }

  /**
   * Clears all input fields.
   */
  public void reset() {
    this.calendarNameField.setText("");
    if (this.timezoneDropdown.getItemCount() > 0) {
      // reset to default timezone for convenience
      this.timezoneDropdown.setSelectedItem(ZoneId.systemDefault().getId());
    }
  }
}