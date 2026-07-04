package com.example.restauranttablebookingmanager.model;

import java.util.Objects;

public class Booking {

    private String bookingId;
    private String customerName;
    private String tableNumber;
    private String bookingDate;
    private String bookingTime;
    private String specialRequest;

    public Booking() {
    }

    public Booking(String bookingId,
            String customerName,
            String tableNumber,
            String bookingDate,
            String bookingTime,
            String specialRequest) {

        this.bookingId = bookingId;
        this.customerName = customerName;
        this.tableNumber = tableNumber;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.specialRequest = specialRequest;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Booking booking = (Booking) o;

        return Objects.equals(bookingId, booking.bookingId)
                && Objects.equals(customerName, booking.customerName)
                && Objects.equals(tableNumber, booking.tableNumber)
                && Objects.equals(bookingDate, booking.bookingDate)
                && Objects.equals(bookingTime, booking.bookingTime)
                && Objects.equals(specialRequest, booking.specialRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                bookingId,
                customerName,
                tableNumber,
                bookingDate,
                bookingTime,
                specialRequest);
    }

    @Override
    public String toString() {
        return bookingId + " - "
                + customerName + " - "
                + tableNumber + " - "
                + bookingDate + " - "
                + bookingTime + " - "
                + specialRequest;
    }
}