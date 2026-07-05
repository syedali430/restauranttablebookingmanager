package com.example.restauranttablebookingmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.restauranttablebookingmanager.controller.BookingController;
import com.example.restauranttablebookingmanager.model.Booking;

@RunWith(GUITestRunner.class)
public class BookingSwingViewTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private BookingSwingView bookingSwingView;

    @Mock
    private BookingController bookingController;

    private AutoCloseable closeable;

    private static final String ID = "TBL-001";
    private static final String CUSTOMER = "Marco Rossi";
    private static final String TABLE = "T01";
    private static final String DATE = "2026-07-10";
    private static final String TIME = "19:30";
    private static final String SPECIAL_REQUEST = "Window seat";

    private static final Booking BOOKING =
            new Booking(ID, CUSTOMER, TABLE, DATE, TIME, SPECIAL_REQUEST);

    @Override
    protected void onSetUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);

        GuiActionRunner.execute(() -> {
            bookingSwingView = new BookingSwingView();
            bookingSwingView.setBookingController(bookingController);
            return bookingSwingView;
        });

        window = new FrameFixture(robot(), bookingSwingView);
        window.show();
    }

    @Override
    protected void onTearDown() throws Exception {
        closeable.close();
    }

    private void typeAllFieldsValidSoAddEnables() {
        window.textBox("bookingidTextBox").enterText(ID);
        window.textBox("customernameTextBox").enterText(CUSTOMER);
        window.textBox("tablenumberTextBox").enterText(TABLE);
        window.textBox("bookingdateTextBox").enterText(DATE);
        window.textBox("bookingtimeTextBox").enterText(TIME);
        window.textBox("specialrequestTextBox").enterText(SPECIAL_REQUEST);
    }

    private void clickAddBooking() {
        window.button(JButtonMatcher.withText("Add Booking")).click();
    }

    private void clickUpdateBooking() {
        window.button(JButtonMatcher.withText("Update Booking")).click();
    }

    private void clickDeleteBooking() {
        window.button(JButtonMatcher.withText("Delete Booking")).click();
    }

    private void clickClear() {
        window.button(JButtonMatcher.withText("Clear")).click();
    }

    private String makeStringOfLength(int n) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; i++) {
            sb.append("s");
        }

        return sb.toString();
    }

    private boolean invokePrivateBooleanMethod(String methodName, String value)
            throws Exception {

        java.lang.reflect.Method method =
                BookingSwingView.class.getDeclaredMethod(
                        methodName,
                        String.class
                );

        method.setAccessible(true);

        return ((Boolean) method.invoke(bookingSwingView, value))
                .booleanValue();
    }

    @Test
    @GUITest
    public void testInitialStateButtonsDisabledFieldsEmptyLabelsCorrectAndBookingListEmpty() {
        window.label(JLabelMatcher.withText("Booking ID")).requireVisible();
        window.label(JLabelMatcher.withText("Customer Name")).requireVisible();
        window.label(JLabelMatcher.withText("Table Number")).requireVisible();
        window.label(JLabelMatcher.withText("Booking Date")).requireVisible();
        window.label(JLabelMatcher.withText("Booking Time")).requireVisible();
        window.label(JLabelMatcher.withText("Special Request")).requireVisible();

        window.textBox("bookingidTextBox").requireEmpty();
        window.textBox("customernameTextBox").requireEmpty();
        window.textBox("tablenumberTextBox").requireEmpty();
        window.textBox("bookingdateTextBox").requireEmpty();
        window.textBox("bookingtimeTextBox").requireEmpty();
        window.textBox("specialrequestTextBox").requireEmpty();

        window.button("btnAddBooking").requireDisabled();
        window.button("btnClear").requireDisabled();
        window.button("btnUpdateBooking").requireDisabled();
        window.button("btnDeleteBooking").requireDisabled();

        assertThat(window.list("bookingList").contents()).isEmpty();

        window.label("errorMessageLabel").requireText("");
    }

    @Test
    @GUITest
    public void testWhenAllFieldsAreFilledAddBookingButtonBecomesEnabled() {
        typeAllFieldsValidSoAddEnables();

        window.button("btnAddBooking").requireEnabled();
    }

    @Test
    @GUITest
    public void testWhenAnyFieldIsEmptyAddBookingButtonStaysDisabled() {
        window.textBox("bookingidTextBox").enterText(" ");
        window.textBox("customernameTextBox").enterText(CUSTOMER);
        window.textBox("tablenumberTextBox").enterText(TABLE);
        window.textBox("bookingdateTextBox").enterText(DATE);
        window.textBox("bookingtimeTextBox").enterText(TIME);

        window.button("btnAddBooking").requireDisabled();
    }

    @Test
    @GUITest
    public void testWhenBookingIsSelectedFieldsFilledBookingIdDisabledUpdateDeleteClearEnabledAndAddDisabled() {
        GuiActionRunner.execute(() ->
                bookingSwingView.getListBookingModel()
                        .addElement(BOOKING)
        );

        window.list("bookingList").selectItem(0);

        window.textBox("bookingidTextBox").requireText(ID);
        window.textBox("customernameTextBox").requireText(CUSTOMER);
        window.textBox("tablenumberTextBox").requireText(TABLE);
        window.textBox("bookingdateTextBox").requireText(DATE);
        window.textBox("bookingtimeTextBox").requireText(TIME);
        window.textBox("specialrequestTextBox").requireText(SPECIAL_REQUEST);

        window.textBox("bookingidTextBox").requireDisabled();

        window.button("btnUpdateBooking").requireEnabled();
        window.button("btnDeleteBooking").requireEnabled();
        window.button("btnClear").requireEnabled();
        window.button("btnAddBooking").requireDisabled();
    }

    @Test
    @GUITest
    public void testWhenClearButtonIsClickedFieldsBecomeEmptyAndSelectionIsCleared() {
        GuiActionRunner.execute(() ->
                bookingSwingView.getListBookingModel()
                        .addElement(BOOKING)
        );

        window.list("bookingList").selectItem(0);
        clickClear();

        window.textBox("bookingidTextBox").requireText("");
        window.textBox("customernameTextBox").requireText("");
        window.textBox("tablenumberTextBox").requireText("");
        window.textBox("bookingdateTextBox").requireText("");
        window.textBox("bookingtimeTextBox").requireText("");
        window.textBox("specialrequestTextBox").requireText("");

        assertThat(window.list("bookingList").selection()).isEmpty();

        window.button("btnUpdateBooking").requireDisabled();
        window.button("btnDeleteBooking").requireDisabled();
        window.button("btnAddBooking").requireDisabled();
    }

    @Test
    @GUITest
    public void testWhenWindowStartsAndBookingsAreLoadedTheyAppearInTheList() {
        Booking booking2 =
                new Booking(
                        "TBL-002",
                        "Giulia Bianchi",
                        "T02",
                        "2026-07-11",
                        "20:00",
                        "Birthday dinner"
                );

        GuiActionRunner.execute(() ->
                bookingSwingView.displayBookings(
                        Arrays.asList(BOOKING, booking2)
                )
        );

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        assertThat(window.list("bookingList").contents())
                                .containsExactly(
                                        BOOKING.toString(),
                                        booking2.toString()
                                )
                );
    }

    @Test
    @GUITest
    public void testWhenAddBookingButtonIsClickedBookingIsAddedInTheBookingList() {
        typeAllFieldsValidSoAddEnables();
        clickAddBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(bookingController)
                                .addBooking(BOOKING)
                );

        GuiActionRunner.execute(() ->
                bookingSwingView.addBooking(BOOKING)
        );

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        assertThat(window.list("bookingList").contents())
                                .containsExactly(BOOKING.toString())
                );
    }

    @Test
    @GUITest
    public void testWhenDeleteBookingButtonIsClickedBookingIsDeletedFromTheBookingList() {
        Booking booking2 =
                new Booking(
                        "TBL-002",
                        "Giulia Bianchi",
                        "T02",
                        "2026-07-11",
                        "20:00",
                        "Birthday dinner"
                );

        GuiActionRunner.execute(() -> {
            DefaultListModel<Booking> model =
                    bookingSwingView.getListBookingModel();

            model.addElement(BOOKING);
            model.addElement(booking2);
        });

        window.list("bookingList").selectItem(1);
        clickDeleteBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(bookingController)
                                .deleteBooking(booking2)
                );

        GuiActionRunner.execute(() ->
                bookingSwingView.deleteBooking(booking2)
        );

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        assertThat(window.list("bookingList").contents())
                                .containsExactly(BOOKING.toString())
                );
    }

    @Test
    @GUITest
    public void testWhenUpdateBookingButtonIsClickedBookingIsUpdatedInTheBookingList() {
        GuiActionRunner.execute(() ->
                bookingSwingView.getListBookingModel()
                        .addElement(BOOKING)
        );

        window.list("bookingList").selectItem(0);

        window.textBox("bookingdateTextBox").setText("");
        window.textBox("bookingdateTextBox").enterText("2026-07-12");

        window.textBox("bookingtimeTextBox").setText("");
        window.textBox("bookingtimeTextBox").enterText("20:30");

        Booking updated =
                new Booking(
                        ID,
                        CUSTOMER,
                        TABLE,
                        "2026-07-12",
                        "20:30",
                        SPECIAL_REQUEST
                );

        clickUpdateBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(bookingController)
                                .updateBooking(updated)
                );

        GuiActionRunner.execute(() ->
                bookingSwingView.updateBooking(updated)
        );

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        assertThat(window.list("bookingList").contents())
                                .containsExactly(updated.toString())
                );
    }

    @Test
    @GUITest
    public void testWhenBookingIdIsWrongBookingCannotBeAddedToBookingListAndErrorMessageIsShown() {
        window.textBox("bookingidTextBox").enterText("NL+-23");
        window.textBox("customernameTextBox").enterText(CUSTOMER);
        window.textBox("tablenumberTextBox").enterText(TABLE);
        window.textBox("bookingdateTextBox").enterText(DATE);
        window.textBox("bookingtimeTextBox").enterText(TIME);
        window.textBox("specialrequestTextBox").enterText(SPECIAL_REQUEST);

        clickAddBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        window.label("errorMessageLabel")
                                .requireText(
                                        "Booking ID must be TBL- followed by 3 digits (e.g., TBL-001)"
                                )
                );

        verify(bookingController, never()).addBooking(BOOKING);
    }

    @Test
    @GUITest
    public void testWhenCustomerNameHasWrongCharactersBookingCannotBeAddedToBookingListAndErrorMessageIsShown() {
        window.textBox("bookingidTextBox").enterText(ID);
        window.textBox("customernameTextBox").enterText("Marco 123");
        window.textBox("tablenumberTextBox").enterText(TABLE);
        window.textBox("bookingdateTextBox").enterText(DATE);
        window.textBox("bookingtimeTextBox").enterText(TIME);
        window.textBox("specialrequestTextBox").enterText(SPECIAL_REQUEST);

        clickAddBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        window.label("errorMessageLabel")
                                .requireText(
                                        "Customer Name must contain only letters and spaces"
                                )
                );

        verify(bookingController, never()).addBooking(BOOKING);
    }

    @Test
    @GUITest
    public void testWhenTableNumberHasWrongCharactersBookingCannotBeAddedToBookingListAndErrorMessageIsShown() {
        window.textBox("bookingidTextBox").enterText(ID);
        window.textBox("customernameTextBox").enterText(CUSTOMER);
        window.textBox("tablenumberTextBox").enterText("Table99");
        window.textBox("bookingdateTextBox").enterText(DATE);
        window.textBox("bookingtimeTextBox").enterText(TIME);
        window.textBox("specialrequestTextBox").enterText(SPECIAL_REQUEST);

        clickAddBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        window.label("errorMessageLabel")
                                .requireText(
                                        "Table Number must be T followed by 2 digits (e.g., T01)"
                                )
                );

        verify(bookingController, never()).addBooking(BOOKING);
    }

    @Test
    @GUITest
    public void testWhenBookingDateIsWrongBookingCannotBeAddedToBookingListAndErrorMessageIsShown() {
        window.textBox("bookingidTextBox").enterText(ID);
        window.textBox("customernameTextBox").enterText(CUSTOMER);
        window.textBox("tablenumberTextBox").enterText(TABLE);
        window.textBox("bookingdateTextBox").enterText("10-07-2026");
        window.textBox("bookingtimeTextBox").enterText(TIME);
        window.textBox("specialrequestTextBox").enterText(SPECIAL_REQUEST);

        clickAddBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        window.label("errorMessageLabel")
                                .requireText(
                                        "Booking Date must be in format YYYY-MM-DD"
                                )
                );

        verify(bookingController, never()).addBooking(BOOKING);
    }

    @Test
    @GUITest
    public void testWhenBookingTimeIsWrongBookingCannotBeAddedToBookingListAndErrorMessageIsShown() {
        window.textBox("bookingidTextBox").enterText(ID);
        window.textBox("customernameTextBox").enterText(CUSTOMER);
        window.textBox("tablenumberTextBox").enterText(TABLE);
        window.textBox("bookingdateTextBox").enterText(DATE);
        window.textBox("bookingtimeTextBox").enterText("19-30");
        window.textBox("specialrequestTextBox").enterText(SPECIAL_REQUEST);

        clickAddBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        window.label("errorMessageLabel")
                                .requireText(
                                        "Booking Time must be in format HH:MM"
                                )
                );

        verify(bookingController, never()).addBooking(BOOKING);
    }

    @Test
    @GUITest
    public void testWhenSpecialRequestIsOver200CharactersBookingCannotBeAddedToBookingListAndErrorMessageIsShown() {
        window.textBox("bookingidTextBox").enterText(ID);
        window.textBox("customernameTextBox").enterText(CUSTOMER);
        window.textBox("tablenumberTextBox").enterText(TABLE);
        window.textBox("bookingdateTextBox").enterText(DATE);
        window.textBox("bookingtimeTextBox").enterText(TIME);
        window.textBox("specialrequestTextBox").enterText(makeStringOfLength(201));

        clickAddBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        window.label("errorMessageLabel")
                                .requireText(
                                        "Special Request cannot exceed 200 characters"
                                )
                );

        verify(bookingController, never()).addBooking(BOOKING);
    }

    @Test
    @GUITest
    public void testUpdateBookingDoesNothingWhenFormIsInvalid() {
        GuiActionRunner.execute(() ->
                bookingSwingView.getListBookingModel()
                        .addElement(BOOKING)
        );

        window.list("bookingList").selectItem(0);

        window.textBox("bookingidTextBox").requireDisabled();
        window.textBox("customernameTextBox").setText("123");

        clickUpdateBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(bookingController, never())
                                .updateBooking(any(Booking.class))
                );
    }

    @Test
    @GUITest
    public void testDeleteBookingDoesNothingWhenNoSelection() {
        clickDeleteBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(bookingController, never())
                                .deleteBooking(any(Booking.class))
                );
    }

    @Test
    @GUITest
    public void testShowErrorMessageWithBookingRemovesThatBookingFromList() {
        GuiActionRunner.execute(() ->
                bookingSwingView.getListBookingModel()
                        .addElement(BOOKING)
        );

        GuiActionRunner.execute(() ->
                bookingSwingView.showErrorMessage("Error: ", BOOKING)
        );

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertThat(window.list("bookingList").contents())
                            .isEmpty();

                    window.label("errorMessageLabel")
                            .requireText("Error: " + BOOKING.toString());
                });
    }

    @Test
    @GUITest
    public void testPrivateValidationMethodsWithNullInput() throws Exception {
        assertThat(invokePrivateBooleanMethod("isBookingIdValid", null))
                .isFalse();

        assertThat(invokePrivateBooleanMethod("isLettersAndSpaces", null))
                .isFalse();

        assertThat(invokePrivateBooleanMethod("isTableNumberValid", null))
                .isFalse();

        assertThat(invokePrivateBooleanMethod("isDateValid", null))
                .isFalse();

        assertThat(invokePrivateBooleanMethod("isTimeValid", null))
                .isFalse();
    }

    @Test
    @GUITest
    public void testUpdateBookingWhenBookingIdNotFoundDoesNotChangeList() {
        GuiActionRunner.execute(() ->
                bookingSwingView.getListBookingModel()
                        .addElement(BOOKING)
        );

        Booking notPresent =
                new Booking(
                        "TBL-999",
                        "Luca Ferrari",
                        "T03",
                        "2026-07-20",
                        "21:00",
                        "Some request"
                );

        GuiActionRunner.execute(() ->
                bookingSwingView.updateBooking(notPresent)
        );

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        assertThat(window.list("bookingList").contents())
                                .containsExactly(BOOKING.toString())
                );
    }

    @Test
    @GUITest
    public void testDeleteBookingClickedWithNoSelectionDoesNotCallController() {
        GuiActionRunner.execute(() ->
                bookingSwingView.getListBookingModel()
                        .addElement(BOOKING)
        );

        window.list("bookingList").clearSelection();

        GuiActionRunner.execute(() ->
                window.button("btnDeleteBooking").target().setEnabled(true)
        );

        clickDeleteBooking();

        Awaitility.await().atMost(2, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(bookingController, never())
                                .deleteBooking(any(Booking.class))
                );
    }

    @Test
    @GUITest
    public void testWhenUpdatingAndCustomerNameIsOnlySpacesErrorMessageIsShown() {
        GuiActionRunner.execute(() ->
                bookingSwingView.getListBookingModel()
                        .addElement(BOOKING)
        );

        window.list("bookingList").selectItem(0);

        window.textBox("customernameTextBox").setText("   ");

        clickUpdateBooking();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        window.label("errorMessageLabel")
                                .requireText(
                                        "Customer Name must contain only letters and spaces"
                                )
                );

        verify(bookingController, never())
                .updateBooking(any(Booking.class));
    }
}
