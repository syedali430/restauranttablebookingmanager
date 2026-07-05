package com.example.restauranttablebookingmanager.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.example.restauranttablebookingmanager.model.Booking;
import com.example.restauranttablebookingmanager.repository.BookingRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class BookingMongoRepository implements BookingRepository {

    private final MongoCollection<Document> bookingCollection;

    private static final String ID = "bookingId";
    private static final String CUSTOMER = "customerName";
    private static final String TABLE = "tableNumber";
    private static final String DATE = "bookingDate";
    private static final String TIME = "bookingTime";
    private static final String SPECIAL_REQUEST = "specialRequest";

    public BookingMongoRepository(
            MongoClient client,
            String databaseName,
            String collectionName) {

        this.bookingCollection =
                client.getDatabase(databaseName)
                        .getCollection(collectionName);
    }

    @Override
    public void save(Booking booking) {

        bookingCollection.insertOne(
                new Document()
                        .append(ID, booking.getBookingId())
                        .append(CUSTOMER, booking.getCustomerName())
                        .append(TABLE, booking.getTableNumber())
                        .append(DATE, booking.getBookingDate())
                        .append(TIME, booking.getBookingTime())
                        .append(SPECIAL_REQUEST, booking.getSpecialRequest())
        );
    }

    @Override
    public List<Booking> findAll() {

        return StreamSupport
                .stream(bookingCollection.find().spliterator(), false)
                .map(this::fromDocument)
                .collect(Collectors.toList());
    }

    @Override
    public Booking findById(String bookingId) {

        Document d =
                bookingCollection.find(Filters.eq(ID, bookingId))
                        .first();

        return d != null ? fromDocument(d) : null;
    }

    @Override
    public void update(Booking booking) {

        Document update =
                new Document()
                        .append(CUSTOMER, booking.getCustomerName())
                        .append(TABLE, booking.getTableNumber())
                        .append(DATE, booking.getBookingDate())
                        .append(TIME, booking.getBookingTime())
                        .append(SPECIAL_REQUEST, booking.getSpecialRequest());

        bookingCollection.updateOne(
                Filters.eq(ID, booking.getBookingId()),
                new Document("$set", update)
        );
    }

    @Override
    public void delete(String bookingId) {

        bookingCollection.deleteOne(
                Filters.eq(ID, bookingId)
        );
    }

    private Booking fromDocument(Document d) {

        return new Booking(
                "" + d.get(ID),
                "" + d.get(CUSTOMER),
                "" + d.get(TABLE),
                "" + d.get(DATE),
                "" + d.get(TIME),
                "" + d.get(SPECIAL_REQUEST)
        );
    }
}
