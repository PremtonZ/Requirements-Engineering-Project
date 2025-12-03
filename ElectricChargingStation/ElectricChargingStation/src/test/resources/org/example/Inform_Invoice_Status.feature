Feature: Inform Invoice Status
    As a customer
    I want to review my invoices
    so that I can view, download and forward them.

    Background:
        Given I am logged in as customer
        And I am in the invoice tab


    Scenario: Rechnungsdetails anzeigen
        When I click on an invoice in the invoice history
        Then I see the details of the invoice:
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


    Scenario: Rechnungen herunterladen
        When I click on an invoice in the invoice history
        Then the app shows me the download button in the top right corner
        And I click on the download button
        Then the app calls the operating-system-specific download function with the invoice as a PDF
        And it offers me various options to download or forward the invoice
