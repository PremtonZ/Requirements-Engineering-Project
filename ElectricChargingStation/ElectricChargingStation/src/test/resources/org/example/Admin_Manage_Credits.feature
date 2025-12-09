Feature: Admin Manage Credits
    As an Admin,
    I want to manage customer credit,
    so that I can handle customer balance

    Scenario: Show Customer Balance
        Given I am logged in as admin
        And a customer account with username "Max" exists
        And customer account "Max" has 150.0 credits
        When I view the credit balance for customer account "Max"
        Then I see that the account has 150.0 credits
        And the credit balance is displayed correctly

    Scenario: Top-Up Customer Balance
        Given I am logged in as admin
        And a customer account with username "Max" exists
        And customer account "Max" has 100.0 credits
        When I top up 50.0 credits to customer account "Max"
        Then the account balance increases by 50.0 credits
        And the account now has 150.0 credits
        And the credit top-up is successful

    Scenario: Show Customer Balance History
