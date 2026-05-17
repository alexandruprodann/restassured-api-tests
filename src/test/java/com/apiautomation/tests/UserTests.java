package com.apiautomation.tests;

import com.apiautomation.base.BaseTest;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserTests extends BaseTest {

    @Test
    public void shouldReturnUserWhenValidIdProvided() {
        given()
        .when()
            .get("/users/2")
        .then()
            .statusCode(200)
            .body("data.id", equalTo(2))
            .body("data.first_name", equalTo("Janet"))
            .body("data.last_name", equalTo("Weaver"))
            .body("data.email", notNullValue());
    }

    @Test
    public void shouldReturn404WhenUserDoesNotExist() {
        given()
        .when()
            .get("/users/999")
        .then()
            .statusCode(404);
    }

    @Test
    public void shouldReturnPagedListOfUsers() {
        given()
            .queryParam("page", 2)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("page", equalTo(2))
            .body("data", not(empty()))
            .body("data.first_name", hasItem("Lindsay"));
    }

    @Test
    public void shouldCreateUserAndReturnCreatedStatus() {
        Map<String, Object> requestBody = Map.of(
                "name", "Alex",
                "job", "QA Engineer"
        );

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .body("name", equalTo("Alex"))
            .body("job", equalTo("QA Engineer"))
            .body("id", notNullValue())
            .body("createdAt", notNullValue());
    }

    @Test
    public void shouldUpdateUserAndReturnUpdatedFields() {
        Map<String, Object> requestBody = Map.of(
                "name", "Alex Updated",
                "job", "Senior QA Engineer"
        );

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .put("/users/3")
        .then()
            .statusCode(200)
            .body("name", equalTo("Alex Updated"))
            .body("job", equalTo("Senior QA Engineer"))
            .body("updatedAt", notNullValue());
    }

    @Test
    public void shouldDeleteUserAndReturnNoContent() {
        given()
        .when()
            .delete("/users/3")
        .then()
            .statusCode(204);
    }

    @Test
    public void shouldReturnGeneratedIdWhenUserIsCreated() {
        Map<String, Object> requestBody = Map.of(
                "name", "Alex",
                "job", "QA Engineer"
        );

        String userId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract()
            .path("id");


        System.out.println("Created user with ID: " + userId);

        assert userId != null : "User ID should not be null";
        assert !userId.isEmpty() : "User ID should not be empty";
    }

    @Test
    public void shouldFetchUserSuccessfullyWhenIdExtractedFromList() {
        int firstUserId = given()
            .when()
                .get("/users?page=1")
            .then()
                .statusCode(200)
                .extract()
                .path("data[0].id");

        given()
        .when()
            .get("/users/" + firstUserId)
        .then()
            .statusCode(200)
            .body("data.id", equalTo(firstUserId));
    }
}