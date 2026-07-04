package com.example.restauranttablebookingmanager.repository;

import java.util.List;

import com.example.restauranttablebookingmanager.model.Booking;

public interface BookingRepository {

    void save(Booking booking);

    List<Booking> findAll();

    Booking findById(String bookingId);

    void update(Booking booking);

    void delete(String bookingId);
}