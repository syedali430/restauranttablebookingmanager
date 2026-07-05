package com.example.restauranttablebookingmanager.view.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.example.restauranttablebookingmanager.controller.BookingController;
import com.example.restauranttablebookingmanager.model.Booking;
import com.example.restauranttablebookingmanager.view.BookingView;

public class BookingSwingView extends JFrame implements BookingView {

    private static final long serialVersionUID = 1L;

    private static final int SPECIAL_REQUEST_MAX_LENGTH = 200;

    private JTextField txtBookingId;
    private JTextField txtCustomerName;
    private JTextField txtTableNumber;
    private JTextField txtBookingDate;
    private JTextField txtBookingTime;
    private JTextField txtSpecialRequest;

    private JButton btnAddBooking;
    private JButton btnUpdateBooking;
    private JButton btnDeleteBooking;
    private JButton btnClear;

    private JLabel lblErrorMessage;

    private JList<Booking> listBookings;
    private DefaultListModel<Booking> listBookingModel;

    private transient BookingController bookingController;

    public void setBookingController(BookingController bookingController) {
        this.bookingController = bookingController;
    }

    public DefaultListModel<Booking> getListBookingModel() {
        return listBookingModel;
    }

    public BookingSwingView() {
        setMinimumSize(new Dimension(900, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Restaurant Table Booking Manager");

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 45, 0, 770, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 35, 173, 35, 35, 35, 23, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] {
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE
        };
        getContentPane().setLayout(gridBagLayout);

        JLabel lblBookingId = new JLabel("Booking ID");
        GridBagConstraints gbc_lblBookingId = new GridBagConstraints();
        gbc_lblBookingId.anchor = GridBagConstraints.EAST;
        gbc_lblBookingId.insets = new Insets(0, 0, 5, 5);
        gbc_lblBookingId.gridx = 1;
        gbc_lblBookingId.gridy = 0;
        getContentPane().add(lblBookingId, gbc_lblBookingId);

        txtBookingId = new JTextField();
        txtBookingId.setName("bookingidTextBox");
        GridBagConstraints gbc_txtBookingId = new GridBagConstraints();
        gbc_txtBookingId.insets = new Insets(0, 0, 5, 5);
        gbc_txtBookingId.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtBookingId.gridx = 2;
        gbc_txtBookingId.gridy = 0;
        getContentPane().add(txtBookingId, gbc_txtBookingId);

        JLabel lblCustomerName = new JLabel("Customer Name");
        GridBagConstraints gbc_lblCustomerName = new GridBagConstraints();
        gbc_lblCustomerName.anchor = GridBagConstraints.EAST;
        gbc_lblCustomerName.insets = new Insets(0, 0, 5, 5);
        gbc_lblCustomerName.gridx = 1;
        gbc_lblCustomerName.gridy = 1;
        getContentPane().add(lblCustomerName, gbc_lblCustomerName);

        txtCustomerName = new JTextField();
        txtCustomerName.setName("customernameTextBox");
        GridBagConstraints gbc_txtCustomerName = new GridBagConstraints();
        gbc_txtCustomerName.insets = new Insets(0, 0, 5, 5);
        gbc_txtCustomerName.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtCustomerName.gridx = 2;
        gbc_txtCustomerName.gridy = 1;
        getContentPane().add(txtCustomerName, gbc_txtCustomerName);

        JLabel lblTableNumber = new JLabel("Table Number");
        GridBagConstraints gbc_lblTableNumber = new GridBagConstraints();
        gbc_lblTableNumber.anchor = GridBagConstraints.EAST;
        gbc_lblTableNumber.insets = new Insets(0, 0, 5, 5);
        gbc_lblTableNumber.gridx = 1;
        gbc_lblTableNumber.gridy = 2;
        getContentPane().add(lblTableNumber, gbc_lblTableNumber);

        txtTableNumber = new JTextField();
        txtTableNumber.setName("tablenumberTextBox");
        GridBagConstraints gbc_txtTableNumber = new GridBagConstraints();
        gbc_txtTableNumber.insets = new Insets(0, 0, 5, 5);
        gbc_txtTableNumber.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtTableNumber.gridx = 2;
        gbc_txtTableNumber.gridy = 2;
        getContentPane().add(txtTableNumber, gbc_txtTableNumber);

        JLabel lblBookingDate = new JLabel("Booking Date");
        GridBagConstraints gbc_lblBookingDate = new GridBagConstraints();
        gbc_lblBookingDate.anchor = GridBagConstraints.EAST;
        gbc_lblBookingDate.insets = new Insets(0, 0, 5, 5);
        gbc_lblBookingDate.gridx = 1;
        gbc_lblBookingDate.gridy = 3;
        getContentPane().add(lblBookingDate, gbc_lblBookingDate);

        txtBookingDate = new JTextField();
        txtBookingDate.setName("bookingdateTextBox");
        GridBagConstraints gbc_txtBookingDate = new GridBagConstraints();
        gbc_txtBookingDate.insets = new Insets(0, 0, 5, 5);
        gbc_txtBookingDate.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtBookingDate.gridx = 2;
        gbc_txtBookingDate.gridy = 3;
        getContentPane().add(txtBookingDate, gbc_txtBookingDate);

        JLabel lblBookingTime = new JLabel("Booking Time");
        GridBagConstraints gbc_lblBookingTime = new GridBagConstraints();
        gbc_lblBookingTime.anchor = GridBagConstraints.EAST;
        gbc_lblBookingTime.insets = new Insets(0, 0, 5, 5);
        gbc_lblBookingTime.gridx = 1;
        gbc_lblBookingTime.gridy = 4;
        getContentPane().add(lblBookingTime, gbc_lblBookingTime);

        txtBookingTime = new JTextField();
        txtBookingTime.setName("bookingtimeTextBox");
        GridBagConstraints gbc_txtBookingTime = new GridBagConstraints();
        gbc_txtBookingTime.insets = new Insets(0, 0, 5, 5);
        gbc_txtBookingTime.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtBookingTime.gridx = 2;
        gbc_txtBookingTime.gridy = 4;
        getContentPane().add(txtBookingTime, gbc_txtBookingTime);

        JLabel lblSpecialRequest = new JLabel("Special Request");
        GridBagConstraints gbc_lblSpecialRequest = new GridBagConstraints();
        gbc_lblSpecialRequest.anchor = GridBagConstraints.EAST;
        gbc_lblSpecialRequest.insets = new Insets(0, 0, 5, 5);
        gbc_lblSpecialRequest.gridx = 1;
        gbc_lblSpecialRequest.gridy = 5;
        getContentPane().add(lblSpecialRequest, gbc_lblSpecialRequest);

        txtSpecialRequest = new JTextField();
        txtSpecialRequest.setName("specialrequestTextBox");
        GridBagConstraints gbc_txtSpecialRequest = new GridBagConstraints();
        gbc_txtSpecialRequest.insets = new Insets(0, 0, 5, 5);
        gbc_txtSpecialRequest.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtSpecialRequest.gridx = 2;
        gbc_txtSpecialRequest.gridy = 5;
        getContentPane().add(txtSpecialRequest, gbc_txtSpecialRequest);

        btnAddBooking = new JButton("Add Booking");
        btnAddBooking.setEnabled(false);
        btnAddBooking.setName("btnAddBooking");
        btnAddBooking.addActionListener(e -> new Thread(() -> {
            Booking booking = buildBookingFromFields();
            if (booking != null) {
                bookingController.addBooking(booking);
            }
        }).start());

        GridBagConstraints gbc_btnAddBooking = new GridBagConstraints();
        gbc_btnAddBooking.insets = new Insets(0, 0, 5, 5);
        gbc_btnAddBooking.gridx = 2;
        gbc_btnAddBooking.gridy = 6;
        getContentPane().add(btnAddBooking, gbc_btnAddBooking);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridwidth = 3;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 7;
        getContentPane().add(scrollPane, gbc_scrollPane);

        listBookingModel = new DefaultListModel<>();
        listBookings = new JList<>(listBookingModel);
        listBookings.setName("bookingList");
        listBookings.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listBookings.addListSelectionListener(e -> {
            boolean selected = listBookings.getSelectedIndex() != -1;

            btnDeleteBooking.setEnabled(selected);
            btnUpdateBooking.setEnabled(selected);
            btnClear.setEnabled(selected);

            if (selected) {
                Booking booking = listBookings.getSelectedValue();
                txtBookingId.setText(booking.getBookingId());
                txtCustomerName.setText(booking.getCustomerName());
                txtTableNumber.setText(booking.getTableNumber());
                txtBookingDate.setText(booking.getBookingDate());
                txtBookingTime.setText(booking.getBookingTime());
                txtSpecialRequest.setText(booking.getSpecialRequest());

                txtBookingId.setEnabled(false);
                btnAddBooking.setEnabled(false);
            } else {
                clearFields();
                txtBookingId.setEnabled(true);
            }
        });

        listBookings.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Booking booking = (Booking) value;
                return super.getListCellRendererComponent(list, getDisplayString(booking), index, isSelected, cellHasFocus);
            }
        });

        scrollPane.setViewportView(listBookings);

        btnUpdateBooking = new JButton("Update Booking");
        btnUpdateBooking.setEnabled(false);
        btnUpdateBooking.setName("btnUpdateBooking");
        btnUpdateBooking.addActionListener(e -> new Thread(() -> {
            Booking booking = buildBookingFromFields();
            if (booking != null) {
                bookingController.updateBooking(booking);
            }
        }).start());

        GridBagConstraints gbc_btnUpdateBooking = new GridBagConstraints();
        gbc_btnUpdateBooking.insets = new Insets(0, 0, 5, 5);
        gbc_btnUpdateBooking.gridx = 2;
        gbc_btnUpdateBooking.gridy = 8;
        getContentPane().add(btnUpdateBooking, gbc_btnUpdateBooking);

        btnDeleteBooking = new JButton("Delete Booking");
        btnDeleteBooking.setEnabled(false);
        btnDeleteBooking.setName("btnDeleteBooking");
        btnDeleteBooking.addActionListener(e -> new Thread(() -> {
            Booking selectedBooking = listBookings.getSelectedValue();
            if (selectedBooking != null) {
                bookingController.deleteBooking(selectedBooking);
            }
        }).start());

        GridBagConstraints gbc_btnDeleteBooking = new GridBagConstraints();
        gbc_btnDeleteBooking.insets = new Insets(0, 0, 5, 5);
        gbc_btnDeleteBooking.gridx = 2;
        gbc_btnDeleteBooking.gridy = 9;
        getContentPane().add(btnDeleteBooking, gbc_btnDeleteBooking);

        btnClear = new JButton("Clear");
        btnClear.setEnabled(false);
        btnClear.setName("btnClear");
        btnClear.addActionListener(e -> {
            clearFields();
            btnUpdateBooking.setEnabled(false);
            btnDeleteBooking.setEnabled(false);
            btnClear.setEnabled(false);
            listBookings.clearSelection();
            txtBookingId.setEnabled(true);
        });

        GridBagConstraints gbc_btnClear = new GridBagConstraints();
        gbc_btnClear.insets = new Insets(0, 0, 5, 5);
        gbc_btnClear.gridx = 2;
        gbc_btnClear.gridy = 10;
        getContentPane().add(btnClear, gbc_btnClear);

        lblErrorMessage = new JLabel("");
        lblErrorMessage.setName("errorMessageLabel");
        GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
        gbc_lblErrorMessage.gridwidth = 3;
        gbc_lblErrorMessage.insets = new Insets(0, 0, 0, 5);
        gbc_lblErrorMessage.gridx = 0;
        gbc_lblErrorMessage.gridy = 11;
        getContentPane().add(lblErrorMessage, gbc_lblErrorMessage);

        KeyAdapter addEnabler = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                boolean allFilled = !txtBookingId.getText().trim().isEmpty()
                        && !txtCustomerName.getText().trim().isEmpty()
                        && !txtTableNumber.getText().trim().isEmpty()
                        && !txtBookingDate.getText().trim().isEmpty()
                        && !txtBookingTime.getText().trim().isEmpty()
                        && !txtSpecialRequest.getText().trim().isEmpty();

                btnAddBooking.setEnabled(listBookings.getSelectedIndex() == -1 && allFilled);
            }
        };

        txtBookingId.addKeyListener(addEnabler);
        txtCustomerName.addKeyListener(addEnabler);
        txtTableNumber.addKeyListener(addEnabler);
        txtBookingDate.addKeyListener(addEnabler);
        txtBookingTime.addKeyListener(addEnabler);
        txtSpecialRequest.addKeyListener(addEnabler);
    }

    @Override
    public void displayBookings(List<Booking> bookings) {
        bookings.forEach(listBookingModel::addElement);
    }

    @Override
    public void addBooking(Booking booking) {
        SwingUtilities.invokeLater(() -> {
            listBookingModel.addElement(booking);
            resetErrorLabel();
            clearFields();
        });
    }

    @Override
    public void deleteBooking(Booking booking) {
        SwingUtilities.invokeLater(() -> {
            listBookingModel.removeElement(booking);
            resetErrorLabel();
            clearFields();
            txtBookingId.setEnabled(true);
        });
    }

    @Override
    public void updateBooking(Booking booking) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < listBookingModel.size(); i++) {
                if (listBookingModel.get(i).getBookingId().equals(booking.getBookingId())) {
                    listBookingModel.set(i, booking);
                    break;
                }
            }
            resetErrorLabel();
            clearFields();
            txtBookingId.setEnabled(true);
            listBookings.clearSelection();
        });
    }

    @Override
    public void showErrorMessage(String message, Booking booking) {
        SwingUtilities.invokeLater(() -> {
            String displayMessage = (booking == null) ? "" : getDisplayString(booking);
            lblErrorMessage.setText(message + displayMessage);
            if (booking != null) {
                listBookingModel.removeElement(booking);
            }
        });
    }

    private void resetErrorLabel() {
        lblErrorMessage.setText("");
    }

    private void clearFields() {
        txtBookingId.setText("");
        txtCustomerName.setText("");
        txtTableNumber.setText("");
        txtBookingDate.setText("");
        txtBookingTime.setText("");
        txtSpecialRequest.setText("");
    }

    private String getDisplayString(Booking booking) {
        return booking.getBookingId() + " - " + booking.getCustomerName() + " - "
                + booking.getTableNumber() + " - " + booking.getBookingDate() + " - "
                + booking.getBookingTime() + " - " + booking.getSpecialRequest();
    }

    private boolean isBookingIdValid(String s) {
        if (s == null) {
            return false;
        }
        return s.matches("^TBL-\\d{3}$");
    }

    private boolean isLettersAndSpaces(String s) {
        if (s == null) {
            return false;
        }
        if (s.isEmpty()) {
            return false;
        }
        return s.matches("^[a-zA-Z ]+$");
    }

    private boolean isTableNumberValid(String s) {
        if (s == null) {
            return false;
        }
        return s.matches("^T\\d{2}$");
    }

    private boolean isDateValid(String s) {
        if (s == null) {
            return false;
        }
        return s.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    private boolean isTimeValid(String s) {
        if (s == null) {
            return false;
        }
        return s.matches("^\\d{2}:\\d{2}$");
    }

    private Booking buildBookingFromFields() {
        String bookingId = txtBookingId.getText().trim();
        String customerName = txtCustomerName.getText().trim();
        String tableNumber = txtTableNumber.getText().trim();
        String bookingDate = txtBookingDate.getText().trim();
        String bookingTime = txtBookingTime.getText().trim();
        String specialRequest = txtSpecialRequest.getText().trim();

        if (!isBookingIdValid(bookingId)) {
            showErrorMessage("Booking ID must be TBL- followed by 3 digits (e.g., TBL-001)", null);
            return null;
        }

        if (!isLettersAndSpaces(customerName)) {
            showErrorMessage("Customer Name must contain only letters and spaces", null);
            return null;
        }

        if (!isTableNumberValid(tableNumber)) {
            showErrorMessage("Table Number must be T followed by 2 digits (e.g., T01)", null);
            return null;
        }

        if (!isDateValid(bookingDate)) {
            showErrorMessage("Booking Date must be in format YYYY-MM-DD", null);
            return null;
        }

        if (!isTimeValid(bookingTime)) {
            showErrorMessage("Booking Time must be in format HH:MM", null);
            return null;
        }

        if (specialRequest.length() > SPECIAL_REQUEST_MAX_LENGTH) {
            showErrorMessage("Special Request cannot exceed 200 characters", null);
            return null;
        }

        return new Booking(bookingId, customerName, tableNumber, bookingDate, bookingTime, specialRequest);
    }
}
