// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package view;

import model.ROIEvent;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.*;

/**
 * Panel that displays a scrollable list of calendar events.
 * Used as the left-hand side of the main calendar GUI layout.
 */
public class ScrollEventsPanel extends JPanel {
  private final JLabel headerLabel;
  private final JTextArea eventArea;

  /**
   * Constructs a scrollable panel to display events.
   */
  public ScrollEventsPanel() {
    this.setLayout(new BorderLayout());

    this.eventArea = new JTextArea();
    this.eventArea.setEditable(false);
    this.eventArea.setLineWrap(true);
    this.eventArea.setWrapStyleWord(true);

    JScrollPane scrollPane = new JScrollPane(this.eventArea);

    this.headerLabel = new JLabel("Displaying up to 10 events from the selected date:");
    this.headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    this.headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(this.headerLabel, BorderLayout.NORTH);

    this.add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * Updates the displayed text to show the given list of events.
   *
   * @param events the list of events to display
   */
  public void displayEvents(List<ROIEvent> events) {
    StringBuilder sb = new StringBuilder();

    if (events.isEmpty()) {
      sb.append("No events scheduled for this day and beyond.");
    } else {
      for (ROIEvent event : events) {
        sb.append(event.toString()).append("\n\n");
      }
    }

    this.eventArea.setText(sb.toString());
    this.eventArea.setCaretPosition(0); // scroll to top
  }

  /**
   * Sets the header for displaying events based on the current start day.
   *
   * @param text the current start day
   */
  public void setHeaderText(String text) {
    this.headerLabel.setText(text);
  }

  /**
   * Clears the displayed event list.
   */
  public void clear() {
    this.eventArea.setText("");
  }
}