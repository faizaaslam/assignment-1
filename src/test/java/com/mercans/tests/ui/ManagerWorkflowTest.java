package com.mercans.tests.ui;

import com.mercans.config.Config;
import com.mercans.pages.LeavesPage;
import com.mercans.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Manager Workflow Tests")
public class ManagerWorkflowTest extends BaseTest {
   
    @BeforeEach
    public void loginAsManager() {
        driver.get(Config.getBaseUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAndHandleWelcome(Config.getManagerUsername(), Config.getManagerPassword());
    }

    @Test
    @DisplayName("TC-08: Verify Manager is able to view sick leave requests")
    public void testManagerViewsSickLeaveRequests() {
        LeavesPage leavesPage = new LeavesPage(driver);
        leavesPage.navigateToLeaves();
        
        assertTrue(leavesPage.isLeavesPageLoaded(), "Leaves page should be loaded");
        assertTrue(leavesPage.isSickLeaveSectionPresent(), "Manager should be able to view sick leave requests");
    }
}
