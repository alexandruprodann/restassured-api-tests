package com.apiautomation.tests;

import com.apiautomation.base.BaseTest;
import com.apiautomation.utils.ConfigReader;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthTests extends BaseTest {

    @Test
    public void shouldReturnTokenWhenCredentialsAreValid() {
        Map<String, Object> requestBody = Map.of(
                "email", ConfigReader.get("login.email"),
                "password", ConfigReader.get("login.password")
        );

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .body("token", notNullValue());
    }

    @Test
    public void shouldReturn400WhenPasswordIsMissing() {
        Map<String, Object> requestBody = Map.of(
                "email", ConfigReader.get("login.email")
        );

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/login")
        .then()
            .statusCode(400)
            .body("error", equalTo("Missing password"));
    }

    @Test
    public void shouldReturn400WhenCredentialsAreInvalid() {
        Map<String, Object> requestBody = Map.of(
                "email", "invalid@user.com",
                "password", "wrongpassword"
        );

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/login")
        .then()
            .statusCode(400)
            .body("error", notNullValue());
    }
}