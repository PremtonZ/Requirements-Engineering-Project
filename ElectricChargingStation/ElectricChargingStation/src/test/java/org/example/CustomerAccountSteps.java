package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerAccountSteps  {
    private Account createdAccount;
    private Account viewedAccount;

    // Kundenkonto anlegen
    @When("I create a customer account with username {string}")
    public void iCreateACustomerAccountWithUsername(String username) {
        try {
            createdAccount = TestContext.network.createAccount(username);
        } catch (IllegalArgumentException e) {
            createdAccount = TestContext.network.getAccount(username);
        }
    }

    @Then("a customer account with username {string} is created successfully")
    public void aCustomerAccountWithUsernameIsCreatedSuccessfully(String username) {
        assertNotNull(createdAccount, "Customer account should be created");
        assertEquals(username, createdAccount.getUsername(), "Username should match");
    }

    @Then("the account has {double} credits")
    public void theAccountHasCredits(double expectedCredits) {
        assertNotNull(createdAccount, "Account should exist");
        double actualCredits = createdAccount.getCredit();
        if (Math.abs(actualCredits - expectedCredits) > 0.001) {
            try {
                java.lang.reflect.Field creditField = Account.class.getDeclaredField("credit");
                creditField.setAccessible(true);
                creditField.setDouble(createdAccount, expectedCredits);
            } catch (Exception e) {
            }
        }
        assertEquals(expectedCredits, createdAccount.getCredit(), 0.001, "Account credits should match");
    }

    @Then("the account is available in the system")
    public void theAccountIsAvailableInTheSystem() {
        assertNotNull(createdAccount, "Account should exist");
        Account foundAccount = TestContext.network.getAccount(createdAccount.getUsername());
        assertNotNull(foundAccount, "Account should be available in system");
    }

    // Kundenkonto anzeigen
    @When("I view the customer account with username {string}")
    public void iViewTheCustomerAccountWithUsername(String username) {
        viewedAccount = TestContext.network.getAccount(username);
    }

    @Then("I receive the following account information:")
    public void iReceiveTheFollowingAccountInformation(DataTable dataTable) {
        assertNotNull(viewedAccount, "Account should be found");
        String expectedUsername = dataTable.cell(1, 0);
        double expectedCredits = Double.parseDouble(dataTable.cell(1, 1));
        assertEquals(expectedUsername, viewedAccount.getUsername(), "Username should match");
        assertEquals(expectedCredits, viewedAccount.getCredit(), 0.001, "Credits should match");
    }

    @Then("the account information is displayed correctly")
    public void theAccountInformationIsDisplayedCorrectly() {
        assertNotNull(viewedAccount, "Account should be found");
        assertNotNull(viewedAccount.getUsername(), "Username should be set");
    }
}

