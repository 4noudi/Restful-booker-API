package ru.gloomyana.tests;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.gloomyana.config.AuthConfig;
import ru.gloomyana.helpers.ApiHelpers;
import ru.gloomyana.models.AuthRequestModel;
import ru.gloomyana.models.AuthResponseModel;

import static io.restassured.RestAssured.given;
import static ru.gloomyana.specs.RestfulBookerSpec.baseRequestSpec;

public class TestBase {
    TestData testData = new TestData();
    AuthConfig config = ConfigFactory.create(AuthConfig.class, System.getProperties());
    protected String token;

    @BeforeAll
    static void checkHealthBeforeTest() {
        given(baseRequestSpec)
                .when()
                .get("/ping")
                .then()
                .assertThat().statusCode(201);
    }

    @BeforeEach
    void createAuthToken() {
        AuthRequestModel authRequestModel = new AuthRequestModel(config.username(), config.password());
        AuthResponseModel response = ApiHelpers.createToken(authRequestModel);
        token = response.getToken();
    }
}
