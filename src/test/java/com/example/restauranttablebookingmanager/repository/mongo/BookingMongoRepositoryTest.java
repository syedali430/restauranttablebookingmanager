package com.example.restauranttablebookingmanager.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.restauranttablebookingmanager.model.Booking;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class BookingMongoRepositoryTest {

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient client;
    private BookingMongoRepository bookingMongoRepository;
    private MongoCollection<Document> bookingCollection;

    public static final String BOOKING_COLLECTION_NAME = "booking";
    public static final String BOOKINGMANAGER_DB_NAME = "restauranttablebookingmanager";

    @BeforeClass
    public static void setupServer() {
        server = new MongoServer(new MemoryBackend());
        serverAddress = server.bind();
    }

    @AfterClass
    public static void shutdownServer() {
        server.shutdown();
    }

    @Before
    public void setup() {
        client = new MongoClient(new ServerAddress(serverAddress));

        bookingMongoRepository =
                new BookingMongoRepository(
                        client,
                        BOOKINGMANAGER_DB_NAME,
                        BOOKING_COLLECTION_NAME
                );

        MongoDatabase database =
                client.getDatabase(BOOKINGMANAGER_DB_NAME);

        database.drop();

        bookingCollection =
                database.getCollection(BOOKING_COLLECTION_NAME);
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Test
    public void testFindAllWhenDatabaseIsEmpty() {
        assertThat(bookingMongoRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindAllWhenDatabaseIsNotEmpty() {
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

        assertThat(bookingMongoRepository.findAll())
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
    public void testFindByIdNotFound() {
        assertThat(bookingMongoRepository.findById("TBL-001")).isNull();
    }

    @Test
    public void testFindByIdFound() {
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

        assertThat(bookingMongoRepository.findById("TBL-002"))
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
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-10",
                        "19:30",
                        "Window seat"
                );

        bookingMongoRepository.save(booking);

        assertThat(readAllBookingsFromDatabase())
                .containsExactly(booking);
    }

    @Test
    public void testDelete() {
        addTestBookingToDatabase(
                "TBL-001",
                "Marco Rossi",
                "T01",
                "2026-07-10",
                "19:30",
                "Window seat"
        );

        bookingMongoRepository.delete("TBL-001");

        assertThat(readAllBookingsFromDatabase()).isEmpty();
    }

    @Test
    public void testUpdate() {
        Booking original =
                new Booking(
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-10",
                        "19:30",
                        "Window seat"
                );

        bookingMongoRepository.save(original);

        assertThat(readAllBookingsFromDatabase())
                .containsExactly(original);

        Booking updated =
                new Booking(
                        "TBL-001",
                        "Marco Rossi",
                        "T01",
                        "2026-07-12",
                        "20:30",
                        "Anniversary dinner"
                );

        bookingMongoRepository.update(updated);

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
