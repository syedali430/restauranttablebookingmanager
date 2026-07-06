package com.example.restauranttablebookingmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.restauranttablebookingmanager.controller.BookingController;
import com.example.restauranttablebookingmanager.model.Booking;
import com.example.restauranttablebookingmanager.repository.mongo.BookingMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

@RunWith(GUITestRunner.class)
public class BookingSwingViewIT extends AssertJSwingJUnitTestCase {

    private static MongoServer server;
    private static InetSocketAddress serverAddress;
    private MongoClient mongoClient;
    private FrameFixture window;
    private BookingSwingView bookingSwingView;
    private BookingController bookingController;
    private BookingMongoRepository bookingRepository;

    public static final String BOOKING_COLLECTION_NAME = "booking";
    public static final String BOOKING_DB_NAME = "restauranttablebookingmanager";

    @BeforeClass
    public static void setupServer() {
        server = new MongoServer(new MemoryBackend());
        serverAddress = server.bind();
    }

    @AfterClass
    public static void shutdownServer() {
        server.shutdown();
    }

    @Override
    protected void onSetUp() {
        mongoClient = new MongoClient(new ServerAddress(serverAddress));
        bookingRepository = new BookingMongoRepository(mongoClient, BOOKING_DB_NAME, BOOKING_COLLECTION_NAME);
        for (Booking booking : bookingRepository.findAll()) {
            bookingRepository.delete(booking.getBookingId());
        }
        GuiActionRunner.execute(() -> {
            bookingSwingView = new BookingSwingView();
            bookingController = new BookingController(bookingRepository, bookingSwingView);
            bookingSwingView.setBookingController(bookingController);
            return bookingSwingView;
        });
        window = new FrameFixture(robot(), bookingSwingView);
        window.show();
    }

    @Override
    protected void onTearDown() {
        mongoClient.close();
    }

    @Test @GUITest
    public void testAllBookings() {
        Booking b1 = new Booking("TBL-001","Marco Rossi","T01","2026-07-10","19:30","Window seat");
        Booking b2 = new Booking("TBL-002","Giulia Bianchi","T02","2026-07-11","20:00","Birthday dinner");
        bookingRepository.save(b1);
        bookingRepository.save(b2);
        GuiActionRunner.execute(() -> bookingController.getAllBookings());
        assertThat(window.list("bookingList").contents()).containsExactly(b1.toString(), b2.toString());
    }

    @Test @GUITest
    public void testAddButtonSuccess() {
        window.textBox("bookingidTextBox").enterText("TBL-003");
        window.textBox("customernameTextBox").enterText("Luca Ferrari");
        window.textBox("tablenumberTextBox").enterText("T03");
        window.textBox("bookingdateTextBox").enterText("2026-07-12");
        window.textBox("bookingtimeTextBox").enterText("18:30");
        window.textBox("specialrequestTextBox").enterText("Anniversary");
        window.button(JButtonMatcher.withText("Add Booking")).click();
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(window.list("bookingList").contents()).containsExactly(
                new Booking("TBL-003","Luca Ferrari","T03","2026-07-12","18:30","Anniversary").toString()));
    }

    @Test @GUITest
    public void testDeleteButtonSuccess() {
        GuiActionRunner.execute(() -> bookingController.addBooking(
            new Booking("TBL-004","Sofia Romano","T04","2026-07-13","21:00","Delete booking")));
        window.list("bookingList").selectItem(0);
        window.button(JButtonMatcher.withText("Delete Booking")).click();
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(window.list("bookingList").contents()).isEmpty());
    }

    @Test @GUITest
    public void testUpdateButtonSuccess() {
        Booking booking = new Booking("TBL-005","Matteo Ricci","T05","2026-07-14","19:00","Original request");
        bookingRepository.save(booking);
        GuiActionRunner.execute(() -> bookingSwingView.getListBookingModel().addElement(booking));
        window.list("bookingList").selectItem(0);
        window.textBox("bookingidTextBox").requireText("TBL-005");
        window.textBox("bookingidTextBox").requireDisabled();
        window.textBox("customernameTextBox").setText("Matteo Ricci");
        window.textBox("tablenumberTextBox").setText("T05");
        window.textBox("bookingdateTextBox").setText("2026-07-15");
        window.textBox("bookingtimeTextBox").setText("20:30");
        window.textBox("specialrequestTextBox").setText("Updated request");
        window.button(JButtonMatcher.withText("Update Booking")).click();
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(window.list("bookingList").contents()).containsExactly(
                new Booking("TBL-005","Matteo Ricci","T05","2026-07-15","20:30","Updated request").toString()));
    }
}
