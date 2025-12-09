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

  # MVP 2
  Scenario: Update Customer Account
    Given I am logged in as admin
      And a customer account with username "Max" exists
      And customer account "Max" has the following data:
        | field    | value             |
        | email    | max@example.com   |
        | name     | Max Mustermann    |
    When I update the customer account "Max" with:
      | field | new value          |
      | email | max.new@example.com|
      | name  | Max M.             |
      And I save the changes
    Then the update is accepted
      And the updated customer information is persisted in the database
      And when I retrieve the customer account "Max" I see:
        | field | value               |
        | email | max.new@example.com |
        | name  | Max M.              |

  Scenario: Delete Customer Account
    Given I am logged in as admin
      And a customer account with username "Max" exists
    When I delete the customer account with username "Max"
    Then the account is permanently removed from the system
      And attempts to retrieve customer account "Max" return "not found"
      And the account data is removed from the database

  Scenario: Deactivate Customer Account
    Given I am logged in as admin
      And a customer account with username "Max" exists
      And customer account "Max" is active
    When I deactivate the customer account "Max"
    Then the account status is set to "inactive"
      And the account is blocked from logging in
      And attempts to perform actions requiring an active account fail with "account inactive"