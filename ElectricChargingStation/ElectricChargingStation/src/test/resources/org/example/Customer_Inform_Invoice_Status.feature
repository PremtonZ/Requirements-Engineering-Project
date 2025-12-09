Feature: Customer Inform Invoice Status
  As a Customer
  I want to view my invoices
  so that I can check the details and keep records

  Scenario: Read Invoice
    Given I am logged in as a customer
      And an invoice with item number "42" exists for my account
    When I open the invoice with item number "42"
    Then I see the following invoice details:
      | field                     | value                      |
      | location name             | Stephansplatz              |
      | charging point            | AC_1                       |
      | charging mode             | AC                         |
      | start time                | 2025-11-26T14:10:00        |
      | duration of use           | 27 minutes                 |
      | loaded energy (kWh)       | 12.4 kWh                   |
      | price                     | 5.83 EUR                   |
      | taxes                     | included                   |
      | payment method            | wallet (credit balance)    |
    And all invoice information is displayed correctly

