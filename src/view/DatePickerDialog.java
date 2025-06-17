// Dreshta Boghra & Aaron Zhou
// CS3500 HW6

package view;

import util.DateDropDownUtil;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.time.LocalDate;

/**
 * A modal dialog that allows the user to select a specific date.
 */
public class DatePickerDialog extends JDialog {

  private final JComboBox<Integer> yearBox;
  private final JComboBox<Integer> monthBox;
  private final JComboBox<Integer> dayBox;

  /**
   * Constructs the date picker dialog.
   *
   * @param owner     the parent frame
   * @param onConfirm callback for when the user selects a date and clicks confirm
   */
  public DatePickerDialog(Frame owner, ActionListener onConfirm) {
    super(owner, "Select Date", true); // modal

    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setResizable(false);
    this.setSize(300, 150);

    this.yearBox = DateDropDownUtil.createYearDropdown();
    this.monthBox = DateDropDownUtil.createMonthDropdown();
    this.dayBox = DateDropDownUtil.createDayDropdown();

    JPanel datePanel = new JPanel();
    datePanel.setBorder(BorderFactory.createTitledBorder("Date"));
    datePanel.add(new JLabel("Year:"));
    datePanel.add(this.yearBox);
    datePanel.add(new JLabel("Month:"));
    datePanel.add(this.monthBox);
    datePanel.add(new JLabel("Day:"));
    datePanel.add(this.dayBox);

    JButton confirmButton = new JButton("View");
    confirmButton.addActionListener(onConfirm);

    this.add(datePanel);
    this.add(confirmButton);
  }

  /**
   * Returns the selected date as a LocalDate.
   *
   * @return the selected date
   */
  public LocalDate getSelectedDate() {
    Integer year = (Integer) this.yearBox.getSelectedItem();
    Integer month = (Integer) this.monthBox.getSelectedItem();
    Integer day = (Integer) this.dayBox.getSelectedItem();

    if (year == null || month == null || day == null) {
      throw new IllegalStateException("All date fields must be selected.");
    }

    return LocalDate.of(year, month, day);
  }
}