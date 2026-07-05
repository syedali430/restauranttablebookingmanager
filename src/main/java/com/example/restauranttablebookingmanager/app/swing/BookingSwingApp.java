package com.example.restauranttablebookingmanager.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.restauranttablebookingmanager.controller.BookingController;
import com.example.restauranttablebookingmanager.repository.mongo.BookingMongoRepository;
import com.example.restauranttablebookingmanager.view.swing.BookingSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class BookingSwingApp implements Callable<Void> {

    @Option(names = { "--mongo-host" }, description = "MongoDB host address")
    private String mongoHost = "localhost";

    @Option(names = { "--mongo-port" }, description = "MongoDB host port")
    private int mongoPort = 27017;

    @Option(names = { "--db-name" }, description = "Database name")
    private String databaseName = "restauranttablebookingmanager";

    @Option(names = { "--db-collection" }, description = "Collection name")
    private String collectionName = "booking";

    public static void main(String[] args) {
        new CommandLine(new BookingSwingApp()).execute(args);
    }

    @Override
    public Void call() {

        EventQueue.invokeLater(() -> {

            try {

                BookingMongoRepository repository =
                        new BookingMongoRepository(
                                new MongoClient(
                                        new ServerAddress(
                                                mongoHost,
                                                mongoPort
                                        )
                                ),
                                databaseName,
                                collectionName
                        );

                BookingSwingView view =
                        new BookingSwingView();

                BookingController controller =
                        new BookingController(
                                repository,
                                view
                        );

                view.setBookingController(controller);
                view.setVisible(true);

                controller.getAllBookings();

            } catch (Exception e) {

                Logger.getLogger(getClass().getName())
                        .log(Level.SEVERE, "Exception", e);
            }
        });

        return null;
    }
}
