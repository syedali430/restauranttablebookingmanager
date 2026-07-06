package com.example.restauranttablebookingmanager.controller.racecondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.awaitility.Awaitility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.restauranttablebookingmanager.controller.BookingController;
import com.example.restauranttablebookingmanager.model.Booking;
import com.example.restauranttablebookingmanager.repository.BookingRepository;
import com.example.restauranttablebookingmanager.view.BookingView;

public class BookingControllerRaceConditionTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingView bookingView;

    @InjectMocks
    private BookingController bookingController;

    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void testAddingSameBookingAtTheSameTimeAddsOnlyOneBooking() {
        List<Booking> bookings = new ArrayList<>();

        Booking booking =
                new Booking(
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-10",
                        "19:30",
                        "Window seat"
                );

        when(bookingRepository.findById(anyString()))
                .thenAnswer(invocation ->
                        bookings.stream().findFirst().orElse(null)
                );

        doAnswer(invocation -> {
            bookings.add(booking);
            return null;
        }).when(bookingRepository).save(any(Booking.class));

        List<Thread> threads =
                IntStream.range(0, 10)
                        .mapToObj(i -> new Thread(
                                () -> bookingController.addBooking(booking)
                        ))
                        .peek(Thread::start)
                        .collect(Collectors.toList());

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .until(() -> threads.stream().noneMatch(Thread::isAlive));

        assertThat(bookings).containsExactly(booking);
    }

    @Test
    public void testDeletingSameBookingAtTheSameTimeRemovesBookingOnce() {
        List<Booking> bookings = new ArrayList<>();

        Booking booking =
                new Booking(
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-10",
                        "19:30",
                        "Window seat"
                );

        bookings.add(booking);

        when(bookingRepository.findById(anyString()))
                .thenAnswer(invocation ->
                        bookings.stream().findFirst().orElse(null)
                );

        doAnswer(invocation -> {
            bookings.remove(booking);
            return null;
        }).when(bookingRepository).delete(anyString());

        List<Thread> threads =
                IntStream.range(0, 10)
                        .mapToObj(i -> new Thread(
                                () -> bookingController.deleteBooking(booking)
                        ))
                        .peek(Thread::start)
                        .collect(Collectors.toList());

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .until(() -> threads.stream().noneMatch(Thread::isAlive));

        assertThat(bookings).isEmpty();
    }

    @Test
    public void testUpdatingSameBookingAtSameTimeEndsWithOneCorrectUpdate() {
        List<Booking> bookings = new ArrayList<>();

        Booking original =
                new Booking(
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-10",
                        "19:30",
                        "Old request"
                );

        bookings.add(original);

        Booking updated =
                new Booking(
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-11",
                        "20:00",
                        "Updated request"
                );

        when(bookingRepository.findById(anyString()))
                .thenAnswer(invocation ->
                        bookings.stream().findFirst().orElse(null)
                );

        doAnswer(invocation -> {
            Booking bookingFromController = invocation.getArgument(0);
            bookings.clear();
            bookings.add(bookingFromController);
            return null;
        }).when(bookingRepository).update(any(Booking.class));

        List<Thread> threads =
                IntStream.range(0, 10)
                        .mapToObj(i -> new Thread(
                                () -> bookingController.updateBooking(updated)
                        ))
                        .peek(Thread::start)
                        .collect(Collectors.toList());

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .until(() -> threads.stream().noneMatch(Thread::isAlive));

        assertThat(bookings).containsExactly(updated);
    }
}