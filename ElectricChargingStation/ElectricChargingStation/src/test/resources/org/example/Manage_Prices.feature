Feature: Manage Prices
  As an Owner,
  I want to manage prices,
  so that I can set pricing for charging services

  Scenario: Set Location Prices (AC/DC)
    Given I am logged in as admin
    And a location with name "Deutschwagram" exists
    When I set the location price for "Deutschwagram" to AC kWh "0.42", AC per minute "0.05", DC kWh "0.55", and DC per minute "0.05"
    Then the location "Deutschwagram" has AC price per kWh "0.42"
    And the location "Deutschwagram" has AC price per minute "0.05"
    And the location "Deutschwagram" has DC price per kWh "0.55"
    And the location "Deutschwagram" has DC price per minute "0.05"
    And the location price is saved successfully
   
