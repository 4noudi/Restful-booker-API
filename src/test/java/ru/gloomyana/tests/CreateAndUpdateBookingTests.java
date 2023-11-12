package ru.gloomyana.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.gloomyana.models.BookingRequestModel;
import ru.gloomyana.models.BookingResponseModel;
import ru.gloomyana.models.CreateBookingResponseModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.gloomyana.helpers.ApiHelpers.createBooking;
import static ru.gloomyana.helpers.ApiHelpers.updateBooking;
import static ru.gloomyana.specs.RestfulBookerSpec.baseRequestSpec;

@Epic("API tests for restful-booker")
@Feature("Create and update booking")
@Tag("api")
@Owner("gloomyana")
public class CreateAndUpdateBookingTests extends TestBase {

    @Test
    @DisplayName("Successful create a new booking")
    public void successfulCreateNewBooking() {
        BookingRequestModel bookingRequestModel = testData.createBookingRequestModel();

        CreateBookingResponseModel response = step("Make create booking request", () ->
                createBooking(bookingRequestModel, token));
        step("Verify successful create new booking id", () ->
                assertThat(response.getBookingId()).isNotNull());
        step("Verify successful create new booking with request data", () ->
                assertThat(response.getBookingRequestModel().equals(bookingRequestModel)));
    }

    @Test
    @DisplayName("Successful update booking data by id")
    public void successfulUpdateBooking() {
        BookingRequestModel bookingRequestModel = testData.createBookingRequestModel();
        int id = createBooking(bookingRequestModel, token).getBookingId();
        BookingRequestModel newBookingRequestModel = testData.createBookingRequestModel();

        BookingResponseModel response = step("Make update all booking data request", () ->
                updateBooking(newBookingRequestModel, token, id));
        step("Verify successful update firstname", () ->
                assertThat(response.getFirstname()).isEqualTo(newBookingRequestModel.getFirstname()));
        step("Verify successful update lastname", () ->
                assertThat(response.getLastname()).isEqualTo(newBookingRequestModel.getLastname()));
        step("Verify successful update total price", () ->
                assertThat(response.getTotalPrice()).isEqualTo(newBookingRequestModel.getTotalPrice()));
    }

    @Test
    @DisplayName("Unsuccessful update booking with invalid booking id")
    public void unsuccessfulUpdateBookingWithInvalidBookingId() {
        BookingRequestModel bookingRequestModel = testData.createBookingRequestModel();

        int id = 123456789;
        step("Try to update booking with invalid booking id", () ->
                updateBooking(bookingRequestModel, token, id));
    }

    @Test
    @DisplayName("Unsuccessful create booking with empty request data")
    public void unsuccessfulCreateBookingWithEmptyRequestData() {
        BookingRequestModel bookingRequestModel = testData.createBookingRequestModel();

        step("Make create booking request with empty request data", () ->
                given(baseRequestSpec)
                        .body(bookingRequestModel)
                        .contentType(JSON)
                        .when()
                        .post("https://restful-booker.herokuapp.com/booking")
                        .then()
                        .assertThat().statusCode(400));
    }

    @Test
    @DisplayName("Unsuccessful create booking with invalid request data")
    public void unsuccessfulCreateBookingWithInvalidRequestData() {
        BookingRequestModel bookingRequestModel = testData.createBookingRequestModel();
        bookingRequestModel.setFirstname("");
        bookingRequestModel.setLastname("");
        bookingRequestModel.setTotalPrice(-1);

        step("Make create booking request with invalid request data", () ->
                given(baseRequestSpec)
                        .body(bookingRequestModel)
                        .contentType(JSON)
                        .when()
                        .post("https://restful-booker.herokuapp.com/booking")
                        .then()
                        .assertThat().statusCode(400));
    }

    @Test
    @DisplayName("Unsuccessful update booking with empty request data")
    public void unsuccessfulUpdateBookingWithEmptyRequestData() {
        BookingRequestModel bookingRequestModel = testData.createBookingRequestModel();

        step("Make update booking request with empty request data", () ->
                given(baseRequestSpec)
                        .body(bookingRequestModel)
                        .contentType(JSON)
                        .when()
                        .put("https://restful-booker.herokuapp.com/booking/1")
                        .then()
                        .assertThat().statusCode(400));
    }

    @Test
    @DisplayName("Unsuccessful update booking with invalid request data")
    public void unsuccessfulUpdateBookingWithInvalidRequestData() {
        BookingRequestModel bookingRequestModel = testData.createBookingRequestModel();
        bookingRequestModel.setFirstname("");
        bookingRequestModel.setLastname("");
        bookingRequestModel.setTotalPrice(-1);

        step("Make update booking request with invalid request data", () ->
                given(baseRequestSpec)
                        .body(bookingRequestModel)
                        .contentType(JSON)
                        .when()
                        .put("https://restful-booker.herokuapp.com/booking/1")
                        .then()
                        .assertThat().statusCode(400));
    }
}
