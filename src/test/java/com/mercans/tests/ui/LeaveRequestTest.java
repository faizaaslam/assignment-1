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

@DisplayName("Leave Request Tests")
public class LeaveRequestTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestTest.class);

    @BeforeEach
    public void loginAndNavigateToLeaves() {
        driver.get(Config.getBaseUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAndHandleWelcome(Config.getEmployeeUsername(), Config.getEmployeePassword());
        
        LeavesPage leavesPage = new LeavesPage(driver);
        leavesPage.navigateToLeaves();
        assertTrue(leavesPage.isLeavesPageLoaded(), "Leaves page should be loaded after navigation");
    }

    @Test
    @DisplayName("TC-003: Verify user is able to Request and Withdraw Annual Leave")
    public void testRequestAndWithdrawAnnualLeave() {
        LeavesPage leavesPage = new LeavesPage(driver);
        
        leavesPage.clickRequestLeave();
        logger.info("Step 1: Clicked 'Request new leave' button");
        
        String startDateId = leavesPage.selectRandomStartDate();
        logger.info("Step 2: Selected random start date with id: {}", startDateId);
        
        String endDateId = leavesPage.selectRandomEndDate(startDateId);
        logger.info("Step 3: Selected random end date with id: {} (after start date)", endDateId);
        
        leavesPage.enterNote(Config.getLeaveNote());
        logger.info("Step 4: Entered note 'Annual leaves'");
        
        leavesPage.clickSubmitApprove();
        logger.info("Step 5: Clicked submit/approve button");
        
        leavesPage.clickPrimaryActionButton();
        logger.info("Step 6: Clicked primary action button");
        
        assertTrue(leavesPage.verifyRequestStatusContainsPending(), "Request status should contain 'Pending'");
        logger.info("Step 7: Verified request status contains 'Pending'");
        
        leavesPage.openLeaveRequest();
        logger.info("Step 8: Opened leave request");

        leavesPage.withdrawLeaveRequest();
        logger.info("Step 9: Clicked withdraw request button");
        
        leavesPage.clickPrimaryActionButton();
        logger.info("Step 10: Clicked primary action button to confirm withdrawal");
        
        assertTrue(leavesPage.verifyLeavesBalance(Config.getExpectedBalanceAfterWithdrawal()), 
            "Leaves balance should show expected value after withdrawal");
        logger.info("Step 11: Verified leaves webapp contains 'Estimated balance28 d'");
        
        logger.info("Leave request and withdrawal completed successfully");
    }

}
