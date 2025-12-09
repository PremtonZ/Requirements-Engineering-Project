Feature: Manage Infrastructure
  As an Admin
  I want to manage infrastructure
  so that I can offer charging services

  Background:
    Given I am logged in as admin

  Scenario: Create Location
    When I create a location with name "Deutschwagram"
    Then a location with name "Deutschwagram" is created successfully
      And the location is available in the network

  Scenario: Create Charging Point
    Given I am logged in as admin
    And a location with name "Deutschwagram" exists
    When I add a charging point with name "AC_1" to location "Deutschwagram"
    Then a charging point with name "AC_1" is created successfully at location "Deutschwagram"
    And the charging point is available for use

  Scenario: Set Charging Point Mode (AC/DC)
    Given I am logged in as admin
    And a location with name "Deutschwagram" exists
    And a charging point with name "AC_1" exists at location "Deutschwagram"
    When I set the charging point type to "AC" for charging point "AC_1" at location "Deutschwagram"
    Then the charging point "AC_1" at location "Deutschwagram" has type "AC"
    And the charging point type is saved successfully

  Scenario: Read Location
    Given a location with name "Deutschwagram" exists with the following details:
      | field     | value          |
      | name      | Deutschwagram  |
      | address   | Musterstraße 1 |
      | country   | AT             |
    When I view the location details for "Deutschwagram"
    Then I see the location details:
      | field   | value          |
      | name    | Deutschwagram  |
      | address | Musterstraße 1 |
      | country | AT             |
    And the location details are displayed correctly

  Scenario: Update Location
    Given a location with name "Deutschwagram" exists
    When I update the location "Deutschwagram" with:
      | field   | new value       |
      | name    | Deutschwagram N |
      | address | Neue Strasse 5   |
    And I save the changes
    Then the location update is saved successfully
      And the location "Deutschwagram" has the updated details:
        | field   | value          |
        | name    | Deutschwagram N|
        | address | Neue Strasse 5 |

  Scenario: Delete Location
    Given a location "Deutschwagram" exists
      And the location has associated charging stations and charging points
    When I delete the location "Deutschwagram"
    Then the location is removed from the system
      And all charging stations assigned to "Deutschwagram" are removed
      And all charging points assigned to those stations are removed
      And attempts to retrieve the location return "not found"

  Scenario: Create Charging Station
    Given a location "Deutschwagram" exists
    When I create a charging station with name "Station_A" at location "Deutschwagram"
    Then the charging station "Station_A" is created successfully at location "Deutschwagram"
      And the charging station is available in the network

  Scenario: Read Charging Station
    Given a charging station "Station_A" exists at location "Deutschwagram" with the following:
      | field | value         |
      | name  | Station_A     |
      | type  | outdoor       |
    When I view charging station "Station_A" at location "Deutschwagram"
    Then I see the charging station details:
      | field | value         |
      | name  | Station_A     |
      | type  | outdoor       |
      | location | Deutschwagram |
    And the charging station details are displayed correctly

  Scenario: Update Charging Station 
    Given a charging station "Station_A" exists at location "Deutschwagram"
    When I update charging station "Station_A" with:
      | field | new value     |
      | name  | Station_A_01  |
      | type  | indoor        |
    And I save the changes
    Then the charging station update is saved successfully
      And charging station "Station_A" now has:
        | field | value       |
        | name  | Station_A_01|
        | type  | indoor      |

  Scenario: Delete Charging Station 
    Given a charging station "Station_A" exists at location "Deutschwagram"
      And "Station_A" has assigned charging points
    When I delete charging station "Station_A"
    Then the charging station is removed from the location
      And all charging points assigned to "Station_A" are removed
      And attempts to retrieve "Station_A" return "not found"

  Scenario: Create Charging Point
    Given a location "Deutschwagram" exists
      And a charging station "Station_A" exists at location "Deutschwagram"
    When I add a charging point with name "AC_1" and mode "AC" to charging station "Station_A" at location "Deutschwagram"
    Then a charging point with name "AC_1" is created successfully at charging station "Station_A"
      And the charging point is available for use

  Scenario: Read Charging Point
    Given a charging point "AC_1" exists at charging station "Station_A" in location "Deutschwagram" with:
      | field | value      |
      | name  | AC_1       |
      | mode  | AC         |
      | id    | CP-0001    |
    When I view the charging point "AC_1" at charging station "Station_A"
    Then I see the charging point details:
      | field | value     |
      | id    | CP-0001   |
      | name  | AC_1      |
      | mode  | AC        |
      | station | Station_A|
      | location| Deutschwagram |
    And the charging point information is displayed correctly

  Scenario: Update Charging Point
    Given a charging point "AC_1" exists at charging station "Station_A"
    When I update charging point "AC_1" with:
      | field | new value |
      | name  | AC_1_New  |
      | mode  | DC        |
    And I save the changes
    Then the charging point update is saved successfully
      And charging point "AC_1" now has:
        | field | value    |
        | name  | AC_1_New |
        | mode  | DC       |

  Scenario: Delete Charging Point
    Given a charging point "AC_1" exists at charging station "Station_A"
    When I delete the charging point "AC_1"
    Then the charging point is removed from the charging station
      And attempts to retrieve charging point "AC_1" return "not found"

    Given a charging point "AC_1" exists at charging station "Station_A" in location "Deutschwagram"
      And the charging point mode is "unknown"
    When I set the charging point mode to "AC" for "AC_1"
    Then the charging point "AC_1" has mode "AC"
      And the charging point mode is saved successfully
