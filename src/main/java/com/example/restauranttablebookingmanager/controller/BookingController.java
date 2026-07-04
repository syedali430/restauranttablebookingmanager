package com.example.restauranttablebookingmanager.controller;

import com.example.restauranttablebookingmanager.model.Booking;
import com.example.restauranttablebookingmanager.repository.BookingRepository;
import com.example.restauranttablebookingmanager.view.BookingView;

public class BookingController {

    private final BookingRepository repository;
    private final BookingView view;

    public BookingController(
            BookingRepository repository,
            BookingView view) {

        this.repository = repository;
        this.view = view;
    }

    public void getAllBookings() {

        view.displayBookings(repository.findAll());
    }

    public synchronized void addBooking(Booking booking) {

        Booking existing =
                repository.findById(booking.getBookingId());

        if (existing != null) {

            view.showErrorMessage(
                    "Booking already exists with ID "
                            + booking.getBookingId(),
                    existing
            );

            return;
        }

        repository.save(booking);
        view.addBooking(booking);
    }
}