package com.apiautomation.tests;

import com.apiautomation.base.BaseTest;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserTests extends BaseTest {

    @Test
    public void getUserById() {
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
    public void getUserNotFound() {
        given()
        .when()
                .get("/users/999")
        .then()
                .statusCode(404);
    }

    @Test
    public void getUsersList() {
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
    public void createUser() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Alex");
        requestBody.put("job", "QA Engineer");

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
    public void updateUser() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Alex Updated");
        requestBody.put("job", "Senior QA Engineer");

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
    public void deleteUser() {
        given()
        .when()
                .delete("/users/3")
        .then()
                .statusCode(204);
    }
}