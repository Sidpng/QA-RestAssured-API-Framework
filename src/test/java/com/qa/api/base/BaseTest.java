package com.qa.api.base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

/**
 * Base Test class for API automation
 * Provides common setup, configuration, and reusable request/response methods
 */
public class BaseTest {

    protected RequestSpecification requestSpec;
    protected String baseURI = "https://reqres.in";
    protected String basePath = "/api";

    @BeforeClass
    public void setup() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(baseURI)
               .setBasePath(basePath)
               .setContentType(ContentType.JSON)
               .addHeader("Accept", "application/json");

        requestSpec = builder.build();
    }

    /**
     * Helper method to perform GET request
     * @param endpoint API endpoint
     * @return Response object
     */
    protected Response getRequest(String endpoint) {
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(endpoint);
    }

    /**
     * Helper method to perform POST request
     * @param endpoint API endpoint
     * @param body Request payload
     * @return Response object
     */
    protected Response postRequest(String endpoint, Object body) {
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post(endpoint);
    }

    /**
     * Helper method to perform PUT request
     * @param endpoint API endpoint
     * @param body Request payload
     * @return Response object
     */
    protected Response putRequest(String endpoint, Object body) {
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .put(endpoint);
    }

    /**
     * Helper method to perform DELETE request
     * @param endpoint API endpoint
     * @return Response object
     */
    protected Response deleteRequest(String endpoint) {
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .delete(endpoint);
    }
}
