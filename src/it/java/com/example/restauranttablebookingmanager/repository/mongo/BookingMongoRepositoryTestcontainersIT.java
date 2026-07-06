package com.example.restauranttablebookingmanager.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.example.restauranttablebookingmanager.model.Booking;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class BookingMongoRepositoryTestcontainersIT {

    @ClassRule
    public static final MongoDBContainer mongo =
            new MongoDBContainer("mongo:4.4.3");

    private MongoClient client;
    private BookingMongoRepository bookingRepository;
    private MongoCollection<Document> bookingCollection;

    public static final String BOOKING_COLLECTION_NAME = "booking";
    public static final String BOOKING_DB_NAME =
            "restauranttablebookingmanager";

    @Before
    public void setup() {

        client =
                new MongoClient(
                        new ServerAddress(
                                mongo.getHost(),
                                mongo.getFirstMappedPort()
                        )
                );

        bookingRepository =
                new BookingMongoRepository(
                        client,
                        BOOKING_DB_NAME,
                        BOOKING_COLLECTION_NAME
                );

        MongoDatabase database =
                client.getDatabase(BOOKING_DB_NAME);

        database.drop();

        bookingCollection =
                database.getCollection(BOOKING_COLLECTION_NAME);
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Test
    public void testFindAll() {

        addTestBookingToDatabase(
                "TBL-001",
                "Marco Rossi",
                "T01",
                "2026-07-10",
                "19:30",
                "Window seat"
        );

        addTestBookingToDatabase(
                "TBL-002",
                "Giulia Bianchi",
                "T02",
                "2026-07-11",
                "20:00",
                "Birthday dinner"
        );

        assertThat(bookingRepository.findAll())
                .containsExactly(
                        new Booking(
                                "TBL-001",
                                "Marco Rossi",
                                "T01",
                                "2026-07-10",
                                "19:30",
                                "Window seat"
                        ),
                        new Booking(
                                "TBL-002",
                                "Giulia Bianchi",
                                "T02",
                                "2026-07-11",
                                "20:00",
                                "Birthday dinner"
                        )
                );
    }

    @Test
    public void testFindById() {

        addTestBookingToDatabase(
                "TBL-001",
                "Marco Rossi",
                "T01",
                "2026-07-10",
                "19:30",
                "Window seat"
        );

        addTestBookingToDatabase(
                "TBL-002",
                "Giulia Bianchi",
                "T02",
                "2026-07-11",
                "20:00",
                "Birthday dinner"
        );

        assertThat(bookingRepository.findById("TBL-002"))
                .isEqualTo(
                        new Booking(
                                "TBL-002",
                                "Giulia Bianchi",
                                "T02",
                                "2026-07-11",
                                "20:00",
                                "Birthday dinner"
                        )
                );
    }

    @Test
    public void testSave() {

        Booking booking =
                new Booking(
                        "TBL-003",
                        "Luca Ferrari",
                        "T03",
                        "2026-07-12",
                        "18:30",
                        "Anniversary"
                );

        bookingRepository.save(booking);

        assertThat(readAllBookingsFromDatabase())
                .containsExactly(booking);
    }

    @Test
    public void testDelete() {

        addTestBookingToDatabase(
                "TBL-004",
                "Sofia Romano",
                "T04",
                "2026-07-13",
                "21:00",
                "Delete me"
        );

        bookingRepository.delete("TBL-004");

        assertThat(readAllBookingsFromDatabase()).isEmpty();
    }

    @Test
    public void testUpdate() {

        Booking original =
                new Booking(
                        "TBL-005",
                        "Matteo Ricci",
                        "T05",
                        "2026-07-14",
                        "19:00",
                        "Original request"
                );

        bookingRepository.save(original);

        assertThat(readAllBookingsFromDatabase())
                .containsExactly(original);

        Booking updated =
                new Booking(
                        "TBL-005",
                        "Matteo Ricci",
                        "T05",
                        "2026-07-15",
                        "20:30",
                        "Updated request"
                );

        bookingRepository.update(updated);

        assertThat(readAllBookingsFromDatabase())
                .containsExactly(updated);
    }

    private void addTestBookingToDatabase(
            String bookingId,
            String customerName,
            String tableNumber,
            String bookingDate,
            String bookingTime,
            String specialRequest) {

        bookingCollection.insertOne(
                new Document()
                        .append("bookingId", bookingId)
                        .append("customerName", customerName)
                        .append("tableNumber", tableNumber)
                        .append("bookingDate", bookingDate)
                        .append("bookingTime", bookingTime)
                        .append("specialRequest", specialRequest)
        );
    }

    private List<Booking> readAllBookingsFromDatabase() {

        return StreamSupport
                .stream(bookingCollection.find().spliterator(), false)
                .map(d -> new Booking(
                        "" + d.get("bookingId"),
                        "" + d.get("customerName"),
                        "" + d.get("tableNumber"),
                        "" + d.get("bookingDate"),
                        "" + d.get("bookingTime"),
                        "" + d.get("specialRequest")
                ))
                .collect(Collectors.toList());
    }
}