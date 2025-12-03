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
      | AC_1 | Deutschwagram | AC | in operation free |
      | DC_1 | Deutschwagram | DC | in operation free |
    When I display the charging point status for location "Deutschwagram"
    Then I see 2 charging points at location "Deutschwagram"
    And I see charging point "AC_1" with mode "AC" and state "in operation free"
    And I see charging point "DC_1" with mode "DC" and state "in operation free"

  // MVP 2
  Scenario: Read Charging Point
  
  Scenario: Update Charging Point Status Deniz
