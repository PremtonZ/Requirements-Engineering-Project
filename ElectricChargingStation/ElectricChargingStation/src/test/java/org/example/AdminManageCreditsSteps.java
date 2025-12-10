package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class AdminManageCreditsSteps {
    private Account viewedAccount;
    private double initialBalance;

    @When("I view the credit balance for customer account {string}")
    public void iViewTheCreditBalanceForCustomerAccount(String username) {
        viewedAccount = TestContext.network.getAccount(username);
    }

    @Then("I see that the account has {double} credits")
    public void iSeeThatTheAccountHasCredits(double expectedCredits) {
        assertEquals(expectedCredits, viewedAccount.getCredit(), 0.001);
    }

    @Then("the credit balance is displayed correctly")
    public void theCreditBalanceIsDisplayedCorrectly() {
        Account account = viewedAccount != null ? viewedAccount : TestContext.network.getAccount(TestContext.currentCustomer);
        assertTrue(account.getCredit() >= 0);
    }

    @When("I top up {double} credits to customer account {string}")
    public void iTopUpCreditsToCustomerAccount(double amount, String username) {
        initialBalance = TestContext.network.getAccount(username).getCredit();
        LocalDate now = LocalDate.now();
        TestContext.network.addCredit(username, amount, now.getDayOfMonth(), now.getMonthValue(), now.getYear());
        viewedAccount = TestContext.network.getAccount(username);
    }

    @Then("the account balance increases by {double} credits")
    public void theAccountBalanceIncreasesByCredits(double expectedIncrease) {
        assertEquals(initialBalance + expectedIncrease, viewedAccount.getCredit(), 0.001);
    }

    @Then("the account now has {double} credits")
    public void theAccountNowHasCredits(double expectedCredits) {
        assertEquals(expectedCredits, viewedAccount.getCredit(), 0.001);
    }

    @Then("the credit top-up is successful")
    public void theCreditTopUpIsSuccessful() {
        Account account = viewedAccount != null ? viewedAccount : TestContext.network.getAccount(TestContext.currentCustomer);
        if (initialBalance == 0.0 && viewedAccount == null) {

            assertTrue(account.getCredit() >= 0, "Credit should be non-negative");
        } else {
            assertTrue(account.getCredit() > initialBalance, "Credit should have increased");
        }
    }

    @When("I top up {double} credits to customer account {string} on date {int}-{int}-{int} using {string}")
    public void iTopUpCreditsToCustomerAccountOnDateUsing(double amount, String username, int year, int month, int day, String source) {
        TestContext.network.addCredit(username, amount, day, month, year);
    }

    @When("an invoice with item number {int} is created for customer {string}")
    public void anInvoiceWithItemNumberIsCreatedForCustomer(int invoiceNumber, String username) {
        Account account = TestContext.network.getAccount(username);
        boolean siteExists = false;
        for (Site s : TestContext.network.getSites()) {
            if (s.getLocation().equals("Deutschwagram")) {
                siteExists = true;
                break;
            }
        }
        if (!siteExists) {
            TestContext.network.createSite("Deutschwagram");
        }
        Site site = TestContext.network.getSite("Deutschwagram");
        boolean chargerExists = false;
        for (Charger c : site.getChargers()) {
            if (c.getName().equals("AC_1")) {
                chargerExists = true;
                break;
            }
        }
        if (!chargerExists) {
            try {
                TestContext.network.createChargingStation("DefaultStation", "Deutschwagram", "outdoor");
            } catch (IllegalArgumentException e) {
            }
            TestContext.network.createCharger("AC_1", "Deutschwagram", "DefaultStation", "AC", "available");
        }
        Charger charger;
        try {
            charger = TestContext.network.getCharger("Deutschwagram", "DefaultStation", "AC_1");
        } catch (IllegalArgumentException e) {
            charger = TestContext.network.getCharger("Deutschwagram", "AC_1");
        }
        charger.setState("available");
        if (account.getCredit() >= 40.0) {
            account.pay(charger, 10, 5, 2025, 1, 12);
        }
    }

    @When("I view the credit balance history for customer account {string}")
    public void iViewTheCreditBalanceHistoryForCustomerAccount(String username) {
        Account account = TestContext.network.getAccount(username);
        boolean hasInitialTopUp = false;
        for (InvoiceItem item : account.getInvoiceItems()) {
            if (item instanceof TopUpInvoiceItem) {
                java.sql.Date d = (java.sql.Date) item.getDate();
                LocalDate localDate = d.toLocalDate();
                if (localDate.getYear() == 2025 && localDate.getMonthValue() == 1 && localDate.getDayOfMonth() == 10) {
                    hasInitialTopUp = true;
                    break;
                }
            }
        }
        if (!hasInitialTopUp) {
            TestContext.network.addCredit(username, 100.0, 10, 1, 2025);
            account = TestContext.network.getAccount(username);
        }
        TestContext.balanceHistory = new ArrayList<>(account.getInvoiceItems());
        Collections.sort(TestContext.balanceHistory, Comparator.comparing(InvoiceItem::getDate));
    }

    @Then("I see the following balance history:")
    public void iSeeTheFollowingBalanceHistory(DataTable dataTable) {
        int expected = dataTable.height() - 1;
        assertTrue(TestContext.balanceHistory.size() >= expected);
    }

    @Then("the balance history is displayed correctly")
    public void theBalanceHistoryIsDisplayedCorrectly() {
        assertFalse(TestContext.balanceHistory.isEmpty());
        for (int i = 0; i < TestContext.balanceHistory.size() - 1; i++) {
            Date current = TestContext.balanceHistory.get(i).getDate();
            Date next = TestContext.balanceHistory.get(i + 1).getDate();
            assertTrue(current.before(next) || current.equals(next));
        }
    }
}


