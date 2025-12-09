Feature: Manage Credit
  As an Customer,
  I want to manage my credit,
  so that I can handle my account

  Background:
    Given I am logged in as customer "Max"
    And a customer account with username "Max" exists
    And customer account "Max" has 100.0 credits

  Scenario: Top-Up Balance
    When I top up 50.0 credits to my account
    Then my account balance increases by 50.0 credits
    And my account now has 150.0 credits
    And the credit top-up is successful

  Scenario: Read Balance
    And customer account "Max" has 150.0 credits
    When I view my credit balance
    Then I see that my account has 150.0 credits
    And the credit balance is displayed correctly

  Scenario: Read Balance History
    When I top up 50.0 credits to my account on date 2025-01-15 using "credit card"
    And I top up 25.0 credits to my account on date 2025-01-20 using "voucher"
    And an invoice with item number 42 is created for customer "Max"
    And I view my credit balance history
    Then I see the following balance history:
      | Date       | Transaction Type | Credits | Source       | Reference      |
      | 2025-01-10 | topup            | 100.0   | bank transfer| —              |
      | 2025-01-12 | withdrawal       | -40.0   | —            | invoice #42    |
      | 2025-01-15 | topup            | 50.0    | credit card  | —              |
      | 2025-01-20 | topup            | 25.0    | voucher      | —              |
    And the balance history is displayed correctly
  
