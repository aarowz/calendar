// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package view;

import controller.ICalendarGuiFeatures;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

/**
 * Panel that displays the control buttons on the right side of the GUI.
 * Includes buttons for viewing, adding, editing events, and switching calendars.
 */
public class ButtonPanel extends JPanel {

  private final JButton viewScheduleButton;
  private final JButton addEventButton;
  private final JButton editEventButton;
  private final JButton switchCalendarButton;

  /**
   * Constructs the control button panel.
   * Buttons are laid out vertically with spacing between them.
   */
  public ButtonPanel() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.viewScheduleButton = new JButton("View Schedule");
    this.addEventButton = new JButton("Add Event");
    this.editEventButton = new JButton("Edit Event");
    this.switchCalendarButton = new JButton("Switch Calendar");

    this.add(spaced(this.viewScheduleButton));
    this.add(spaced(this.addEventButton));
    this.add(spaced(this.editEventButton));
    this.add(spaced(this.switchCalendarButton));
  }

  /**
   * Attaches the given feature callbacks to each button.
   *
   * @param features the controller features to attach
   */
  public void setFeatures(ICalendarGuiFeatures features) {
    this.viewScheduleButton.addActionListener(e -> features.handleViewSchedule());
    this.addEventButton.addActionListener(e -> features.handleAddEvent());
    this.editEventButton.addActionListener(e -> features.handleEditEvent());
    this.switchCalendarButton.addActionListener(e -> features.handleSwitchCalendar());
  }

  /**
   * Adds vertical spacing around a button.
   *
   * @param button the button to wrap with spacing
   * @return a vertically spaced component
   */
  private Component spaced(JButton button) {
    JPanel wrapper = new JPanel();
    wrapper.setLayout(new GridLayout(1, 1));
    wrapper.setMaximumSize(new Dimension(180, 40));
    wrapper.add(button);
    return wrapper;
  }
}