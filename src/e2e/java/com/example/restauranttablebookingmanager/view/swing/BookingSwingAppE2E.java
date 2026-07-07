package com.example.restauranttablebookingmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;

@RunWith(GUITestRunner.class)
public class BookingSwingAppE2E extends AssertJSwingJUnitTestCase {

    @ClassRule
    public static final MongoDBContainer mongo =
            new MongoDBContainer("mongo:4.4.3");

    private static final String DB_NAME = "test-db";
    private static final String COLLECTION_NAME = "test-collection";

    private static final String BOOKING_FIXTURE_1_ID = "TBL-001";
    private static final String BOOKING_FIXTURE_1_CUSTOMER = "Marco Rossi";
    private static final String BOOKING_FIXTURE_1_TABLE = "T01";
    private static final String BOOKING_FIXTURE_1_DATE = "2026-07-10";
    private static final String BOOKING_FIXTURE_1_TIME = "19:30";
    private static final String BOOKING_FIXTURE_1_SPECIAL_REQUEST = "Window seat";

    private static final String BOOKING_FIXTURE_2_ID = "TBL-002";
    private static final String BOOKING_FIXTURE_2_CUSTOMER = "Giulia Bianchi";
    private static final String BOOKING_FIXTURE_2_TABLE = "T02";
    private static final String BOOKING_FIXTURE_2_DATE = "2026-07-11";
    private static final String BOOKING_FIXTURE_2_TIME = "20:00";
    private static final String BOOKING_FIXTURE_2_SPECIAL_REQUEST = "Birthday dinner";

    private MongoClient mongoClient;
    private FrameFixture window;

    @Override
    protected void onSetUp() throws Exception {
        String containerIpAddress = mongo.getContainerIpAddress();
        Integer mappedPort = mongo.getFirstMappedPort();

        mongoClient = new MongoClient(containerIpAddress, mappedPort);
        mongoClient.getDatabase(DB_NAME).drop();

        addTestBookingToDatabase(
                BOOKING_FIXTURE_1_ID,
                BOOKING_FIXTURE_1_CUSTOMER,
                BOOKING_FIXTURE_1_TABLE,
                BOOKING_FIXTURE_1_DATE,
                BOOKING_FIXTURE_1_TIME,
                BOOKING_FIXTURE_1_SPECIAL_REQUEST
        );

        addTestBookingToDatabase(
                BOOKING_FIXTURE_2_ID,
                BOOKING_FIXTURE_2_CUSTOMER,
                BOOKING_FIXTURE_2_TABLE,
                BOOKING_FIXTURE_2_DATE,
                BOOKING_FIXTURE_2_TIME,
                BOOKING_FIXTURE_2_SPECIAL_REQUEST
        );

        application("com.example.restauranttablebookingmanager.app.swing.BookingSwingApp")
                .withArgs(
                        "--mongo-host=" + containerIpAddress,
                        "--mongo-port=" + mappedPort.toString(),
                        "--db-name=" + DB_NAME,
                        "--db-collection=" + COLLECTION_NAME
                )
                .start();

        window = WindowFinder.findFrame(
                new GenericTypeMatcher<JFrame>(JFrame.class) {
                    @Override
                    protected boolean isMatching(JFrame frame) {
                        return "Restaurant Table Booking Manager".equals(frame.getTitle())
                                && frame.isShowing();
                    }
                }
        ).using(robot());
    }

    @Override
    protected void onTearDown() {
        mongoClient.close();
    }
    
    @Test
    @GUITest
    public void testOnStartAllDatabaseElementsAreShown() {
        assertThat(window.list("bookingList").contents())
                .anySatisfy(e ->
                        assertThat(e).contains(
                                BOOKING_FIXTURE_1_ID,
                                BOOKING_FIXTURE_1_CUSTOMER,
                                BOOKING_FIXTURE_1_TABLE,
                                BOOKING_FIXTURE_1_DATE,
                                BOOKING_FIXTURE_1_TIME,
                                BOOKING_FIXTURE_1_SPECIAL_REQUEST
                        )
                )
                .anySatisfy(e ->
                        assertThat(e).contains(
                                BOOKING_FIXTURE_2_ID,
                                BOOKING_FIXTURE_2_CUSTOMER,
                                BOOKING_FIXTURE_2_TABLE,
                                BOOKING_FIXTURE_2_DATE,
                                BOOKING_FIXTURE_2_TIME,
                                BOOKING_FIXTURE_2_SPECIAL_REQUEST
                        )
                );
    }

