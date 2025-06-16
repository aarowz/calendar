// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package controller;

import model.IDelegator;
import model.ROIEvent;
import util.DefaultCalendarInitializerUtil;
import view.CreateEventPanel;
import view.DatePickerDialog;
import view.CalendarGuiView;
import view.EditEventPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The main GUI controller for the calendar application.
 * Handles user interactions and delegates behavior to the model and view.
 */
public class CalendarGuiController implements ICalendarGuiFeatures {
  private LocalDate activeViewDate = LocalDate.now();
  private final IDelegator model;
  private final CalendarGuiView view;
  private String currentCalendarName;

  /**
   * Constructs the GUI controller.
   *
   * @param model the calendar model delegator
   * @param view  the graphical view
   */
  public CalendarGuiController(IDelegator model, CalendarGuiView view) {
    this.model = Objects.requireNonNull(model);
    this.view = Objects.requireNonNull(view);
    this.currentCalendarName = DefaultCalendarInitializerUtil.createDefaultCalendar(this.model);
    this.setupViewFeatures();
    this.refreshScheduleFromActiveDate();
  }

  /**
   * Sets up feature callbacks for the view.
   */
  private void setupViewFeatures() {
    this.view.setFeatures(this);
  }

  /**
   * Handles a request to view the schedule from a selected date.
   */
  @Override
  public void handleViewSchedule() {
    final DatePickerDialog[] dialogRef = new DatePickerDialog[1];

    dialogRef[0] = new DatePickerDialog(view, e -> {
      try {
        LocalDate selected = dialogRef[0].getSelectedDate();

        List<ROIEvent> allEvents = model.getEventsBetween(
                selected.atStartOfDay(),
                LocalDateTime.MAX
        );
        List<ROIEvent> top10 = allEvents.stream().limit(10).collect(Collectors.toList());

        this.view.displayEvents(top10);
        this.view.setHeaderText("Displaying up to 10 events from: " + selected);
        this.activeViewDate = selected;

      } catch (Exception ex) {
        this.view.showError("Unable to retrieve schedule: " + ex.getMessage());
      } finally {
        dialogRef[0].dispose();
      }
    });
    dialogRef[0].setLocationRelativeTo(null);
    dialogRef[0].setVisible(true);
  }

  /**
   * Handles a request to open the event creation form.
   */
  @Override
  public void handleAddEvent() {
    CreateEventPanel panel = new CreateEventPanel();

    JDialog dialog = new JDialog(view, "Add Event", true);
    dialog.getContentPane().add(panel);
    dialog.pack();
    dialog.setLocationRelativeTo(view);

    JButton confirm = new JButton("Create");
    confirm.addActionListener(e -> {
      try {
        if (!panel.isValidForm()) {
          throw new IllegalArgumentException("Invalid form. Make sure subject is filled and " +
                  "times are valid.");
        }

        // determine start and end times
        LocalDateTime start = panel.getStartDateTime();
        LocalDateTime end = panel.getEndDateTime();

        if (panel.isAllDayEvent()) {
          // force to 8am–5pm for all-day
          start = start.withHour(8).withMinute(0);
          end = start.withHour(17).withMinute(0);
        }

        this.model.createEvent(
                panel.getSubject(),
                start,
                end,
                panel.getDescription(),
                panel.getStatus(),
                panel.getLocationText()
        );

        panel.resetForm();
        this.refreshScheduleFromActiveDate();
        dialog.dispose();

      } catch (Exception ex) {
        this.view.showError("Failed to create event: " + ex.getMessage());
      }
    });

    panel.add(confirm);
    dialog.pack();
    dialog.setVisible(true);
  }

  @Override
  public void handleEditEvent() {
    EditEventPanel panel = new EditEventPanel();

    // Load all events across all dates
    List<ROIEvent> events = this.model.getEventsBetween(
            LocalDateTime.MIN,
            LocalDateTime.MAX
    );
    panel.loadEvents(new ArrayList<>(events));

    // Build and configure the dialog
    JDialog dialog = new JDialog(this.view, "Edit Event", true);
    dialog.setLayout(new BorderLayout());
    dialog.add(panel, BorderLayout.CENTER);

    // Create Save button with all-day support
    JButton confirm = new JButton("Save");
    confirm.addActionListener(e -> {
      try {
        if (!panel.isValidForm()) {
          throw new IllegalArgumentException("Invalid form or no event selected.");
        }

        ROIEvent original = panel.getSelectedEvent();

        LocalDateTime start;
        LocalDateTime end;

        if (panel.isAllDayEvent()) {
          // Force 8am to 5pm on selected start date
          start = panel.getStartDateTime().withHour(8).withMinute(0);
          end = start.withHour(17).withMinute(0);
        } else {
          start = panel.getStartDateTime();
          end = panel.getEndDateTime();
        }

        this.model.editEvent(
                original.getSubject(),
                original.getStart(),
                panel.getSubject(),
                start,
                end,
                panel.getDescription(),
                panel.getStatus(),
                panel.getLocationText()
        );

        this.refreshScheduleFromActiveDate();
        panel.resetForm();
        dialog.dispose();

      } catch (Exception ex) {
        this.view.showError("Failed to edit event: " + ex.getMessage());
      }
    });

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(confirm);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    dialog.pack();
    dialog.setLocationRelativeTo(this.view);
    dialog.setVisible(true);
  }

  /**
   * Creates a "Save" button that applies edits from the given EditEventPanel.
   * When clicked, the button validates input, updates the event in the model,
   * refreshes the schedule view, and closes the dialog.
   *
   * @param panel  the event editing form containing updated input
   * @param dialog the dialog window containing the form
   * @return a configured "Save" button with attached action logic
   */
  private JButton getjButton(EditEventPanel panel, JDialog dialog) {
    JButton confirm = new JButton("Save");
    confirm.addActionListener(e -> {
      try {
        if (!panel.isValidForm()) {
          throw new IllegalArgumentException("Invalid form or no event selected.");
        }

        ROIEvent original = panel.getSelectedEvent();

        this.model.editEvent(
                original.getSubject(),
                original.getStart(),
                panel.getSubject(),
                panel.getStartDateTime(),
                panel.getEndDateTime(),
                panel.getDescription(),
                panel.getStatus(),
                panel.getLocationText()
        );

        this.refreshScheduleFromActiveDate();
        panel.resetForm();
        dialog.dispose();

      } catch (Exception ex) {
        this.view.showError("Failed to edit event: " + ex.getMessage());
      }
    });
    return confirm;
  }

  /**
   * Handles a request to switch the active calendar.
   */
  @Override
  public void handleSwitchCalendar() {
    // stub
  }

  /**
   * Refresh schedule that accounts for immediate display of results.
   */
  private void refreshScheduleFromActiveDate() {
    List<ROIEvent> events = this.model.getEventsBetween(
            this.activeViewDate.atStartOfDay(),
            LocalDateTime.MAX
    ).stream().limit(10).collect(Collectors.toList());

    this.view.displayEvents(events);
    this.view.setHeaderText("Displaying up to 10 events from: " + this.activeViewDate);
  }
}