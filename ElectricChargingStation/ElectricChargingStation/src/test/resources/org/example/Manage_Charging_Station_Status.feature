Feature: Manage Charging Station Status
  As an Owner
  I want to view charging stations and their points per location
  so that I can check availability and find out-of-order devices

  Scenario: Read Charging Station
    Given I am logged in as owner
      And the following locations and charging stations exist:
        | location       | charging station |
        | Hauptplatz     | Station_1        |
        | Hauptplatz     | Station_2        |
        | Stephansplatz  | Station_A        |
    When I select the location "Hauptplatz"
    Then I see a list of charging stations assigned to "Hauptplatz":
      | charging station |
      | Station_1        |
      | Station_2        |
    And the charging station list is displayed correctly

  Scenario: Read Charging Point Status
    Given I am logged in as owner
      And a location "Hauptplatz" exists
      And a charging station "Station_1" exists at location "Hauptplatz"
      And the following charging points exist for "Station_1":
        | charging point | status        |
        | AC_1           | available     |
        | AC_2           | occupied      |
        | DC_1           | out_of_order  |
    When I select charging station "Station_1" at location "Hauptplatz"
    Then I see the status of each charging point:
      | charging point | status       |
      | AC_1           | available    |
      | AC_2           | occupied     |
      | DC_1           | out_of_order |
    And each charging point status is displayed with a human-readable label (available, occupied, out of order)
    And the UI shows appropriate visual indicators for each status (e.g., green/yellow/red)
