package com.mercans.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mercans.utils.Utilities;

import java.util.List;

public class LeavesPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LeavesPage.class);

    @FindBy(xpath = "//*[@class='lp-dashboard-card__title' and contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'leave')]")
    private WebElement leavesCardTitle;

    @FindBy(css = "[aria-label='Request new leave']")
    private WebElement requestLeaveButton;

    @FindBy(css = "span.label-02-strong, span[class*='label-02-strong']")
    private WebElement leaveBalance;

    @FindBy(css = "[data-test='submit-approve']")
    private WebElement submitApproveButton;

    @FindBy(css = "[data-test='primary-action-button']")
    private WebElement primaryActionButton;

    @FindBy(css = "[data-test='withdraw-request']")
    private WebElement withdrawRequestButton;

    @FindBy(xpath = "//div[@class='balance-card__header']//div[text()='Sick leave']")
    private List<WebElement> sickLeaveElements;

    @FindBy(xpath = "//div[contains(@class, 'balance-row') and contains(@class, 'balance-row_total')]//span[@class='label-02-strong']")
    private WebElement remainingLeaves;

    @FindBy(xpath = "//span[contains(@id, 'request-') and contains(@id, '-heading') and contains(text(), 'Annual leave')]")
    private WebElement leaveRequestHeading;

    @FindBy(xpath = "//div[@class='balance-card__header']//div[text()='Annual leave']")
    private WebElement annualLeaveHeading;

    public LeavesPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLeaves() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(leavesCardTitle));
            leavesCardTitle.click();
            logger.info("Clicked on Leaves card to navigate to Leaves module");
            waitForPageLoad();
            return;
        } catch (Exception e) {
            logger.debug("Could not use @FindBy leavesCardTitle, trying fallback");
        }
        
        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'leave')]"));
        for (WebElement elem : elements) {
            try {
                if (elem.isDisplayed() && elem.getText().toLowerCase().contains("leave")) {
                    elem.click();
                    logger.info("Clicked on element with 'leave' text");
                    waitForPageLoad();
                    return;
                }
            } catch (Exception ex) {
                continue;
            }
        }
    }

    public void clickRequestLeave() {
        wait.until(ExpectedConditions.elementToBeClickable(requestLeaveButton));
        requestLeaveButton.click();
        logger.info("Clicked 'Request new leave' button");
    }

    public void enterNote(String note) {
        try {
            By noteTextbox = By.xpath("//textarea[@name='note' or @placeholder='Write your note' or contains(@aria-label, 'note')]");
            WebElement noteElement = wait.until(ExpectedConditions.elementToBeClickable(noteTextbox));
            noteElement.click();
            noteElement.clear();
            noteElement.sendKeys(note);
            logger.info("Entered note: {}", note);
        } catch (Exception e) {
            logger.error("Could not enter note: {}", e.getMessage());
            throw e;
        }
    }

    public void clickSubmitApprove() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitApproveButton));
            submitApproveButton.click();
            logger.info("Clicked submit/approve button");
        } catch (Exception e) {
            logger.error("Could not click submit/approve button: {}", e.getMessage());
            throw e;
        }
    }

    public void clickPrimaryActionButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(primaryActionButton));
            primaryActionButton.click();
            logger.info("Clicked primary action button");
        } catch (Exception e) {
            logger.error("Could not click primary action button: {}", e.getMessage());
            throw e;
        }
    }

    public void openLeaveRequest() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(leaveRequestHeading));
            leaveRequestHeading.click();
            logger.info("Opened leave request");
        } catch (Exception e) {
            logger.error("Could not open leave request: {}", e.getMessage());
            throw e;
        }
    }

    public void withdrawLeaveRequest() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(withdrawRequestButton));
            withdrawRequestButton.click();
            logger.info("Clicked withdraw request button");
        } catch (Exception e) {
            logger.error("Could not click withdraw request button: {}", e.getMessage());
            throw e;
        }
    }

    public boolean isLeavesPageLoaded() {
        try {
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl != null && currentUrl.contains("/employee/my-leaves")) {
                By pageTitle = By.xpath("//div[@class='page-title' and text()='My leaves']");
                wait.until(ExpectedConditions.presenceOfElementLocated(pageTitle));
                logger.debug("Leaves page is loaded - URL and page title verified");
                return true;
            }
        } catch (Exception e) {
            logger.debug("Leaves page not loaded: {}", e.getMessage());
        }
        return false;
    }
    
    private void waitForPageLoad() {
        try {
            if (driver instanceof JavascriptExecutor) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
            }
        } catch (Exception e) {
            logger.debug("Page load wait completed or timed out: {}", e.getMessage());
        }
    }

    public String getLeaveBalance() {
        try {
            wait.until(ExpectedConditions.visibilityOf(leaveBalance));
            return leaveBalance.getText();
        } catch (Exception e) {
            logger.debug("Leave balance not found: {}", e.getMessage());
            return null;
        }
    }

    public String selectRandomStartDate() {
        String dateId = Utilities.getRandomWeekdayDateForCurrentMonth();
        By dateElement = By.cssSelector(String.format("[id='%s']", dateId));
        WebElement date = wait.until(ExpectedConditions.presenceOfElementLocated(dateElement));
        
        scrollIntoView(date);
        
        wait.until(ExpectedConditions.elementToBeClickable(date));
        date.click();
        logger.info("Selected random start date with id: {}", dateId);
        return dateId;
    }

    public String selectRandomEndDate(String startDateId) {
        String dateId = Utilities.getRandomWeekdayDateAfterStart(startDateId);
        By dateElement = By.cssSelector(String.format("[id='%s']", dateId));
        WebElement date = wait.until(ExpectedConditions.presenceOfElementLocated(dateElement));
        
        scrollIntoView(date);
        
        wait.until(ExpectedConditions.elementToBeClickable(date));
        date.click();
        logger.info("Selected random end date with id: {} (after start date: {})", dateId, startDateId);
        return dateId;
    }

    private void scrollIntoView(WebElement element) {
        try {
            if (driver instanceof JavascriptExecutor) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
                logger.debug("Scrolled element into view");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            logger.debug("Could not scroll element into view: {}", e.getMessage());
        }
    }

    public boolean verifyLeavesBalance(String text) {
        try {
            Thread.sleep(5000);
            wait.until(ExpectedConditions.visibilityOf(annualLeaveHeading));
            wait.until(ExpectedConditions.visibilityOf(remainingLeaves));
            logger.info("Remaining leaves text: {}", remainingLeaves.getText());
            return remainingLeaves.getText().contains(text);
        } catch (Exception e) {
            logger.error("Could not verify remaining leaves text: {}", e.getMessage());
            return false;
        }
    }

    public boolean verifyRequestStatusContainsPending() {
        try {
            By statusPattern = By.cssSelector("[id^='request-'][id$='-status']");
            List<WebElement> statusElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(statusPattern));
            
            for (WebElement status : statusElements) {
                if (status.isDisplayed()) {
                    String statusText = status.getText();
                    if (statusText != null && statusText.contains("Pending")) {
                        logger.info("Found pending status in element with id: {}", status.getAttribute("id"));
                        return true;
                    }
                }
            }
            
            return false;
        } catch (Exception e) {
            logger.error("Could not verify request status: {}", e.getMessage());
            return false;
        }
    }

    public boolean isSickLeaveSectionPresent() {
        return sickLeaveElements.stream().anyMatch(WebElement::isDisplayed);
    }
}
