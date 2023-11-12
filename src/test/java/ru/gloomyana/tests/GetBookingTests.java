package ru.gloomyana.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.gloomyana.models.BookingResponseModel;


import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.gloomyana.specs.RestfulBookerSpec.*;

@Epic("API tests for restful-booker")
@Feature("Get booking")
@Tag("api")
@Owner("gloomyana")
public class GetBookingTests {

    @Test
    @DisplayName("Get all booking ids returns status 200")
    public void getAllBookingIdsReturns200() {
        step("Make get all booking request and verify it returns status code 200", () ->
                given(baseRequestSpec)
                        .when()
                        .get("https://restful-booker.herokuapp.com/booking")
                        .then()
                        .assertThat().statusCode(200));
    }

    @Test
    @DisplayName("Get booking request returns not null data")
    public void getBookingReturnsNotNullData() {
        // Get the booking ID of an existing booking
        int bookingId = 8;

        // Make a GET request to the booking endpoint with the booking ID
        BookingResponseModel response = step("Make get data request by id", () ->
                given(baseRequestSpec)
                        .contentType(JSON)
                        .when()
                        .get("https://restful-booker.herokuapp.com/booking/" + bookingId)
                        .then()
                        .statusCode(200)
                        .spec(bookingResponseSpec)
                        .extract().as(BookingResponseModel.class));

        // Verify that the response is not null
        step("Verify successful get data request", () ->
                assertThat(response).isNotNull());
    }

    @Test
    @DisplayName("Get booking request with invalid booking id returns 404")
    public void getBookingWithInvalidBookingIdReturns404() {
        int id = 123456789;

        step("Make get booking request with invalid booking id and verify it returns status code 404", () ->
                given(baseRequestSpec)
                        .when()
                        .get("https://restful-booker.herokuapp.com/ping" + id)
                        .then()
                        .assertThat().statusCode(404));
    }

    @Test
    @DisplayName("Get booking request with filter parameter by firstname returns correct results")
    public void getBookingWithFilterParameterByFirstnameReturnsCorrectResults() {
        String firstname = "Sally";

        step("Make get booking request with filter parameter by firstname", () ->
                given(baseRequestSpec)
                        .when()
                        .queryParam("firstname", firstname)
                        .get("https://restful-booker.herokuapp.com/booking")
                        .then()
                        .statusCode(200)
                        .spec(bookingResponseSpec)
                        .extract().jsonPath().getList(".", BookingResponseModel.class));
    }

    @Test
    @DisplayName("Get booking request with filter parameter by lastname returns correct results")
    public void getBookingWithFilterParameterByLastnameReturnsCorrectResults() {
        String lastname = "Brown";

        step("Make get booking request with filter parameter by lastname", () ->
                given(baseRequestSpec)
                        .when()
                        .queryParam("lastname", lastname)
                        .get("https://restful-booker.herokuapp.com/booking")
                        .then()
                        .statusCode(200)
                        .spec(bookingResponseSpec)
                        .extract().jsonPath().getList(".", BookingResponseModel.class));
    }

    @Test
    @DisplayName("Get booking request with filter parameter by checkout returns correct results")
    public void getBookingWithFilterParameterByCheckoutReturnsCorrectResults() {
        String checkout = "2023-11-12";

        step("Make get booking request with filter parameter by checkout", () ->
                given(baseRequestSpec)
                        .when()
                        .queryParam("checkout", checkout)
                        .get("https://restful-booker.herokuapp.com/booking")
                        .then()
                        .statusCode(200)
                        .spec(bookingResponseSpec)
                        .extract().jsonPath().getList(".", BookingResponseModel.class));
    }

    @Test
    @DisplayName("Get booking request with pagination parameter limit returns correct results")
    public void getBookingWithPaginationParameterLimitReturnsCorrectResults() {
        int limit = 10;

        step("Make get booking request with pagination parameter limit", () ->
                given(baseRequestSpec)
                        .when()
                        .queryParam("limit", limit)
                        .get("https://restful-booker.herokuapp.com/booking")
                        .then()
                        .statusCode(200)
                        .spec(bookingResponseSpec)
                        .extract().jsonPath().getList(".", BookingResponseModel.class));
    }

    @Test
    @DisplayName("Get booking request with pagination parameter offset returns correct results")
    public void getBookingWithPaginationParameterOffsetReturnsCorrectResults() {
        int offset = 10;

        step("Make get booking request with pagination parameter offset", () ->
                given(baseRequestSpec)
                        .when()
                        .queryParam("offset", offset)
                        .get("https://restful-booker.herokuapp.com/booking")
                        .then()
                        .statusCode(200)
                        .spec(bookingResponseSpec)
                        .extract().jsonPath().getList(".", BookingResponseModel.class));
    }

    @Test
    @DisplayName("Get booking request for active bookings returns correct results")
    public void getBookingForActiveBookingsReturnsCorrectResults() {
        step("Make get booking request for active bookings", () ->
                given(baseRequestSpec)
                        .when()
                        .get("https://restful-booker.herokuapp.com/booking?is-active=true")
                        .then()
                        .statusCode(200)
                        .spec(bookingResponseSpec)
                        .extract().jsonPath().getList(".", BookingResponseModel.class));
    }

    @Test
    @DisplayName("Get booking request for deleted bookings returns correct results")
    public void getBookingForDeletedBookingsReturnsCorrectResults() {
        step("Make get booking request for deleted bookings", () ->
                given(baseRequestSpec)
                        .when()
                        .get("https://restful-booker.herokuapp.com/booking?is-active=false")
                        .then()
                        .statusCode(200)
                        .spec(bookingResponseSpec)
                        .extract().jsonPath().getList(".", BookingResponseModel.class));
    }

    @Test
    @DisplayName("Get booking request with filter parameter by checkin returns correct results")
    public void getBookingWithFilterParameterByCheckinReturnsCorrectResults() {
        String checkin = "2023-11-11";

        step("Make get booking request with filter parameter by checkin", () ->
                given(baseRequestSpec)
                        .when()
                        .queryParam("checkin", checkin)
                        .get("https://restful-booker.herokuapp.com/ping")
                        .then()
                        .statusCode(200)
                        .spec(bookingResponseSpec)
                        .extract().jsonPath().getList(".", BookingResponseModel.class));
    }

    @Test
    @DisplayName("Get booking request for booking that has been deleted returns 404")
    public void getBookingForBookingThatHasBeenDeletedReturns404() {
        int id = 8;

        given(baseRequestSpec)
                .contentType(JSON)
                .when()
                .delete("https://restful-booker.herokuapp.com/ping" + id)
                .then()
                .statusCode(204);

        step("Make get booking request for booking that has been deleted and verify it returns status code 404", () ->
                given(baseRequestSpec)
                        .when()
                        .get("https://restful-booker.herokuapp.com/ping" + id)
                        .then()
                        .assertThat().statusCode(404));
    }
}
