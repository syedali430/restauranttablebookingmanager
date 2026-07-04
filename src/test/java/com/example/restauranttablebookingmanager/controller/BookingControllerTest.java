package com.example.restauranttablebookingmanager.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
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

    @Test
    public void testAddNewBookingWhenBookingAlreadyExists() {

        Booking toAdd =
                new Booking(
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-10",
                        "19:30",
                        "Window seat"
                );

        Booking existing =
                new Booking(
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-10",
                        "19:30",
                        "Window seat"
                );

        when(bookingRepository.findById("TBL-001")).thenReturn(existing);

        bookingController.addBooking(toAdd);

        verify(bookingView).showErrorMessage(
                "Booking already exists with ID TBL-001",
                existing
        );

        verifyNoMoreInteractions(ignoreStubs(bookingRepository));
    }

    @Test
    public void testAddNewBookingWhenBookingDoesNotExist() {

        Booking booking =
                new Booking(
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-10",
                        "19:30",
                        "Window seat"
                );

        when(bookingRepository.findById("TBL-001")).thenReturn(null);

        bookingController.addBooking(booking);

        InOrder inOrder = inOrder(bookingRepository, bookingView);
        inOrder.verify(bookingRepository).save(booking);
        inOrder.verify(bookingView).addBooking(booking);
    }

    @Test
    public void testUpdatingExistingBooking() {

        Booking booking =
                new Booking(
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-10",
                        "19:30",
                        "Window seat"
                );

        when(bookingRepository.findById("TBL-001")).thenReturn(booking);

        bookingController.updateBooking(booking);

        InOrder inOrder = inOrder(bookingRepository, bookingView);
        inOrder.verify(bookingRepository).update(booking);
        inOrder.verify(bookingView).updateBooking(booking);
    }
}