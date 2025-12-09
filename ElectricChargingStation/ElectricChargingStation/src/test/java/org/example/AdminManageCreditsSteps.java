package org.example;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class AdminManageCreditsSteps {
    private Account viewedAccount;
    private double initialBalance;


    // Show Customer Balance
    @When("I view the credit balance for customer account {string}")
    public void iViewTheCreditBalanceForCustomerAccount(String username) {
        viewedAccount = TestContext.network.getAccount(username);
    }

    @Then("I see that the account has {double} credits")
    public void iSeeThatTheAccountHasCredits(double expectedCredits) {
        assertNotNull(viewedAccount, "Account should be found");
        assertEquals(expectedCredits, viewedAccount.getCredit(), 0.001, "Account credits should match");
    }

    @Then("the credit balance is displayed correctly")
    public void theCreditBalanceIsDisplayedCorrectly() {
        assertNotNull(viewedAccount, "Account should be found");
        assertTrue(viewedAccount.getCredit() >= 0, "Credit balance should be non-negative");
    }

    // Top-Up Customer Balance
    @When("I top up {double} credits to customer account {string}")
    public void iTopUpCreditsToCustomerAccount(double amount, String username) {
        Account account = TestContext.network.getAccount(username);
        initialBalance = account.getCredit();
        TestContext.network.addCredit(username, amount);
        viewedAccount = TestContext.network.getAccount(username);
    }

    @Then("the account balance increases by {double} credits")
    public void theAccountBalanceIncreasesByCredits(double expectedIncrease) {
        assertNotNull(viewedAccount, "Account should be found");
        assertEquals(initialBalance + expectedIncrease, viewedAccount.getCredit(), 0.001, "Account balance should increase correctly");
    }

    @Then("the account now has {double} credits")
    public void theAccountNowHasCredits(double expectedCredits) {
        assertNotNull(viewedAccount, "Account should be found");
        assertEquals(expectedCredits, viewedAccount.getCredit(), 0.001, "Account credits should match");
    }

    @Then("the credit top-up is successful")
    public void theCreditTopUpIsSuccessful() {
        assertNotNull(viewedAccount, "Account should be found");
        assertTrue(viewedAccount.getCredit() > initialBalance, "Credit should have increased");
    }
}

