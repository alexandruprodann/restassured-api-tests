package com.apiautomation.base;

import com.apiautomation.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.getOrDefault("base.uri", "https://reqres.in");
        RestAssured.basePath = ConfigReader.getOrDefault("base.path", "/api");
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("x-api-key", ConfigReader.getRequired("api.key"))
                .addHeader("X-Reqres-Env", ConfigReader.getOrDefault("reqres.env", "prod"))
                .addHeader("User-Agent", ConfigReader.getOrDefault(
                        "user.agent",
                        "restassured-api-tests/1.0"))
                .build();
    }
}
