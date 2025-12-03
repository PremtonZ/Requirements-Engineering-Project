package org.example;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerLoginSteps  {

    // Kundenkonto einloggen
    @When("I login as customer with username {string}")
    public void iLoginAsCustomerWithUsername(String username) {
        try {
            Account account = TestContext.network.getAccount(username);
            if (account != null) {
                TestContext.isCustomerLoggedIn = true;
                TestContext.currentCustomer = username;
                TestContext.canAccessCustomerAccount = true;
            } else {
                TestContext.isCustomerLoggedIn = false;
                TestContext.currentCustomer = null;
                TestContext.canAccessCustomerAccount = false;
            }
        } catch (Exception e) {
            TestContext.isCustomerLoggedIn = false;
            TestContext.currentCustomer = null;
            TestContext.canAccessCustomerAccount = false;
        }
    }

    @Then("I am successfully logged in as customer {string}")
    public void iAmSuccessfullyLoggedInAsCustomer(String username) {
        assertTrue(TestContext.isCustomerLoggedIn, "Customer should be logged in");
        assertEquals(username, TestContext.currentCustomer, "Current customer should match");
    }

    @Then("I can access my customer account")
    public void iCanAccessMyCustomerAccount() {
        assertTrue(TestContext.canAccessCustomerAccount, "Customer should be able to access account");
        assertNotNull(TestContext.currentCustomer, "Current customer should be set");
    }
}

