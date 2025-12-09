Feature: Admin Manage Credits
    As an Owner,
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
        Given I am logged in as admin
        And a customer account with username "Max" exists
        And customer account "Max" has 100.0 credits
        When I top up 50.0 credits to customer account "Max" on date 2025-01-15 using "credit card"
        And I top up 25.0 credits to customer account "Max" on date 2025-01-20 using "voucher"
        And an invoice with item number 42 is created for customer "Max"
        And I view the credit balance history for customer account "Max"
        Then I see the following balance history:
          | Date       | Transaction Type | Credits | Source       | Reference      |
          | 2025-01-10 | topup            | 100.0   | bank transfer| —              |
          | 2025-01-12 | withdrawal       | -40.0   | —            | invoice #42    |
          | 2025-01-15 | topup            | 50.0    | credit card  | —              |
          | 2025-01-20 | topup            | 25.0    | voucher      | —              |
        And the balance history is displayed correctly

