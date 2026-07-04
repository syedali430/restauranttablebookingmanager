package com.example.restauranttablebookingmanager.controller;

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
}