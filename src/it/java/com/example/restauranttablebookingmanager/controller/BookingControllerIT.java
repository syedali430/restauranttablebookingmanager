package com.example.restauranttablebookingmanager.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.MongoDBContainer;

import com.example.restauranttablebookingmanager.model.Booking;
import com.example.restauranttablebookingmanager.repository.BookingRepository;
import com.example.restauranttablebookingmanager.repository.mongo.BookingMongoRepository;
import com.example.restauranttablebookingmanager.view.BookingView;
import com.mongodb.MongoClient;

public class BookingControllerIT {

    @ClassRule
    public static final MongoDBContainer mongo =
            new MongoDBContainer("mongo:4.4.3");

    @Mock
    private BookingView bookingView;

    private BookingRepository bookingRepository;

    private BookingController bookingController;

    private AutoCloseable closeable;

    public static final String BOOKING_COLLECTION_NAME = "booking";
    public static final String BOOKING_DB_NAME =
            "restauranttablebookingmanager";

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        bookingRepository =
                new BookingMongoRepository(
                        new MongoClient(
                                mongo.getHost(),
                                mongo.getFirstMappedPort()
                        ),
                        BOOKING_DB_NAME,
                        BOOKING_COLLECTION_NAME
                );

        for (Booking booking : bookingRepository.findAll()) {
            bookingRepository.delete(booking.getBookingId());
        }

        bookingController =
                new BookingController(
                        bookingRepository,
                        bookingView
                );
    }

    @After
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testAllBookingsAreShownInTheView() {

        Booking booking =
                new Booking(
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-10",
                        "19:30",
                        "Booking for IT test"
                );

        bookingRepository.save(booking);

        bookingController.getAllBookings();

        verify(bookingView).displayBookings(asList(booking));
    }

    @Test
    public void testNewBookingIsAddedToTheView() {

        Booking booking =
                new Booking(
                        "TBL-002",
                        "Giulia Bianchi",
                        "T02",
                        "2026-07-11",
                        "20:00",
                        "Birthday dinner"
                );

        bookingController.addBooking(booking);

        verify(bookingView).addBooking(booking);
    }

    @Test
    public void testSelectedBookingIsDeletedFromTheView() {

        Booking bookingToDelete =
                new Booking(
                        "TBL-003",
                        "Luca Ferrari",
                        "T03",
                        "2026-07-12",
                        "18:30",
                        "Delete booking"
                );

        bookingRepository.save(bookingToDelete);

        bookingController.deleteBooking(bookingToDelete);

        verify(bookingView).deleteBooking(bookingToDelete);
    }

    @Test
    public void testSelectedBookingIsUpdatedInTheView() {

        Booking original =
                new Booking(
                        "TBL-004",
                        "Sofia Romano",
                        "T04",
                        "2026-07-13",
                        "19:00",
                        "Original request"
                );

        bookingRepository.save(original);

        Booking updated =
                new Booking(
                        "TBL-004",
                        "Sofia Romano",
                        "T04",
                        "2026-07-14",
                        "20:00",
                        "Updated request"
                );

        bookingController.updateBooking(updated);

        verify(bookingView).updateBooking(updated);
    }
}