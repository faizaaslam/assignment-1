package com.mercans.tests.ui;

import com.mercans.config.Config;
import com.mercans.pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Login Tests")
public class LoginTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);

    @Test
    @DisplayName("TC-001: Verify manager is able to login successfully")
    public void testSuccessfulManagerLogin() {
        driver.get(Config.getBaseUrl());
        LoginPage loginPage = new LoginPage(driver);

        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");

        loginPage.loginAndHandleWelcome(Config.getManagerUsername(), Config.getManagerPassword());

        assertNotEquals(Config.getBaseUrl(), driver.getCurrentUrl(), 
            "Should navigate away from login page");
        logger.info("Manager login successful");
    }

    @Test
    @DisplayName("TC-002: Verify employee is able to login successfully")
    public void testSuccessfulEmployeeLogin() {
        driver.get(Config.getBaseUrl());
        LoginPage loginPage = new LoginPage(driver);

        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");

        loginPage.loginAndHandleWelcome(Config.getEmployeeUsername(), Config.getEmployeePassword());

        assertNotEquals(Config.getBaseUrl(), driver.getCurrentUrl(), 
            "Should navigate away from login page");
        logger.info("Employee login successful");
    }

    @Test
    @DisplayName("TC-003: Verify user is unable to login with invalid username")
    public void testLoginWithInvalidUsername() {
        driver.get(Config.getBaseUrl());
        LoginPage loginPage = new LoginPage(driver);

        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        
        loginPage.login(Config.getInvalidUsername(), Config.getInvalidPassword());

        String errorMessage = loginPage.getErrorMessage();
        assertNotNull(errorMessage, "Error message should be displayed");
        assertTrue(errorMessage.toLowerCase().contains("invalid") || errorMessage.toLowerCase().contains("username") || errorMessage.toLowerCase().contains("password"), "Error message should indicate invalid credentials");
        logger.info("Error message displayed: {}", errorMessage);
    }

    @Test
    @DisplayName("TC-004: Verify user is unable to login with invalid password")
    public void testLoginWithInvalidPassword() {
        driver.get(Config.getBaseUrl());
        LoginPage loginPage = new LoginPage(driver);

        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        
        loginPage.login(Config.getEmployeeUsername(), Config.getInvalidPassword());

        String errorMessage = loginPage.getErrorMessage();
        assertNotNull(errorMessage, "Error message should be displayed");
        assertTrue(errorMessage.toLowerCase().contains("invalid") || errorMessage.toLowerCase().contains("username") || errorMessage.toLowerCase().contains("password"), "Error message should indicate invalid credentials");
        logger.info("Error message displayed: {}", errorMessage);
    }
}
