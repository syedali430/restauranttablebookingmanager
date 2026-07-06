package com.example.restauranttablebookingmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.example.restauranttablebookingmanager.controller.BookingController;
import com.example.restauranttablebookingmanager.model.Booking;
import com.example.restauranttablebookingmanager.repository.mongo.BookingMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@RunWith(GUITestRunner.class)
public class ModelViewControllerIT extends AssertJSwingJUnitTestCase {

    @ClassRule
    public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

    private MongoClient mongoClient;
    private FrameFixture window;
    private BookingMongoRepository bookingRepository;
    private BookingController bookingController;

    public static final String BOOKING_COLLECTION_NAME="booking";
    public static final String BOOKING_DB_NAME="restauranttablebookingmanager";

    @Override
    protected void onSetUp() {
        mongoClient=new MongoClient(new ServerAddress(mongo.getHost(),mongo.getFirstMappedPort()));
        bookingRepository=new BookingMongoRepository(mongoClient,BOOKING_DB_NAME,BOOKING_COLLECTION_NAME);

        for(Booking booking:bookingRepository.findAll()){
            bookingRepository.delete(booking.getBookingId());
        }

        window=new FrameFixture(robot(),GuiActionRunner.execute(() -> {
            BookingSwingView bookingSwingView=new BookingSwingView();
            bookingController=new BookingController(bookingRepository,bookingSwingView);
            bookingSwingView.setBookingController(bookingController);
            return bookingSwingView;
        }));
        window.show();
    }

    @Override
    protected void onTearDown(){
        mongoClient.close();
    }

    @Test
    public void testAddBooking(){
        window.textBox("bookingidTextBox").enterText("TBL-001");
        window.textBox("customernameTextBox").enterText("Marco Rossi");
        window.textBox("tablenumberTextBox").enterText("T01");
        window.textBox("bookingdateTextBox").enterText("2026-07-10");
        window.textBox("bookingtimeTextBox").enterText("19:30");
        window.textBox("specialrequestTextBox").enterText("Window seat");
        window.button(JButtonMatcher.withText("Add Booking")).click();
        Awaitility.await().atMost(5,TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(bookingRepository.findById("TBL-001")).isEqualTo(
                new Booking("TBL-001","Marco Rossi","T01","2026-07-10","19:30","Window seat")));
    }

    @Test
    public void testDeleteBooking(){
        bookingRepository.save(new Booking("TBL-002","Giulia Bianchi","T02","2026-07-11","20:00","Birthday dinner"));
        GuiActionRunner.execute(() -> bookingController.getAllBookings());
        window.list("bookingList").selectItem(0);
        window.button(JButtonMatcher.withText("Delete Booking")).click();
        Awaitility.await().atMost(5,TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(bookingRepository.findById("TBL-002")).isNull());
    }

    @Test
    public void testUpdateBooking(){
        bookingRepository.save(new Booking("TBL-003","Luca Ferrari","T03","2026-07-12","18:30","Original request"));
        GuiActionRunner.execute(() -> bookingController.getAllBookings());
        window.list("bookingList").selectItem(0);
        window.textBox("bookingidTextBox").requireText("TBL-003");
        window.textBox("bookingidTextBox").requireDisabled();
        window.textBox("customernameTextBox").setText("Luca Ferrari");
        window.textBox("tablenumberTextBox").setText("T03");
        window.textBox("bookingdateTextBox").setText("2026-07-13");
        window.textBox("bookingtimeTextBox").setText("19:30");
        window.textBox("specialrequestTextBox").setText("Updated request");
        window.button(JButtonMatcher.withText("Update Booking")).click();
        Awaitility.await().atMost(5,TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(bookingRepository.findById("TBL-003")).isEqualTo(
                new Booking("TBL-003","Luca Ferrari","T03","2026-07-13","19:30","Updated request")));
        Awaitility.await().atMost(5,TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(window.list("bookingList").contents()).containsExactly(
                new Booking("TBL-003","Luca Ferrari","T03","2026-07-13","19:30","Updated request").toString()));
    }
}
