Feature: Manage Charging Point Status
  As an Admin,
  I want to manage the charging point status,
  so that I can monitor and control charging points

  Scenario: Ladepunktstandort wählen
    Given I am logged in as admin
    And the following locations exist:
      | Deutschwagram |
      | Wien          |
      | Salzburg      |
    When I select location "Deutschwagram"
    Then location "Deutschwagram" is selected
    And I can view charging points at location "Deutschwagram"

  Scenario: Ladepunktstatus anzeigen
    Given I am logged in as admin
    And a location with name "Deutschwagram" exists
    And the following charging points exist:
      | Name | Location | Mode | State |
      | AC_1 | Deutschwagram | AC | available |
      | DC_1 | Deutschwagram | DC | available |
    When I display the charging point status for location "Deutschwagram"
    Then I see 2 charging points at location "Deutschwagram"
    And I see charging point "AC_1" with mode "AC" and state "available"
    And I see charging point "DC_1" with mode "DC" and state "available"

  Scenario: Read Charging Point
    Given I am logged in as admin
    And a charging point "AC_1" exists at location "Deutschwagram" with mode "AC" and state "available"
    When I view the charging point details for "AC_1" at location "Deutschwagram"
    Then I see the following details:
      | field    | value        |
      | name     | AC_1         |
      | location | Deutschwagram|
      | mode     | AC           |
      | state    | available    |
    And the charging point status is shown as one of:
      | available   |
      | occupied    |
      | out of order|

  Scenario: Update Charging Point Status
    Given I am logged in as admin
    And a charging point "AC_1" exists at location "Deutschwagram" with state "available"
    When I update the charging point status for "AC_1" at location "Deutschwagram" to "out of order"
    And I save the changes
    Then the charging point "AC_1" has status "out of order"
    And the updated status is stored in the system
    And when I view charging point "AC_1" again I see state "out of order"