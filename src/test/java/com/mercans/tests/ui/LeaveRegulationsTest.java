package com.mercans.tests.ui;

import com.mercans.config.Config;
import com.mercans.pages.LeavesPage;
import com.mercans.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Leave Regulations Tests")
public class LeaveRegulationsTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(LeaveRegulationsTest.class);
    protected LeavesPage leavesPage;

    @BeforeEach
    public void loginAsEmployeeAndNavigateToLeaves() {
        driver.get(Config.getBaseUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAndHandleWelcome(Config.getEmployeeUsername(), Config.getEmployeePassword());
        
        leavesPage = new LeavesPage(driver);
        leavesPage.navigateToLeaves();
        assertTrue(leavesPage.isLeavesPageLoaded(), "Leaves page should be loaded");
    }

    @Test
    @DisplayName("TC-001: Verify Annual Leave Balance is 28 days")
    public void testAnnualLeaveBalance28Days() {
        assertTrue(leavesPage.isLeavesPageLoaded(), "Leaves page should be loaded");
        String balance = leavesPage.getLeaveBalance();
        logger.info("Annual leave balance: ", balance);
        
        assertTrue(balance.contains("28"), "Annual leave balance should show 28 days");
    }

    @Test
    @DisplayName("TC-002: Verify sick leave is not available for employee (only manager can)")
    public void testSickLeaveIsNotAvailableForEmployee() {
        assertFalse(leavesPage.isSickLeaveSectionPresent(), 
            "Div containing 'Sick leave' text should not be present for employees");
        logger.info("Verified employee cannot request sick leave - Sick leave option not present");
    }

}
