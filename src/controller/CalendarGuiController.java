// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package controller;

import model.IDelegator;
import model.ROIEvent;
import model.EventStatus;
import util.DefaultCalendarInitializerUtil;
import view.CalendarSelectorPanel;
import view.CreateEventPanel;
import view.DatePickerDialog;
import view.CalendarGuiView;
import view.EditEventPanel;
import view.ButtonPanel;
import view.NewCalendarPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
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
  private final ButtonPanel buttonPanel;
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
    this.buttonPanel = this.view.getButtonPanel();
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

  // –-------------–-------------–---
  // View events controller functions
  // –-------------–-------------–---

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
        boolean showPrivate = this.buttonPanel.shouldDisplayPrivateEvents();
        allEvents.sort(Comparator.comparing(ROIEvent::getStart));
        List<ROIEvent> visibleEvents = allEvents.stream()
                .filter(ev -> showPrivate || !ev.getStatus().equals(EventStatus.PRIVATE))
                .limit(10)
                .collect(Collectors.toList());

        this.view.displayEvents(visibleEvents);
        this.activeViewDate = selected;
        this.view.setHeaderText("Current calendar: " + this.currentCalendarName +
                " | Displaying the first 10 events from: " + selected);

      } catch (Exception ex) {
        this.view.showError("Unable to retrieve schedule: " + ex.getMessage());
      } finally {
        dialogRef[0].dispose();
      }
    });
    dialogRef[0].setLocationRelativeTo(null);
    dialogRef[0].setVisible(true);
  }

  // –-------------–-------------–--
  // Add events controller functions
  // –-------------–-------------–--

  /**
   * Handles a request to open the event creation form.
   */
  @Override
  public void handleAddEvent() {
    CreateEventPanel panel = new CreateEventPanel();
    JDialog dialog = buildCreateEventDialog(panel);
    JButton confirm = createAddButton(panel, dialog);

    panel.add(confirm);
    dialog.pack();
    dialog.setVisible(true);
  }

  /**
   * Builds and configures the modal dialog for adding a new event.
   *
   * @param panel the CreateEventPanel containing the form UI
   * @return a configured, modal JDialog
   */
  private JDialog buildCreateEventDialog(CreateEventPanel panel) {
    JDialog dialog = new JDialog(view, "Add Event", true);
    dialog.getContentPane().add(panel);
    dialog.pack();
    dialog.setLocationRelativeTo(view);
    return dialog;
  }

  /**
   * Creates the "Create" button and attaches logic for validating and adding a new event.
   *
   * @param panel  the form input panel
   * @param dialog the dialog to close upon successful event creation
   * @return the fully configured JButton
   */
  private JButton createAddButton(CreateEventPanel panel, JDialog dialog) {
    JButton confirm = new JButton("Create");

    confirm.addActionListener(e -> {
      try {
        validateForm(panel);

        LocalDateTime start = resolveStart(panel);
        LocalDateTime end = resolveEnd(panel, start);

        addEventToModel(panel, start, end);
        finalizeAddFlow(panel, dialog);

      } catch (Exception ex) {
        this.view.showError(ex.getMessage());
      }
    });

    return confirm;
  }

  /**
   * Validates the form and throws an exception if invalid.
   *
   * @param panel the input form
   */
  private void validateForm(CreateEventPanel panel) {
    if (panel.getSubject().trim().isEmpty()) {
      throw new IllegalArgumentException("Please enter a subject for the event.");
    }

    LocalDateTime start;
    LocalDateTime end;

    try {
      start = panel.isAllDayEvent()
              ? panel.getStartDateTime().withHour(8).withMinute(0)
              : panel.getStartDateTime();

      end = panel.isAllDayEvent()
              ? start.withHour(17).withMinute(0)
              : panel.getEndDateTime();

    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date/time. Please ensure all fields are " +
              "selected.");
    }

    if (!start.isBefore(end)) {
      throw new IllegalArgumentException("The start time must be before the end time.");
    }
  }

  /**
   * Resolves the starting date and time for the event.
   *
   * @param panel the input form
   * @return the resolved LocalDateTime for the event start
   */
  private LocalDateTime resolveStart(CreateEventPanel panel) {
    LocalDateTime start = panel.getStartDateTime();
    return panel.isAllDayEvent() ? start.withHour(8).withMinute(0) : start;
  }

  /**
   * Resolves the ending date and time for the event.
   *
   * @param panel the input form
   * @param start the resolved start time (used to base all-day ending)
   * @return the resolved LocalDateTime for the event end
   */
  private LocalDateTime resolveEnd(CreateEventPanel panel, LocalDateTime start) {
    return panel.isAllDayEvent()
            ? start.withHour(17).withMinute(0)
            : panel.getEndDateTime();
  }

  /**
   * Sends the event data to the model for creation.
   *
   * @param panel the input form
   * @param start the resolved start time
   * @param end   the resolved end time
   */
  private void addEventToModel(CreateEventPanel panel, LocalDateTime start, LocalDateTime end) {
    this.model.createEvent(
            panel.getSubject(),
            start,
            end,
            panel.getDescription(),
            panel.isPrivateEvent() ? EventStatus.PRIVATE : EventStatus.PUBLIC,
            panel.getLocationText()
    );
  }

  /**
   * Handles cleanup after a successful event creation.
   *
   * @param panel  the input form
   * @param dialog the dialog to close
   */
  private void finalizeAddFlow(CreateEventPanel panel, JDialog dialog) {
    panel.resetForm();
    this.refreshScheduleFromActiveDate();
    dialog.dispose();
  }

  // –-------------–-------------–---
  // Edit events controller functions
  // –-------------–-------------–---

  /**
   * Handles a request to open the event editing form.
   */
  @Override
  public void handleEditEvent() {
    EditEventPanel panel = new EditEventPanel();

    List<ROIEvent> events = this.model.getEventsBetween(LocalDateTime.MIN, LocalDateTime.MAX);
    panel.loadEvents(new ArrayList<>(events));

    JDialog dialog = buildEditEventDialog(panel);
    JButton confirm = createEditButton(panel, dialog);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(confirm);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    dialog.pack();
    dialog.setVisible(true);
  }

  /**
   * Builds the edit event dialog with the given form.
   *
   * @param panel the edit form
   * @return a modal dialog with the form content
   */
  private JDialog buildEditEventDialog(EditEventPanel panel) {
    JDialog dialog = new JDialog(this.view, "Edit Event", true);
    dialog.setLayout(new BorderLayout());
    dialog.add(panel, BorderLayout.CENTER);
    dialog.setLocationRelativeTo(this.view);
    return dialog;
  }

  /**
   * Creates the "Save" button and attaches all logic for validating and editing an event.
   *
   * @param panel  the edit form
   * @param dialog the dialog to close on success
   * @return the configured JButton
   */
  private JButton createEditButton(EditEventPanel panel, JDialog dialog) {
    JButton confirm = new JButton("Save");

    confirm.addActionListener(e -> {
      try {
        validateEditForm(panel);

        ROIEvent original = panel.getSelectedEvent();
        LocalDateTime start = resolveEditStart(panel);
        LocalDateTime end = resolveEditEnd(panel, start);

        checkDuplicateEdit(panel, original, start, end);
        updateEditedEvent(panel, original, start, end);

        finalizeEditFlow(panel, dialog);

      } catch (Exception ex) {
        this.view.showError("Failed to edit event: " + ex.getMessage());
      }
    });

    return confirm;
  }

  /**
   * Validates the edit form and throws if invalid.
   *
   * @param panel the edit panel
   */
  private void validateEditForm(EditEventPanel panel) {
    if (panel.getSelectedEvent() == null) {
      throw new IllegalArgumentException("Please select an event to edit.");
    }

    if (panel.getSubject().trim().isEmpty()) {
      throw new IllegalArgumentException("Please enter a subject for the event.");
    }

    LocalDateTime start;
    LocalDateTime end;

    try {
      start = panel.isAllDayEvent()
              ? panel.getStartDateTime().withHour(8).withMinute(0)
              : panel.getStartDateTime();

      end = panel.isAllDayEvent()
              ? start.withHour(17).withMinute(0)
              : panel.getEndDateTime();
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date/time. Please ensure all fields are " +
              "selected.");
    }

    if (!start.isBefore(end)) {
      throw new IllegalArgumentException("The start time must be before the end time.");
    }
  }

  /**
   * Updates the event in the model with the new values.
   *
   * @param panel    the form input
   * @param original the original event being edited
   * @param start    the updated start time
   * @param end      the updated end time
   */
  private void updateEditedEvent(EditEventPanel panel, ROIEvent original,
                                 LocalDateTime start, LocalDateTime end) {
    this.model.editEvent(
            original.getSubject(),
            original.getStart(),
            panel.getSubject(),
            start,
            end,
            panel.getDescription(),
            panel.isPrivateEvent() ? EventStatus.PRIVATE : EventStatus.PUBLIC,
            panel.getLocationText()
    );
  }

  /**
   * Resolves the edited start time.
   *
   * @param panel the edit form
   * @return the resolved start LocalDateTime
   */
  private LocalDateTime resolveEditStart(EditEventPanel panel) {
    LocalDateTime start = panel.getStartDateTime();
    return panel.isAllDayEvent() ? start.withHour(8).withMinute(0) : start;
  }

  /**
   * Resolves the edited end time.
   *
   * @param panel the edit form
   * @param start the resolved start time
   * @return the resolved end LocalDateTime
   */
  private LocalDateTime resolveEditEnd(EditEventPanel panel, LocalDateTime start) {
    return panel.isAllDayEvent()
            ? start.withHour(17).withMinute(0)
            : panel.getEndDateTime();
  }

  /**
   * Checks whether the new event values would duplicate an existing one.
   *
   * @param panel    the form input
   * @param original the original event being edited
   * @param start    the new start time
   * @param end      the new end time
   * @throws IllegalArgumentException if a duplicate is detected
   */
  private void checkDuplicateEdit(EditEventPanel panel, ROIEvent original,
                                  LocalDateTime start, LocalDateTime end) {
    List<ROIEvent> allEvents = this.model.getEventsBetween(LocalDateTime.MIN, LocalDateTime.MAX);

    for (ROIEvent existing : allEvents) {
      if (!existing.equals(original) &&
              existing.getSubject().equals(panel.getSubject()) &&
              existing.getStart().equals(start) &&
              existing.getEnd().equals(end)) {
        throw new IllegalArgumentException("An event with the same subject and time already " +
                "exists.");
      }
    }
  }

  /**
   * Finalizes the edit workflow by updating the view and resetting the form.
   *
   * @param panel  the edit form
   * @param dialog the dialog to close
   */
  private void finalizeEditFlow(EditEventPanel panel, JDialog dialog) {
    this.refreshScheduleFromActiveDate();
    panel.resetForm();
    dialog.dispose();
  }

  /**
   * Handles a request to switch the active calendar.
   */
  @Override
  public void handleSwitchCalendar() {
    // Build the selector panel and load calendars from the model
    CalendarSelectorPanel selector = new CalendarSelectorPanel();
    selector.loadCalendars(this.model.getCalendarNames());

    // Build the dialog
    JDialog dialog = new JDialog(this.view, "Switch Calendar", true);
    dialog.setLayout(new BorderLayout());
    dialog.add(selector, BorderLayout.CENTER);

    JButton confirm = new JButton("Switch");
    confirm.addActionListener(e -> {
      try {
        String selected = selector.getSelectedCalendar();
        if (selected == null || selected.isEmpty()) {
          throw new IllegalArgumentException("You must select a calendar.");
        }

        this.currentCalendarName = selected;
        this.model.useCalendar(selected);
        this.refreshScheduleFromActiveDate();
        dialog.dispose();

      } catch (Exception ex) {
        this.view.showError("Failed to switch calendar: " + ex.getMessage());
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
   * Handles a request to create a new calendar.
   */
  @Override
  public void handleCreateCalendar() {
    // Build the creation panel and load timezones
    NewCalendarPanel panel = new NewCalendarPanel();
    panel.loadTimezones();

    // Build the dialog
    JDialog dialog = new JDialog(this.view, "Create New Calendar", true);
    dialog.setLayout(new BorderLayout());
    dialog.add(panel, BorderLayout.CENTER);

    JButton confirm = new JButton("Create");
    confirm.addActionListener(e -> {
      try {
        String name = panel.getCalendarName();
        ZoneId timezone = ZoneId.of(panel.getSelectedTimezone());

        if (name == null || name.isEmpty()) {
          throw new IllegalArgumentException("Calendar name cannot be empty.");
        }
        if (timezone == null) {
          throw new IllegalArgumentException("You must select a timezone.");
        }

        this.model.createCalendar(name, timezone);
        this.model.useCalendar(name);
        this.currentCalendarName = name;

        this.refreshScheduleFromActiveDate();
        panel.reset();
        dialog.dispose();

      } catch (Exception ex) {
        this.view.showError("Failed to create calendar: " + ex.getMessage());
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
   * Refresh schedule that accounts for immediate display of results.
   */
  private void refreshScheduleFromActiveDate() {
    List<ROIEvent> events = this.model.getEventsBetween(
            this.activeViewDate.atStartOfDay(),
            LocalDateTime.MAX
    );
    events.sort(Comparator.comparing(ROIEvent::getStart));

    boolean showPrivate = this.buttonPanel.shouldDisplayPrivateEvents();

    List<ROIEvent> top10 = events.stream()
            .filter(ev -> showPrivate || !ev.getStatus().equals(EventStatus.PRIVATE))
            .limit(10)
            .collect(Collectors.toList());

    this.view.displayEvents(top10);
    this.view.setHeaderText("Current calendar: " + this.currentCalendarName +
            " | Displaying the first 10 events from: " + this.activeViewDate);
  }

  @Override
  public void refreshSchedule() {
    this.refreshScheduleFromActiveDate();
  }
}