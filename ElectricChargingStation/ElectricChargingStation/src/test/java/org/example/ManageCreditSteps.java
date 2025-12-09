package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ManageCreditSteps {
    private double initialBalance;

    @When("I top up {double} credits to my account")
    public void iTopUpCreditsToMyAccount(double amount) {
        Account account = TestContext.network.getAccount(TestContext.currentCustomer);
        initialBalance = account.getCredit();
        LocalDate now = LocalDate.now();
        TestContext.network.addCredit(TestContext.currentCustomer, amount, now.getDayOfMonth(), now.getMonthValue(), now.getYear());
    }

    @Then("my account balance increases by {double} credits")
    public void myAccountBalanceIncreasesByCredits(double expectedIncrease) {
        Account account = TestContext.network.getAccount(TestContext.currentCustomer);
        double actualIncrease = account.getCredit() - initialBalance;
        assertEquals(expectedIncrease, actualIncrease, 0.001, "Account balance should increase by expected amount");
    }

    @Then("my account now has {double} credits")
    public void myAccountNowHasCredits(double expectedCredits) {
        Account account = TestContext.network.getAccount(TestContext.currentCustomer);
        assertEquals(expectedCredits, account.getCredit(), 0.001, "Account should have expected credits");
    }

    @When("I view my credit balance")
    public void iViewMyCreditBalance() {
        // Balance is viewed, no action needed
        assertNotNull(TestContext.currentCustomer, "Customer should be logged in");
    }

    @Then("I see that my account has {double} credits")
    public void iSeeThatMyAccountHasCredits(double expectedCredits) {
        Account account = TestContext.network.getAccount(TestContext.currentCustomer);
        assertEquals(expectedCredits, account.getCredit(), 0.001, "Account should have expected credits");
    }

    @When("I top up {double} credits to my account on date {int}-{int}-{int} using {string}")
    public void iTopUpCreditsToMyAccountOnDateUsing(double amount, int year, int month, int day, String source) {
        TestContext.network.addCredit(TestContext.currentCustomer, amount, day, month, year);
    }

    @When("I view my credit balance history")
    public void iViewMyCreditBalanceHistory() {
        assertNotNull(TestContext.currentCustomer, "Customer should be logged in");
    }
}