    @Test
    @GUITest
    public void testAddButtonSuccess() {

        window.textBox("bookingidTextBox").enterText("TBL-003");
        window.textBox("customernameTextBox").enterText("Luca Ferrari");
        window.textBox("tablenumberTextBox").enterText("T03");
        window.textBox("bookingdateTextBox").enterText("2026-07-12");
        window.textBox("bookingtimeTextBox").enterText("18:30");
        window.textBox("specialrequestTextBox").enterText("Anniversary");

        window.button(JButtonMatcher.withText("Add Booking")).click();

        assertThat(window.list("bookingList").contents())
                .anySatisfy(e ->
                        assertThat(e).contains(
                                "TBL-003",
                                "Luca Ferrari",
                                "T03",
                                "2026-07-12",
                                "18:30",
                                "Anniversary"
                        )
                );
    }

    @Test
    @GUITest
    public void testAddButtonError() {

        window.textBox("bookingidTextBox")
                .enterText(BOOKING_FIXTURE_1_ID);

        window.textBox("customernameTextBox")
                .enterText("New Customer");

        window.textBox("tablenumberTextBox")
                .enterText("T09");

        window.textBox("bookingdateTextBox")
                .enterText("2026-07-21");

        window.textBox("bookingtimeTextBox")
                .enterText("21:00");

        window.textBox("specialrequestTextBox")
                .enterText("Duplicate booking");

        window.button(JButtonMatcher.withText("Add Booking")).click();

        assertThat(window.label("errorMessageLabel").text())
                .contains(
                        BOOKING_FIXTURE_1_ID,
                        BOOKING_FIXTURE_1_CUSTOMER,
                        BOOKING_FIXTURE_1_TABLE,
                        BOOKING_FIXTURE_1_DATE,
                        BOOKING_FIXTURE_1_TIME,
                        BOOKING_FIXTURE_1_SPECIAL_REQUEST
                );
    }
    
    @Test
    @GUITest
    public void testDeleteButtonSuccess() {

        window.list("bookingList")
                .selectItem(Pattern.compile(
                        ".*" + BOOKING_FIXTURE_1_CUSTOMER + ".*"
                ));

        window.button(JButtonMatcher.withText("Delete Booking")).click();

        assertThat(window.list("bookingList").contents())
                .noneMatch(e -> e.contains(BOOKING_FIXTURE_1_CUSTOMER));
    }

    @Test
    @GUITest
    public void testDeleteButtonError() {

        window.list("bookingList")
                .selectItem(Pattern.compile(
                        ".*" + BOOKING_FIXTURE_1_CUSTOMER + ".*"
                ));

        removeTestBookingFromDatabase(BOOKING_FIXTURE_1_ID);

        window.button(JButtonMatcher.withText("Delete Booking")).click();

        assertThat(window.label("errorMessageLabel").text())
                .contains(
                        BOOKING_FIXTURE_1_ID,
                        BOOKING_FIXTURE_1_CUSTOMER,
                        BOOKING_FIXTURE_1_TABLE,
                        BOOKING_FIXTURE_1_DATE,
                        BOOKING_FIXTURE_1_TIME,
                        BOOKING_FIXTURE_1_SPECIAL_REQUEST
                );
    }

    @Test
    @GUITest
    public void testUpdateButtonSuccess() {

        window.list("bookingList")
                .selectItem(Pattern.compile(
                        ".*" + BOOKING_FIXTURE_1_CUSTOMER + ".*"
                ));

        window.textBox("customernameTextBox")
                .setText("Marco Rossi");

        window.textBox("tablenumberTextBox")
                .setText("T01");

        window.textBox("bookingdateTextBox")
                .setText("2026-07-22");

        window.textBox("bookingtimeTextBox")
                .setText("20:30");

        window.textBox("specialrequestTextBox")
                .setText("Updated request");

        window.button(JButtonMatcher.withText("Update Booking")).click();

        assertThat(window.list("bookingList").contents())
                .anySatisfy(e ->
                        assertThat(e).contains(
                                "Marco Rossi",
                                "T01",
                                "2026-07-22",
                                "20:30",
                                "Updated request"
                        )
                );
    }

    private void addTestBookingToDatabase(
            String bookingId,
            String customerName,
            String tableNumber,
            String bookingDate,
            String bookingTime,
            String specialRequest) {

        mongoClient
                .getDatabase(DB_NAME)
                .getCollection(COLLECTION_NAME)
                .insertOne(
                        new Document()
                                .append("bookingId", bookingId)
                                .append("customerName", customerName)
                                .append("tableNumber", tableNumber)
                                .append("bookingDate", bookingDate)
                                .append("bookingTime", bookingTime)
                                .append("specialRequest", specialRequest)
                );
    }

    private void removeTestBookingFromDatabase(String bookingId) {

        mongoClient
                .getDatabase(DB_NAME)
                .getCollection(COLLECTION_NAME)
                .deleteOne(
                        Filters.eq("bookingId", bookingId)
                );
    }
}