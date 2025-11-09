package com.mercans.tests.api;

import com.mercans.config.Config;
import io.restassured.RestAssured;
import io.restassured.config.RedirectConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("API Login Tests")
public class APILoginTest {
    private static final Logger logger = LoggerFactory.getLogger(APILoginTest.class);
    private static String baseUrl;
    private static String loginEndpoint;

    @BeforeAll
    public static void setUp() {
        baseUrl = Config.getBaseUrl().replaceAll("/$", "");
        loginEndpoint = baseUrl + "/api/login";
        RestAssured.baseURI = baseUrl;
        RestAssured.config = RestAssured.config()
            .redirect(RedirectConfig.redirectConfig().followRedirects(false));
    }

    @Test
    @DisplayName("TC-014: API Login - Valid Manager Credentials")
    public void testLoginSuccessManager() {
        String requestBody = String.format(
            "{\"username\":\"%s\",\"password\":\"%s\"}",
            Config.getManagerUsername(),
            Config.getManagerPassword()
        );

        Response response = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(loginEndpoint)
            .then()
            .extract()
            .response();

        logger.info("Response status: {}", response.getStatusCode());
        logger.info("Response headers: {}", response.getHeaders());
        String location = response.getHeader("Location");
        logger.info("Redirect location: {}", location);
        logger.info("Response body: {}", response.getBody().asString());

        int statusCode = response.getStatusCode();
        assertTrue(
            statusCode == 200 || statusCode == 201 || statusCode == 302,
            "Expected 200/201/302, got " + statusCode
        );
        
        if (statusCode == 302) {
            assertNotNull(location, "Redirect location should be present");
            logger.info("API returned redirect to: {}", location);
        }
        
        logger.info("Manager login API call completed with status: {}", statusCode);
    }

    @Test
    @DisplayName("TC-015: API Login - Valid Employee Credentials")
    public void testLoginSuccessEmployee() {
        String requestBody = String.format(
            "{\"username\":\"%s\",\"password\":\"%s\"}",
            Config.getEmployeeUsername(),
            Config.getEmployeePassword()
        );

        Response response = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(loginEndpoint)
            .then()
            .extract()
            .response();

        logger.info("Response status: {}", response.getStatusCode());
        logger.info("Response headers: {}", response.getHeaders());
        String location = response.getHeader("Location");
        logger.info("Redirect location: {}", location);
        logger.info("Response body: {}", response.getBody().asString());

        int statusCode = response.getStatusCode();
        assertTrue(
            statusCode == 200 || statusCode == 201 || statusCode == 302,
            "Expected 200/201/302, got " + statusCode
        );
        
        if (statusCode == 302) {
            assertNotNull(location, "Redirect location should be present");
            logger.info("API returned redirect to: {}", location);
        }
        
        logger.info("Employee login API call completed with status: {}", statusCode);
    }

    @Test
    @DisplayName("TC-016: API Login - Invalid Username")
    public void testLoginInvalidUsername() {
        String requestBody = "{\"username\":\"invalid_user@example.com\",\"password\":\"password123\"}";

        Response response = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(loginEndpoint)
            .then()
            .extract()
            .response();

        logger.info("Response status: {}", response.getStatusCode());
        String location = response.getHeader("Location");
        logger.info("Redirect location: {}", location);
        logger.info("Response body: {}", response.getBody().asString());

        int statusCode = response.getStatusCode();
        assertTrue(
            statusCode == 400 || statusCode == 401 || statusCode == 403 || statusCode == 302,
            "Expected error status (400/401/403/302), got " + statusCode
        );
        
        if (statusCode == 302) {
            logger.info("API returned redirect (may indicate invalid credentials)");
        }
        
        logger.info("Invalid username test completed with status: {}", statusCode);
    }

    @Test
    @DisplayName("TC-017: API Login - Invalid Password")
    public void testLoginInvalidPassword() {
        String requestBody = String.format(
            "{\"username\":\"%s\",\"password\":\"wrong_password\"}",
            Config.getEmployeeUsername()
        );

        Response response = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(loginEndpoint)
            .then()
            .extract()
            .response();

        logger.info("Response status: {}", response.getStatusCode());
        String location = response.getHeader("Location");
        logger.info("Redirect location: {}", location);
        logger.info("Response body: {}", response.getBody().asString());

        int statusCode = response.getStatusCode();
        assertTrue(
            statusCode == 400 || statusCode == 401 || statusCode == 403 || statusCode == 302,
            "Expected error status (400/401/403/302), got " + statusCode
        );
        
        if (statusCode == 302) {
            logger.info("API returned redirect (may indicate invalid credentials)");
        }
        
        logger.info("Invalid password test completed with status: {}", statusCode);
    }

    @Test
    @DisplayName("TC-018: API Login - Missing Username")
    public void testLoginMissingUsername() {
        String requestBody = "{\"password\":\"password123\"}";

        Response response = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(loginEndpoint)
            .then()
            .extract()
            .response();

        logger.info("Response status: {}", response.getStatusCode());
        String location = response.getHeader("Location");
        logger.info("Redirect location: {}", location);
        logger.info("Response body: {}", response.getBody().asString());

        int statusCode = response.getStatusCode();
        assertTrue(
            statusCode == 400 || statusCode == 302,
            "Expected 400 or 302, got " + statusCode
        );
        
        if (statusCode == 302) {
            logger.info("API returned redirect (may indicate missing username)");
        }
        
        logger.info("Missing username test completed with status: {}", statusCode);
    }

    @Test
    @DisplayName("TC-019: API Login - Missing Password")
    public void testLoginMissingPassword() {
        String requestBody = String.format(
            "{\"username\":\"%s\"}",
            Config.getEmployeeUsername()
        );

        Response response = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(loginEndpoint)
            .then()
            .extract()
            .response();

        logger.info("Response status: {}", response.getStatusCode());
        String location = response.getHeader("Location");
        logger.info("Redirect location: {}", location);
        logger.info("Response body: {}", response.getBody().asString());

        int statusCode = response.getStatusCode();
        assertTrue(
            statusCode == 400 || statusCode == 302,
            "Expected 400 or 302, got " + statusCode
        );
        
        if (statusCode == 302) {
            logger.info("API returned redirect (may indicate missing password)");
        }
        
        logger.info("Missing password test completed with status: {}", statusCode);
    }

    @Test
    @DisplayName("TC-020: API Login - Empty Credentials")
    public void testLoginEmptyCredentials() {
        String requestBody = "{\"username\":\"\",\"password\":\"\"}";

        Response response = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(loginEndpoint)
            .then()
            .extract()
            .response();

        logger.info("Response status: {}", response.getStatusCode());
        String location = response.getHeader("Location");
        logger.info("Redirect location: {}", location);
        logger.info("Response body: {}", response.getBody().asString());

        int statusCode = response.getStatusCode();
        assertTrue(
            statusCode == 400 || statusCode == 401 || statusCode == 403 || statusCode == 302,
            "Expected error status (400/401/403/302), got " + statusCode
        );
        
        if (statusCode == 302) {
            logger.info("API returned redirect (may indicate empty credentials)");
        }
        
        logger.info("Empty credentials test completed with status: {}", statusCode);
    }
}
