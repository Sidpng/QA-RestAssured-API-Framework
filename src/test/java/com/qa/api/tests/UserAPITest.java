package com.qa.api.tests;

import com.qa.api.base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

/**
 * Test suite for ReqRes User API endpoints
 * Tests basic CRUD operations on user management endpoints
 */
public class UserAPITest extends BaseTest {

    private static final String USERS_ENDPOINT = "/users";
    private int createdUserId;

    @Test(priority = 1, description = "Verify GET all users returns 200 and valid user list")
    public void testGetAllUsers() {
        Response response = getRequest(USERS_ENDPOINT);

        response.then()
                .statusCode(200)
                .body("page", equalTo(1))
                .body("data", notNullValue())
                .body("data.size()", greaterThan(0))
                .body("data[0].id", notNullValue())
                .body("data[0].email", notNullValue());
    }

    @Test(priority = 2, description = "Verify GET single user by ID returns correct user data")
    public void testGetSingleUser() {
        int userId = 2;

        Response response = getRequest(USERS_ENDPOINT + "/" + userId);

        response.then()
                .statusCode(200)
                .body("data.id", equalTo(userId))
                .body("data.email", containsString("@"))
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue());
    }

    @Test(priority = 3, description = "Verify POST to create user returns 201 with created data")
    public void testCreateUser() {
        String requestBody = "{\n" +
                "  \"name\": \"Siddhartha\",\n" +
                "  \"job\": \"QA Engineer\"\n" +
                "}";

        Response response = postRequest(USERS_ENDPOINT, requestBody);

        response.then()
                .statusCode(201)
                .body("name", equalTo("Siddhartha"))
                .body("job", equalTo("QA Engineer"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());

        createdUserId = response.jsonPath().getInt("id");
    }

    @Test(priority = 4, description = "Verify PUT to update user returns 200 with updated data")
    public void testUpdateUser() {
        String requestBody = "{\n" +
                "  \"name\": \"Siddhartha Upadhyay\",\n" +
                "  \"job\": \"Senior QA Engineer\"\n" +
                "}";

        Response response = putRequest(USERS_ENDPOINT + "/2", requestBody);

        response.then()
                .statusCode(200)
                .body("name", equalTo("Siddhartha Upadhyay"))
                .body("job", equalTo("Senior QA Engineer"))
                .body("updatedAt", notNullValue());
    }

    @Test(priority = 5, description = "Verify DELETE user returns 204 No Content")
    public void testDeleteUser() {
        Response response = deleteRequest(USERS_ENDPOINT + "/2");

        response.then()
                .statusCode(204);
    }

    @Test(priority = 6, description = "Verify GET non-existent user returns 404")
    public void testGetNonExistentUser() {
        Response response = getRequest(USERS_ENDPOINT + "/9999");

        response.then()
                .statusCode(404)
                .body("isEmpty()", is(true));
    }
}
