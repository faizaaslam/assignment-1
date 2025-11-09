package com.mercans.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    @FindBy(css = "input[name='email']")
    private WebElement usernameInput;

    @FindBy(css = "input[name='password']")
    private WebElement passwordInput;

    @FindBy(css = "[data-test='perform-login']")
    private WebElement loginButton;

    @FindBy(css = ".login-response-error, [data-test*='error'], [data-test*='alert'], .error, .alert")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameInput));
        usernameInput.click();
        usernameInput.clear();
        usernameInput.sendKeys(username);
        logger.info("Entered username");
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordInput));
        passwordInput.click();
        passwordInput.clear();
        passwordInput.sendKeys(password);
        logger.info("Entered password");
    }

    public void clickLogin() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            loginButton.click();
            logger.info("Clicked login button");
        } catch (Exception e) {
            logger.error("Could not click login button: {}", e.getMessage());
            throw e;
        }
    }

    public void login(String username, String password) {
        logger.info("Logging in with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public void loginAndHandleWelcome(String username, String password) {
        login(username, password);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        WelcomePage welcomePage = new WelcomePage(driver);
        if (welcomePage.isWelcomeScreenPresent()) {
            welcomePage.handleWelcomeScreen();
        } else {
            logger.info("Welcome screen not present, skipping handling");
        }
    }

    public String getErrorMessage() {
        try {
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".login-response-error")));
            if (errorElement != null && errorElement.isDisplayed()) {
                String errorText = errorElement.getText();
                logger.info("Found error message: {}", errorText);
                return errorText;
            }
        } catch (Exception e) {
            logger.debug("login-response-error not found, trying fallback selectors");
        }
        
        try {
            if (errorMessage != null) {
                wait.until(ExpectedConditions.visibilityOf(errorMessage));
                return errorMessage.getText();
            }
        } catch (Exception e) {
            logger.debug("Error message not found with @FindBy: {}", e.getMessage());
        }
        
        try {
            List<WebElement> errorElements = driver.findElements(By.cssSelector(
                ".login-response-error, [data-test*='error'], [data-test*='alert'], .error, .alert"));
            for (WebElement element : errorElements) {
                if (element.isDisplayed()) {
                    String errorText = element.getText();
                    if (errorText != null && !errorText.trim().isEmpty()) {
                        logger.info("Found error message: {}", errorText);
                        return errorText;
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Error message not found: {}", e.getMessage());
        }
        
        return null;
    }

    public boolean isLoginPageLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(usernameInput)) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
