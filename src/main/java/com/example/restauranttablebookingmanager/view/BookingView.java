package com.example.restauranttablebookingmanager.view;

import java.util.List;

import com.example.restauranttablebookingmanager.model.Booking;

public interface BookingView {

    void displayBookings(List<Booking> bookings);

    void addBooking(Booking booking);

    void deleteBooking(Booking booking);

    void updateBooking(Booking booking);

    void showErrorMessage(String message, Booking booking);
}