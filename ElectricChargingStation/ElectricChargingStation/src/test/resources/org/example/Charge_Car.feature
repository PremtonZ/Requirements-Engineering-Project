Feature: Charge Car
    As a customer,
    I want to manage the charging cycle
    so that I can charge my vehicle the way I want.

    Background:
        Given I am logged in as customer
        And a charging station with name "Stephansplatz" exists for charging
        And a charging point with name "AC_1" exists at charging station "Stephansplatz" for charging
        And the charging point with name "AC_1" has the status "in Betrieb frei"

    Scenario: Choose Charging Point
        When I go to the charging station with the name "Stephansplatz"
        Then I see all the charging points
        And I select the charging point "AC_1"
        And I see the details of the charging point:
            | location                  | Wien          |
            | charging station          | Stephansplatz |
            | charging point            | AC_1          |
            | charging mode             | AC            |
            | price kWh                 | 5.83 EUR      |
        And a button that says "Ladevorgang starten"

    Scenario: Start Charging Process
        Given I am in the details view of the charging point "AC_1"
        When I click on the button "Ladevorgang starten"
        Then I see a confirmation that charging can start now
        And I get a notification
        And the details view starts the timer
        And it shows the price when starting the charging
        And it shows the calculated total price
        And it shows the button "Ladevorgang beenden"

    Scenario: Stop Charging Process
        Given I am in the details view of the charging point "AC_1"
        And I have started the charging cycle
        When I click on the button "Ladevorgang beenden"
        Then I see a confirmation that charging has stopped
        And I get a notification that the charging process has ended
        And it creates an invoice containing a list of invoice items sorted by start time, including:
            | invoice item number        | 42                                |
            | start time                 | 2025-11-26T14:10:00               |
            | location name              | Stephansplatz                     |
            | charging point             | AC_1                              |
            | charging mode              | AC                                |
            | duration of use            | 27 minutes                        |
            | loaded energy (kWh)        | 12.4 kWh                          |
            | price                      | 5.83 EUR                          |
            | customer identity          | CUST-00127                        |
            | money top-ups              | 50 EUR                            |
            | outstanding balance status | 44.17 EUR remaining               |
        And I get an invoice via mail
        And the invoice gets added to my charging history

    Scenario: Show Charging Process Infos
        Given I have completed the charging cycle
        And I clicked on the button "Ladevorgang beenden"
        And I saw a confirmation that charging has stopped
        Then I get a notification that the charging process has ended
        And it shows a charging details list of following items:
            | start time                 | 2025-11-26T14:10:00               |
            | location name              | Stephansplatz                     |
            | charging point             | AC_1                              |
            | duration of use            | 27 minutes                        |
            | loaded energy (kWh)        | 12.4 kWh                          |
            | price                      | 5.83 EUR                          |