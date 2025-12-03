Feature: Admin Manage Customer Account
  As an Admin,
  I want to manage customer accounts,
  so that I can handle customer information

  Scenario: Create Customer Account 
    Given I am logged in as admin
    When I create a customer account with username "Max"
    Then a customer account with username "Max" is created successfully
    And the account has 0.0 credits
    And the account is available in the system

  Scenario: Read Customer Account
    Given I am logged in as admin
    And a customer account with username "Max" exists
    And customer account "Max" has 200.0 credits
    When I view the customer account with username "Max"
    Then I receive the following account information:
      | Username | Credits |
      | Max      | 200.0   |
    And the account information is displayed correctly

  // MVP 2
  Scenario: Deactivate Customer Account

  Scenario: Delete Customer Account

  Scenario: Deactivate Customer Account