package ru.gloomyana.helpers;

import ru.gloomyana.models.*;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static ru.gloomyana.specs.RestfulBookerSpec.*;

public class ApiHelpers {
    public static AuthResponseModel createToken(AuthRequestModel authRequestModel) {
        return given(baseRequestSpec)
                .contentType(JSON)
                .body(authRequestModel)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .spec(authResponseSpec)
                .extract().as(AuthResponseModel.class);
    }

    public static CreateBookingResponseModel createBooking(BookingRequestModel bookingRequestModel, String token) {
        return given(baseRequestSpec)
                .header("Cookie", "token=" + token)
                .contentType(JSON)
                .body(bookingRequestModel)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .spec(createBookingResponseSpec)
                .extract().as(CreateBookingResponseModel.class);
    }

    public static BookingResponseModel updateBooking(BookingRequestModel bookingRequestModel, String token, int id) {
        return given(baseRequestSpec)
                .header("Cookie", "token=" + token)
                .contentType(JSON)
                .body(bookingRequestModel)
                .when()
                .put("/booking/" + id)
                .then()
                .statusCode(200)
                .spec(bookingResponseSpec)
                .extract().as(BookingResponseModel.class);
    }
}
