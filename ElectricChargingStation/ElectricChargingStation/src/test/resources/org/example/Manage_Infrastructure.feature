Feature: Manage Infrastructure
  As an Admin,
  I want to manage infrastructure,
  so that I can offer charging services

  Scenario: Standort anlegen
    Given I am logged in as admin
    When I create a location with name "Deutschwagram"
    Then a location with name "Deutschwagram" is created successfully
    And the location is available in the network

  Scenario: Ladepunkt hinzufügen
    Given I am logged in as admin
    And a location with name "Deutschwagram" exists
    When I add a charging point with name "AC_1" to location "Deutschwagram"
    Then a charging point with name "AC_1" is created successfully at location "Deutschwagram"
    And the charging point is available for use

  Scenario: Ladepunkt-Typ festlegen (AC/DC)
    Given I am logged in as admin
    And a location with name "Deutschwagram" exists
    And a charging point with name "AC_1" exists at location "Deutschwagram"
    When I set the charging point type to "AC" for charging point "AC_1" at location "Deutschwagram"
    Then the charging point "AC_1" at location "Deutschwagram" has type "AC"
    And the charging point type is saved successfully
