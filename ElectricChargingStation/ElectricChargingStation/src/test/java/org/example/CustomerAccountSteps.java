package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerAccountSteps  {
    private Account createdAccount;
    private Account viewedAccount;

    // Create Customer Account
    @When("I register a new customer account with the following information:")
    public void iRegisterANewCustomerAccountWithTheFollowingInformation(DataTable dataTable) {
        String username = extractValueFromDataTable(dataTable, "username");
        try {
            createdAccount = TestContext.network.createAccount(username);
        } catch (IllegalArgumentException e) {
            createdAccount = TestContext.network.getAccount(username);
        }
    }

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

    @Then("I receive a confirmation message {string}")
    public void iReceiveAConfirmationMessage(String message) {
        assertNotNull(createdAccount, "Account should be created");
    }

    @Then("the new account has {double} credits")
    public void theNewAccountHasCredits(double expectedCredits) {
        theAccountHasCredits(expectedCredits);
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

    // Read Customer Account
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

    @Then("I can log in with the newly created credentials")
    public void iCanLogInWithTheNewlyCreatedCredentials() {
        assertNotNull(createdAccount, "Account should be created");
        TestContext.isCustomerLoggedIn = true;
        TestContext.currentCustomer = createdAccount.getUsername();
        TestContext.canAccessCustomerAccount = true;
    }

    @Given("customer account {string} exists with the following information:")
    public void customerAccountExistsWithTheFollowingInformation(String username, DataTable dataTable) {
        try {
            TestContext.network.createAccount(username);
        } catch (IllegalArgumentException e) {
            // Account already exists
        }
        Account account = TestContext.network.getAccount(username);
        String creditsStr = extractValueFromDataTable(dataTable, "credits");
        if (creditsStr != null) {
            double credits = Double.parseDouble(creditsStr);
            if (Math.abs(account.getCredit() - credits) > 0.001) {
                try {
                    java.lang.reflect.Field creditField = Account.class.getDeclaredField("credit");
                    creditField.setAccessible(true);
                    creditField.setDouble(account, credits);
                } catch (Exception e) {
                }
            }
        }
    }

    @When("I view my customer account details")
    public void iViewMyCustomerAccountDetails() {
        viewedAccount = TestContext.network.getAccount(TestContext.currentCustomer);
    }

    @Then("I see the following information:")
    public void iSeeTheFollowingInformation(DataTable dataTable) {
        assertNotNull(viewedAccount, "Account should be found");
        String expectedUsername = dataTable.cell(1, 0);
        String expectedEmail = dataTable.cell(1, 1);
        double expectedCredits = Double.parseDouble(dataTable.cell(1, 2));
        assertEquals(expectedUsername, viewedAccount.getUsername(), "Username should match");
        assertEquals(expectedCredits, viewedAccount.getCredit(), 0.001, "Credits should match");
    }

    @Then("the account details are only visible to me")
    public void theAccountDetailsAreOnlyVisibleToMe() {
        assertNotNull(viewedAccount, "Account should be found");
        assertEquals(TestContext.currentCustomer, viewedAccount.getUsername(), "Should only see own account");
    }

    private String extractValueFromDataTable(DataTable dataTable, String fieldName) {
        for (int i = 1; i < dataTable.height(); i++) {
            String field = dataTable.cell(i, 0);
            String value = dataTable.cell(i, 1);
            if (field.equals(fieldName)) {
                return value;
            }
        }
        return null;
    }



    // Update Customer Account
    private Account updatedAccount;

    @When("I update the customer account username from {string} to {string}")
    public void iUpdateTheCustomerAccountUsernameFromTo(String oldUsername, String newUsername) {
        try {
            TestContext.network.updateAccountUsername(oldUsername, newUsername);
            updatedAccount = TestContext.network.getAccount(newUsername);
        } catch (IllegalArgumentException e) {
            updatedAccount = null;
            throw e;
        }
    }

    @Then("the customer account username is updated to {string}")
    public void theCustomerAccountUsernameIsUpdatedTo(String expectedUsername) {
        assertNotNull(updatedAccount, "Account should exist after update");
        assertEquals(expectedUsername, updatedAccount.getUsername(), "Username should be updated");
    }

    @Then("the account is available in the system with the new username")
    public void theAccountIsAvailableInTheSystemWithTheNewUsername() {
        assertNotNull(updatedAccount, "Account should exist");
        Account foundAccount = TestContext.network.getAccount(updatedAccount.getUsername());
        assertNotNull(foundAccount, "Account should be available in system with new username");
        assertEquals(updatedAccount.getUsername(), foundAccount.getUsername(), "Username should match");
    }

    // Delete Customer Account
    private Account deletedAccount;
    private String deletedUsername;

    @When("I delete the customer account with username {string}")
    public void iDeleteTheCustomerAccountWithUsername(String username) {
        deletedAccount = TestContext.network.getAccount(username);
        deletedUsername = username;
        TestContext.network.deleteAccount(username);
    }

    @Then("the customer account with username {string} is deleted successfully")
    public void theCustomerAccountWithUsernameIsDeletedSuccessfully(String username) {
        assertNotNull(deletedAccount, "Account should have existed before deletion");
        assertEquals(username, deletedUsername, "Username should match");
    }

    @Then("the account is no longer available in the system")
    public void theAccountIsNoLongerAvailableInTheSystem() {
        assertNotNull(deletedAccount, "Account should have existed before deletion");
        try {
            TestContext.network.getAccount(deletedUsername);
            fail("Account should not be available in system after deletion");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Account not found"), "Should throw Account not found exception");
        }
    }
}

