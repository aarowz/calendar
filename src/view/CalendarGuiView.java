// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.ICalendarGuiFeatures;
import model.ROIEvent;

/**
 * The graphical user interface for the calendar application.
 * This class manages the main application window and its layout.
 */
public class CalendarGuiView extends JFrame implements ICalendarGuiView{
  private final ScrollEventsPanel scrollEventsPanel;
  private final ButtonPanel buttonPanel;

  /**
   * Constructs the main GUI window for the calendar application.
   * Sets up the layout with the schedule panel on the left and control panel on the right.
   */
  public CalendarGuiView() {
    super("Calendar App");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    this.scrollEventsPanel = new ScrollEventsPanel();
    this.buttonPanel = new ButtonPanel();

    this.add(this.scrollEventsPanel, BorderLayout.CENTER);
    this.add(this.buttonPanel, BorderLayout.EAST);

    this.setPreferredSize(new Dimension(800, 500));
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  /**
   * Attaches controller features to all interactive components in the view.
   *
   * @param features the controller features
   */
  public void setFeatures(ICalendarGuiFeatures features) {
    this.buttonPanel.setFeatures(features);
  }

  /**
   * Displays the given list of events in the scrollable event panel.
   *
   * @param events the events to display
   */
  public void displayEvents(List<ROIEvent> events) {
    this.scrollEventsPanel.displayEvents(events);
  }

  /**
   * Displays an error message dialog.
   *
   * @param message the error message to show
   */
  public void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error",
            JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Updates the header text in the event view panel.
   *
   * @param text the message to display above the event list
   */
  public void setHeaderText(String text) {
    this.scrollEventsPanel.setHeaderText(text);
  }

  /**
   * Gets the button panel within the view.
   *
   * @return the button panel
   */
  public ButtonPanel getButtonPanel() {
    return this.buttonPanel;
  }
}