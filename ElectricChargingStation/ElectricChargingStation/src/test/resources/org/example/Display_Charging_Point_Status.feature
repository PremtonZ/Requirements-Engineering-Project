Feature: Display Charging Point Status
  As a Customer,
  I want to view charging point status and prices,
  so that I can find and use charging points

  Scenario: Standort anzeigen
    Given I am logged in as customer "Max"
    And the following locations exist:
      | Deutschwagram |
      | Wien          |
    And location "Deutschwagram" has the following prices:
      | AC Kwh | AC Ppm | DC Kwh | DC Ppm |
      | 0.42   | 1.0    | 0.55   | 3.0    |
    And the following charging points exist:
      | Name | Location | Mode | State |
      | AC_1 | Deutschwagram | AC | in operation free |
      | DC_1 | Deutschwagram | DC | in operation free |
    When I view the location "Deutschwagram"
    Then I receive the following location information:
      | Location | AC Kwh | AC Ppm | DC Kwh | DC Ppm | Charging Points |
      | Deutschwagram | 0.42 | 1.0 | 0.55 | 3.0 | 2 |
    And the location information is displayed correctly

  Scenario: Ladepunkt Preis anzeigen (AC/DC)
    Given I am logged in as customer "Max"
    And the following locations exist:
      | Deutschwagram |
    And location "Deutschwagram" has the following prices:
      | AC Kwh | AC Ppm | DC Kwh | DC Ppm |
      | 0.42   | 1.0    | 0.55   | 3.0    |
    When I view the AC charging point price at location "Deutschwagram"
    Then I see that AC price per kWh is 0.42
    And I see that AC price per minute is 1.0
    When I view the DC charging point price at location "Deutschwagram"
    Then I see that DC price per kWh is 0.55
    And I see that DC price per minute is 3.0
    And the charging point prices are displayed correctly
