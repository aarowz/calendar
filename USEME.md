# USEME: Calendar Application

## Authors

Dreshta Boghra & Aaron Zhou

---

## Purpose
After three weeks of grueling work, we created a Java-based calendar application that allows users 
to create, edit, and view events through a graphical user interface (GUI) and command-line 
scripting. Our program supports recurring events, all-day events, private/public visibility, and 
event filtering.

---

## Supported GUI Operations Summary

- [x] View a schedule from a specific date
- [x] Toggle private event visibility
- [x] Create new events (single and all-day)
- [x] Mark events as public/private
- [x] Edit existing event data
- [x] Auto-refresh on changes
- [x] Inline private event tagging

---

## GUI Usage Guide

### 📅 To Change the View Schedule Date
- Click **"View Schedule"**
- Pick a start date using the date picker.
- The schedule will display up to 10 events starting from that date.
- Events are sorted *chronologically*.

### 🔒 To Toggle Private Event Visibility
- Use the **"Display Private Events"** checkbox (right panel).
- When checked: shows both public and private events.
- When unchecked: hides private events.
- The list updates immediately upon toggle.

### ➕ To Add an Event
- Click **"Add Event"**
- Fill in:
    - Subject
    - Location (optional)
    - Description (optional)
    - Start/End date and time
    - **All-Day** checkbox (defaults to 8 AM–5 PM)
    - **Private Event** checkbox
- Click **"Create"** to add the event.
- The schedule refreshes automatically.

### ✏️ To Edit an Event
- Click **"Edit Event"**
- Select an event to edit from the dropdown.
- Modify any field: subject, description, time, location, or privacy.
- Click **"Save"** to update the event.

### 🔁 Private Event Indicators
- Private events appear with a `[Private]` tag preceding the event:
  ```
  [Private] Exam from 2020-01-01T08:00 to 2020-01-01T17:00 at Hastings
  ```
- This is shown in the schedule display panel.

### ❌ Invalid Input Handling
- Empty subject → "Please enter a subject for the event."
- Invalid or missing date/time → "Invalid date/time..."
- Start time after end time → "The start time must be before the end time."