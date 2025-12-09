Feature: Customer Manage Account
  As a Customer
  I want to manage my account
  so that I can access charging features and view my information

  Scenario: Create Customer Account
    Given I am not logged in
    When I register a new customer account with the following information:
      | field     | value               |
      | username  | Max                 |
      | email     | max@example.com     |
      | password  | secret123           |
    Then a customer account with username "Max" is created successfully
      And I receive a confirmation message "Registration successful"
      And the new account has 0.0 credits
      And I can log in with the newly created credentials

  Scenario: Read Customer Account
    Given I am logged in as customer "Max"
      And customer account "Max" exists with the following information:
        | username | Max                 |
        | email    | max@example.com     |
        | credits  | 120.0               |
    When I view my customer account details
    Then I see the following information:
      | Username | Email             | Credits |
      | Max      | max@example.com   | 120.0   |
    And the account details are only visible to me
