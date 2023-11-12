package ru.gloomyana.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.gloomyana.config.AuthConfig;
import ru.gloomyana.models.AuthRequestModel;
import ru.gloomyana.models.AuthResponseModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Fail.fail;
import static ru.gloomyana.helpers.ApiHelpers.createToken;
import static ru.gloomyana.specs.RestfulBookerSpec.baseRequestSpec;

@Epic("API tests for restful-booker")
@Feature("Auth token")
@Tag("api")
@Owner("gloomyana")
public class AuthTest {

    @Test
    @DisplayName("Successful create a new auth token")
    public void createAuthToken() {
        AuthConfig config = ConfigFactory.create(AuthConfig.class, System.getProperties());
        AuthRequestModel authRequestModel = new AuthRequestModel(config.username(), config.password());

        AuthResponseModel response = step("Make token request with user data", () ->
                createToken(authRequestModel));
        step("Verify successful create token", () ->
                assertThat(response.getToken()).isNotNull());
    }

    @Test
    @DisplayName("Successful concurrent creation of auth tokens")
    public void concurrentTokenCreation() throws InterruptedException {
        AuthConfig config = ConfigFactory.create(AuthConfig.class, System.getProperties());
        AuthRequestModel authRequestModel = new AuthRequestModel(config.username(), config.password());

        // Create two threads to concurrently create auth tokens
        Thread thread1 = new Thread(() -> {
            AuthResponseModel response = createToken(authRequestModel);
            assertThat(response.getToken()).isNotNull();
        });

        Thread thread2 = new Thread(() -> {
            AuthResponseModel response = createToken(authRequestModel);
            assertThat(response.getToken()).isNotNull();
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    @Test
    @DisplayName("Unsuccessful create a new auth token with invalid username")
    public void createAuthTokenWithInvalidUsername() {
        AuthConfig config = ConfigFactory.create(AuthConfig.class, System.getProperties());
        AuthRequestModel authRequestModel = new AuthRequestModel("invalid username", config.password());

        AuthResponseModel response = step("Make token request with invalid username", () ->
                createToken(authRequestModel));
        step("Verify unsuccessful create token", () ->
                assertThat(response.getToken()).isNull());
    }

    @Test
    @DisplayName("Unsuccessful create a new auth token with invalid password")
    public void createAuthTokenWithInvalidPassword() {
        AuthConfig config = ConfigFactory.create(AuthConfig.class, System.getProperties());
        AuthRequestModel authRequestModel = new AuthRequestModel(config.username(), "invalid password");

        AuthResponseModel response = step("Make token request with invalid password", () ->
                createToken(authRequestModel));
        step("Verify unsuccessful create token", () ->
                assertThat(response.getToken()).isNull());
    }

    @Test
    @DisplayName("Unsuccessful create a new auth token with empty password")
    public void createAuthTokenWithEmptyPassword() {
        AuthConfig config = ConfigFactory.create(AuthConfig.class, System.getProperties());
        AuthRequestModel authRequestModel = new AuthRequestModel(config.username(), "");

        AuthResponseModel response = step("Make token request with empty password", () ->
                createToken(authRequestModel));
        step("Verify unsuccessful create token", () ->
                assertThat(response.getToken()).isNull());
    }

    @Test
    @DisplayName("Unsuccessful make request with expired token")
    public void makeRequestWithExpiredToken() throws InterruptedException {
        AuthConfig config = ConfigFactory.create(AuthConfig.class, System.getProperties());
        AuthRequestModel authRequestModel = new AuthRequestModel(config.username(), config.password());

        AuthResponseModel response = step("Make token request", () ->
                createToken(authRequestModel));

        // Wait for the token to expire
        Thread.sleep(11000);

        // Make a request with the expired token
        AuthResponseModel expiredResponse = step("Make request with expired token", () ->
                createToken(authRequestModel));

        // Verify that the request was unsuccessful
        step("Verify unsuccessful request", () ->
                assertThat(expiredResponse.getToken()).isNull());
    }

    @Test
    @DisplayName("Unsuccessful make request with reused token")
    public void makeRequestWithReusedToken() {
        AuthConfig config = ConfigFactory.create(AuthConfig.class, System.getProperties());
        AuthRequestModel authRequestModel = new AuthRequestModel(config.username(), config.password());

        // Create an auth token
        AuthResponseModel response = createToken(authRequestModel);

        // Make a request with the auth token
        AuthResponseModel usedResponse = createToken(authRequestModel);

        // Verify that the request was successful
        assertThat(usedResponse.getToken()).isNotNull();

        // Make another request with the same auth token
        AuthResponseModel reusedResponse = createToken(authRequestModel);

        // Verify that the request was unsuccessful
        assertThat(reusedResponse.getToken()).isNull();
    }

}