package ru.gloomyana.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.restassured.specification.RequestSpecification;
import java.util.Objects;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static ru.gloomyana.specs.RestfulBookerSpec.baseRequestSpec;


@Epic("API tests for restful-booker")
@Feature("API health check")
@Tag("api")
@Owner("gloomyana")


public class HealthCheckTest {

    @Test
    @DisplayName("Health check endpoint to confirm the API is up")
    public void healthCheckReturns201() {
        step("Make health check request and verify it returns status 201", () ->
                given(baseRequestSpec)
                        .when()
                        .get("https://restful-booker.herokuapp.com/ping")
                        .then()
                        .assertThat().statusCode(201));
    }

    @Test
    @DisplayName("Health check endpoint should reject invalid endpoint")
    public void healthCheckRejectsInvalidEndpoint() {
        step("Make health check request to invalid endpoint", () ->
                given(baseRequestSpec)
                        .when()
                        .get("https://restful-booker.herokuapp.com/ping/invalid-endpoint")
                        .then()
                        .assertThat().statusCode(404));
    }

    @Test
    @DisplayName("Health check endpoint should return 500 error if there is an internal server error")
    public void healthCheckReturns500OnInternalServerError() {
        step("Simulate an internal server error by sending an invalid PUT request", () ->
                given(baseRequestSpec)
                        .when()
                        .put("https://restful-booker.herokuapp.com/ping")
                        .then()
                        .assertThat().statusCode(500));
    }

    @Test
    @DisplayName("Health check endpoint should reject too many requests")
    public void healthCheckRejectsTooManyRequests() {
        step("Make a large number of requests to the health check endpoint in a short period of time", () -> {
            for (int i = 0; i < 5; i++) {
                given(baseRequestSpec)
                        .when()
                        .get("https://restful-booker.herokuapp.com/ping");

                // Add a sleep between each request to prevent making too many requests in a short period of time
                Thread.sleep(1000);
            }
        });

        step("Verify that the last request returns a 429 Too Many Requests error", () -> {
            Response response = given(baseRequestSpec)
                    .when()
                    .get("https://restful-booker.herokuapp.com/ping")
                    .then()
                    .assertThat().statusCode(429).extract().response();
        });

    }

    @Test
    @DisplayName("Health check endpoint should reject missing authentication credentials")
    public void healthCheckRejectsMissingAuthenticationCredentials() {
        step("Make health check request without authentication credentials", () ->
                given()
                        .when()
                        .get("https://restful-booker.herokuapp.com/health")
                        .then()
                        .assertThat().statusCode(401));
    }

    @Test
    @DisplayName("Health check endpoint should reject invalid HTTP method")
    public void healthCheckRejectsInvalidHttpMethod() {
        step("Make health check request with invalid HTTP method", () ->
                given(baseRequestSpec)
                        .when()
                        .get("https://restful-booker.herokuapp.com/health")
                        .then()
                        .assertThat().statusCode(405));
    }
}
