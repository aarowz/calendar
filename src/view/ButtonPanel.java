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
import javax.swing.JCheckBox;

/**
 * Panel that displays the control buttons on the right side of the GUI.
 * Includes buttons for viewing, adding, editing events, and switching calendars.
 */
public class ButtonPanel extends JPanel {
  private ICalendarGuiFeatures features;
  private final JCheckBox displayPrivateBox;
  private final JButton viewScheduleButton;
  private final JButton addEventButton;
  private final JButton editEventButton;
  private final JButton switchCalendarButton;
  private final JButton createCalendarButton;

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
    this.createCalendarButton = new JButton("Create Calendar");
    this.displayPrivateBox = new JCheckBox("Display Private Events", true);

    this.add(spaced(this.viewScheduleButton));
    this.add(spaced(this.addEventButton));
    this.add(spaced(this.editEventButton));
    this.add(spaced(this.switchCalendarButton));
    this.add(spaced(this.createCalendarButton));
    this.add(spaced(this.displayPrivateBox));
    this.displayPrivateBox.addActionListener(e -> {
      if (this.features != null) {
        this.features.refreshSchedule();
      }
    });
  }

  /**
   * Attaches the given feature callbacks to each button.
   *
   * @param features the controller features to attach
   */
  public void setFeatures(ICalendarGuiFeatures features) {
    this.features = features;

    this.viewScheduleButton.addActionListener(e -> features.handleViewSchedule());
    this.addEventButton.addActionListener(e -> features.handleAddEvent());
    this.editEventButton.addActionListener(e -> features.handleEditEvent());
    this.switchCalendarButton.addActionListener(e -> features.handleSwitchCalendar());
    this.createCalendarButton.addActionListener(e -> features.handleCreateCalendar());
  }

  /**
   * Adds vertical spacing around a button.
   *
   * @param buttonLike the button to wrap with spacing
   * @return a vertically spaced component
   */
  private Component spaced(Component buttonLike) {
    JPanel wrapper = new JPanel();
    wrapper.setLayout(new GridLayout(1, 1));
    wrapper.setMaximumSize(new Dimension(180, 40));
    wrapper.add(buttonLike);
    return wrapper;
  }

  /**
   * Returns whether the user wants to display private events.
   *
   * @return true if private events should be shown
   */
  public boolean shouldDisplayPrivateEvents() {
    return this.displayPrivateBox.isSelected();
  }
}