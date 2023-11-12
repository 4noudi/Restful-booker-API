package ru.gloomyana.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.gloomyana.models.BookingRequestModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.gloomyana.helpers.ApiHelpers.createBooking;
import static ru.gloomyana.specs.RestfulBookerSpec.baseRequestSpec;

@Epic("API tests for restful-booker")
@Feature("Delete booking")
@Tag("api")
@Owner("gloomyana")
public class DeleteBookingTests extends TestBase {

    @Test
    @DisplayName("Delete request returns status 201")
    public void deleteBookingReturns201() {
        BookingRequestModel bookingRequestModel = testData.createBookingRequestModel();
        int id = createBooking(bookingRequestModel, token).getBookingId();

        step("Make booking delete request and verify it returns status code 201", () ->
                given(baseRequestSpec)
                        .header("Cookie", "token=" + token)
                        .when()
                        .delete("https://restful-booker.herokuapp.com/booking/" + id)
                        .then()
                        .assertThat().statusCode(201));
    }

    @Test
    @DisplayName("Delete request without auth token returns 403")
    public void deleteBookingWithoutAuthTokenReturns403() {
        BookingRequestModel bookingRequestModel = testData.createBookingRequestModel();
        int id = createBooking(bookingRequestModel, token).getBookingId();

        step("Make booking delete request without auth token and verify it returns status code 403", () ->
                given(baseRequestSpec)
                        .when()
                        .delete("https://restful-booker.herokuapp.com/booking/" + id)
                        .then()
                        .assertThat().statusCode(403));
    }

    @Test
    @DisplayName("Delete request for invalid booking id returns 404")
    public void deleteBookingWithInvalidBookingIdReturns404() {
        int id = 123456789;

        step("Make booking delete request for invalid booking id and verify it returns status code 404", () ->
                given(baseRequestSpec)
                        .header("Cookie", "token=" + token)
                        .when()
                        .delete("https://restful-booker.herokuapp.com/ping" + id)
                        .then()
                        .assertThat().statusCode(404));
    }

    @Test
    @DisplayName("Delete request for booking that has already been deleted returns 404")
    public void deleteBookingThatHasAlreadyBeenDeletedReturns404() {
        BookingRequestModel bookingRequestModel = testData.createBookingRequestModel();
        int id = createBooking(bookingRequestModel, token).getBookingId();

        given(baseRequestSpec)
                .header("Cookie", "token=" + token)
                .when()
                .delete("https://restful-booker.herokuapp.com/ping" + id)
                .then()
                .assertThat().statusCode(201);

        step("Make booking delete request for booking that has already been deleted and verify it returns status code 404", () ->
                given(baseRequestSpec)
                        .header("Cookie", "token=" + token)
                        .when()
                        .delete("https://restful-booker.herokuapp.com/ping" + id)
                        .then()
                        .assertThat().statusCode(404));
    }
}
