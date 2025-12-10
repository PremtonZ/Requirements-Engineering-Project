Feature: Inform Invoice Status
  As an admin
  I want to review invoices of my customers
  so that I can view them

  Background:
    Given I am logged in as admin
    And I am in the invoice tab

  Scenario: Read Invoice
    Given an invoice with item number "42" exists for customer "Max"
    When I click on an invoice in the invoice history with item number "42"
    Then I see the details of the invoice:
      | field                     | value                      |
      | invoice item number       | 42                         |
      | start time                | 2025-11-26T14:10:00        |
      | location name             | Stephansplatz              |
      | charging point            | AC_1                       |
      | charging mode             | AC                         |
      | duration of use           | 27 minutes                 |
      | loaded energy (kWh)       | 12.0 kWh                   |
      | price                     | 5.83 EUR                   |
      | customer identity         | Max                        |
    And all invoice information is displayed correctly for admin
