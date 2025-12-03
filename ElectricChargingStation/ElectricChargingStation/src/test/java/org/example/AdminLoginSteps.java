package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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

    @Given("a charging point with name {string} exists at location {string}")
    public void aChargingPointWithNameExistsAtLocation(String chargerName, String locationName) {
        try {
            TestContext.network.createCharger(chargerName, locationName, "AC", "in operation free");
        } catch (IllegalArgumentException e) {
        }
    }

    @Given("the following charging points exist:")
    public void theFollowingChargingPointsExist(DataTable dataTable) {
        for (int i = 1; i < dataTable.height(); i++) {
            String name = dataTable.cell(i, 0);
            String location = dataTable.cell(i, 1);
            String mode = dataTable.cell(i, 2);
            String state = dataTable.cell(i, 3);
            try {
                TestContext.network.createCharger(name, location, mode, state);
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
                TestContext.network.addCredit(username, difference);
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
            Account account = TestContext.network.getAccount(username);
            if (account != null) {
                TestContext.isCustomerLoggedIn = true;
                TestContext.currentCustomer = username;
                TestContext.canAccessCustomerAccount = true;
            }
        } catch (Exception e) {
            TestContext.isCustomerLoggedIn = false;
            TestContext.currentCustomer = null;
            TestContext.canAccessCustomerAccount = false;
        }
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

