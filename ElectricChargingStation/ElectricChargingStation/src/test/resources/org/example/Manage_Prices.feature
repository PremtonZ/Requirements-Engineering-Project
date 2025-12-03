Feature: Manage Prices
  As an Admin,
  I want to manage prices,
  so that I can set pricing for charging services

  Scenario: Ladepunktpreis festlegen (AC/DC)
    Given I am logged in as admin
    And a location with name "Deutschwagram" exists
    And a charging point with name "AC_1" exists at location "Deutschwagram"
    When I set the charging point price for "AC_1" at location "Deutschwagram" to AC price per kWh "0.42" and AC price per minute "0.05"
    Then the charging point "AC_1" at location "Deutschwagram" has AC price per kWh "0.42"
    And the charging point "AC_1" at location "Deutschwagram" has AC price per minute "0.05"
    And the charging point price is saved successfully

  Scenario: Standortpreis festlegen (AC/DC)
    Given I am logged in as admin
    And a location with name "Deutschwagram" exists
    When I set the location price for "Deutschwagram" to AC kWh "0.42", AC per minute "0.05", DC kWh "0.55", and DC per minute "0.05"
    Then the location "Deutschwagram" has AC price per kWh "0.42"
    And the location "Deutschwagram" has AC price per minute "0.05"
    And the location "Deutschwagram" has DC price per kWh "0.55"
    And the location "Deutschwagram" has DC price per minute "0.05"
    And the location price is saved successfully
