Feature: Customer Login
  As a Customer,
  I want to login to my account,
  so that I can access my account and use the charging services

  Scenario: Login Customer Account
    Given I am not logged in as customer
    And a customer account with username "Max" exists
    When I login as customer with username "Max"
    Then I am successfully logged in as customer "Max"
    And I can access my customer account
