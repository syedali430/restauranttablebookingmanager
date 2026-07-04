package com.example.restauranttablebookingmanager.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.restauranttablebookingmanager.model.Booking;
import com.example.restauranttablebookingmanager.repository.BookingRepository;
import com.example.restauranttablebookingmanager.view.BookingView;

public class BookingControllerTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingView bookingView;

    @InjectMocks
    private BookingController bookingController;

    private AutoCloseable closeable;

    @Before
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void testDisplayAllBookings() {
        List<Booking> bookings = asList(new Booking());
        when(bookingRepository.findAll()).thenReturn(bookings);

        bookingController.getAllBookings();

        verify(bookingView).displayBookings(bookings);
    }
}