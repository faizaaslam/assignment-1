package com.mercans.tests.ui;

import com.mercans.config.Config;
import com.mercans.utils.DriverFactory;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {
    protected WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeEach
    public void setUp() {
        String headlessMode = System.getProperty("headless", "true");
        boolean isHeadless = Boolean.parseBoolean(headlessMode);
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME, isHeadless);
        if (!isHeadless) {
            driver.manage().window().maximize();
        }
        driver.manage().timeouts().implicitlyWait(
            java.time.Duration.ofSeconds(Config.getImplicitWait())
        );
        logger.info("Test setup completed (headless: {})", isHeadless);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Test teardown completed");
        }
    }

    protected void takeScreenshot(String testName) {
        if (Config.isScreenshotOnFailure() && driver != null) {
            try {
                if (driver instanceof TakesScreenshot) {
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment("Screenshot: " + testName, 
                        new ByteArrayInputStream(screenshot));
                    
                    File screenshotDir = new File("screenshots");
                    if (!screenshotDir.exists()) {
                        screenshotDir.mkdirs();
                    }
                    String timestamp = LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                    );
                    String screenshotPath = String.format("screenshots/%s_%s.png", testName, timestamp);
                    File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    screenshotFile.renameTo(new File(screenshotPath));
                    logger.info("Screenshot saved: {}", screenshotPath);
                }
            } catch (Exception e) {
                logger.error("Failed to take screenshot: {}", e.getMessage());
            }
        }
    }
}
