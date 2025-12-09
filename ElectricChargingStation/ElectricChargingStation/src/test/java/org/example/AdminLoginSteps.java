package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AdminLoginSteps {

    // Login Admin Account
    @Given("I am not logged in")
    public void iAmNotLoggedIn() {
        TestContext.isLoggedIn = false;
        TestContext.currentUser = null;
        TestContext.canAccessDashboard = false;
    }

    @Given("I am logged in as admin")
    public void iAmLoggedInAsAdmin() {
        TestContext.isLoggedIn = true;
        TestContext.currentUser = "admin";
        TestContext.canAccessDashboard = true;
    }

    @When("I login as admin with username {string} and password {string}")
    public void iLoginAsAdminWithUsernameAndPassword(String username, String password) {
        if (username.equals("admin") && password.equals("admin123")) {
            TestContext.isLoggedIn = true;
            TestContext.currentUser = "admin";
            TestContext.canAccessDashboard = true;
        } else {
            TestContext.isLoggedIn = false;
            TestContext.currentUser = null;
            TestContext.canAccessDashboard = false;
        }
    }

    @Then("I am successfully logged in as admin")
    public void iAmSuccessfullyLoggedInAsAdmin() {
        assertTrue(TestContext.isLoggedIn, "User should be logged in");
        assertEquals("admin", TestContext.currentUser, "Current user should be admin");
    }

    @Then("I can access the admin dashboard")
    public void iCanAccessTheAdminDashboard() {
        assertTrue(TestContext.canAccessDashboard, "User should be able to access admin dashboard");
    }

    // Logout Admin Account
    @When("I logout as admin")
    public void iLogoutAsAdmin() {
        TestContext.isLoggedIn = false;
        TestContext.currentUser = null;
        TestContext.canAccessDashboard = false;
    }

    @Then("I am successfully logged out")
    public void iAmSuccessfullyLoggedOut() {
        assertFalse(TestContext.isLoggedIn, "User should be logged out");
        assertNull(TestContext.currentUser, "Current user should be null");
    }

    @Then("I cannot access the admin dashboard anymore")
    public void iCannotAccessTheAdminDashboardAnymore() {
        assertFalse(TestContext.canAccessDashboard, "User should not be able to access admin dashboard");
    }

    @Given("a location with name {string} exists")
    public void aLocationWithNameExists(String locationName) {
        try {
            TestContext.network.createSite(locationName);
        } catch (IllegalArgumentException e) {
        }
    }

    @Given("the following locations exist:")
    public void theFollowingLocationsExist(DataTable dataTable) {
        for (int i = 0; i < dataTable.height(); i++) {
            String locationName = dataTable.cell(i, 0);
            try {
                TestContext.network.createSite(locationName);
            } catch (IllegalArgumentException e) {
            }
        }
    }


    @Given("a customer account with username {string} exists")
    public void aCustomerAccountWithUsernameExists(String username) {
        try {
            TestContext.network.createAccount(username);
        } catch (IllegalArgumentException e) {
        }
    }

    @Given("customer account {string} has {double} credits")
    public void customerAccountHasCredits(String username, double credits) {
        Account account = TestContext.network.getAccount(username);
        try {
            java.lang.reflect.Field creditField = Account.class.getDeclaredField("credit");
            creditField.setAccessible(true);
            creditField.setDouble(account, credits);
        } catch (Exception e) {
            double currentCredits = account.getCredit();
            double difference = credits - currentCredits;
            if (difference > 0) {
                LocalDate now = LocalDate.now();
                TestContext.network.addCredit(username, difference, now.getDayOfMonth(), now.getMonthValue(), now.getYear());
            }
        }
    }

    @Given("I am not logged in as customer")
    public void iAmNotLoggedInAsCustomer() {
        TestContext.isCustomerLoggedIn = false;
        TestContext.currentCustomer = null;
        TestContext.canAccessCustomerAccount = false;
    }

    @Given("I am logged in as customer {string}")
    public void iAmLoggedInAsCustomer(String username) {
        try {
            TestContext.network.getAccount(username);
        } catch (IllegalArgumentException e) {
            try {
                TestContext.network.createAccount(username);
            } catch (IllegalArgumentException e2) {
            }
        }
        TestContext.isCustomerLoggedIn = true;
        TestContext.currentCustomer = username;
        TestContext.canAccessCustomerAccount = true;
    }

    @Given("I am logged in as customer")
    public void iAmLoggedInAsCustomer() {
        // Default customer login without specific username
        String defaultCustomer = "Max";
        try {
            TestContext.network.getAccount(defaultCustomer);
        } catch (IllegalArgumentException e) {
            try {
                TestContext.network.createAccount(defaultCustomer);
            } catch (IllegalArgumentException e2) {
            }
        }
        TestContext.isCustomerLoggedIn = true;
        TestContext.currentCustomer = defaultCustomer;
        TestContext.canAccessCustomerAccount = true;
    }

    @Given("I am logged in as a customer")
    public void iAmLoggedInAsACustomer() {
        // Same as "I am logged in as customer"
        iAmLoggedInAsCustomer();
    }

    @Given("I am logged in as owner")
    public void iAmLoggedInAsOwner() {
        TestContext.isLoggedIn = true;
        TestContext.currentUser = "owner";
        TestContext.canAccessDashboard = true;
    }

    @Given("location {string} has the following prices:")
    public void locationHasTheFollowingPrices(String location, DataTable dataTable) {
        double acKwh = Double.parseDouble(dataTable.cell(1, 0));
        double acPpm = Double.parseDouble(dataTable.cell(1, 1));
        double dcKwh = Double.parseDouble(dataTable.cell(1, 2));
        double dcPpm = Double.parseDouble(dataTable.cell(1, 3));
        TestContext.network.setSitePrices(location, acKwh, acPpm, dcKwh, dcPpm);
    }
}
