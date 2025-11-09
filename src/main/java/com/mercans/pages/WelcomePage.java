package com.mercans.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WelcomePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(WelcomePage.class);

    @FindBy(css = "input[type='checkbox']")
    private List<WebElement> checkboxes;

    @FindBy(css = "button")
    private List<WebElement> buttons;

    @FindBy(css = "input[data-test*='terms'], input[data-test*='accept'], input[data-test*='agree']")
    private WebElement termsCheckbox;

    @FindBy(css = "button[data-test*='continue'], button[data-test*='next'], button[data-test*='proceed'], button[data-test*='start'], button[data-test*='welcome']")
    private WebElement continueButton;

    public WelcomePage(WebDriver driver) {
        super(driver);
    }

    public void handleWelcomeScreen() {
        try {
            Thread.sleep(2000);
            
            try {
                if (termsCheckbox != null && termsCheckbox.isDisplayed() && !termsCheckbox.isSelected()) {
                    wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox));
                    termsCheckbox.click();
                    logger.info("Clicked terms and conditions checkbox using @FindBy");
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                logger.debug("Could not use @FindBy termsCheckbox, trying fallback");
            }
            
            for (WebElement checkbox : checkboxes) {
                if (checkbox.isDisplayed() && !checkbox.isSelected()) {
                    String dataTest = checkbox.getAttribute("data-test");
                    String id = checkbox.getAttribute("id");
                    String name = checkbox.getAttribute("name");
                    
                    if ((dataTest != null && (dataTest.toLowerCase().contains("terms") || 
                         dataTest.toLowerCase().contains("accept") || 
                         dataTest.toLowerCase().contains("agree"))) ||
                        (id != null && (id.toLowerCase().contains("terms") || id.toLowerCase().contains("accept"))) ||
                        (name != null && (name.toLowerCase().contains("terms") || name.toLowerCase().contains("accept")))) {
                        checkbox.click();
                        logger.info("Clicked terms and conditions checkbox");
                        Thread.sleep(500);
                    }
                }
            }
            
            try {
                if (continueButton != null && continueButton.isDisplayed()) {
                    wait.until(ExpectedConditions.elementToBeClickable(continueButton));
                    continueButton.click();
                    logger.info("Clicked welcome screen button using @FindBy: {}", continueButton.getText());
                    Thread.sleep(3000);
                    return;
                }
            } catch (Exception e) {
                logger.debug("Could not use @FindBy continueButton, trying fallback");
            }
            
            for (WebElement button : buttons) {
                if (button.isDisplayed()) {
                    String text = button.getText().toLowerCase();
                    String dataTest = button.getAttribute("data-test");
                    
                    if ((dataTest != null && (dataTest.toLowerCase().contains("continue") || 
                         dataTest.toLowerCase().contains("next") || 
                         dataTest.toLowerCase().contains("proceed") ||
                         dataTest.toLowerCase().contains("start") ||
                         dataTest.toLowerCase().contains("welcome"))) ||
                        (text.contains("continue") || text.contains("next") || 
                         text.contains("get started") || text.contains("start") ||
                         text.contains("proceed"))) {
                        button.click();
                        logger.info("Clicked welcome screen button: {}", button.getText());
                        Thread.sleep(3000);
                        return;
                    }
                }
            }
            
            for (WebElement button : buttons) {
                if (button.isDisplayed() && button.isEnabled()) {
                    button.click();
                    logger.info("Clicked first visible button as fallback");
                    Thread.sleep(3000);
                    return;
                }
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.warn("Welcome screen handling failed: {}", e.getMessage());
        }
    }

    public boolean isWelcomeScreenPresent() {
        try {
            return buttons.stream().anyMatch(WebElement::isDisplayed) ||
                   checkboxes.stream().anyMatch(WebElement::isDisplayed);
        } catch (Exception e) {
            try {
                return driver.findElements(By.cssSelector("button")).stream()
                        .anyMatch(WebElement::isDisplayed) ||
                       driver.findElements(By.cssSelector("input[type='checkbox']")).stream()
                        .anyMatch(WebElement::isDisplayed);
            } catch (Exception ex) {
                return false;
            }
        }
    }
}
