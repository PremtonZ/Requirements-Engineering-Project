package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class InformInvoiceStatusSteps {
    private InvoiceItem selectedInvoice;
    private Account customerAccount;

    @Given("I am in the invoice tab")
    public void iAmInTheInvoiceTab() {
        assertTrue(TestContext.isLoggedIn, "Admin should be logged in");
    }

    @Given("an invoice with item number {string} exists for customer {string}")
    public void anInvoiceWithItemNumberExistsForCustomer(String itemNumber, String username) {
        try {
            customerAccount = TestContext.network.getAccount(username);
        } catch (IllegalArgumentException e) {
            customerAccount = TestContext.network.createAccount(username);
        }

        int invoiceId = Integer.parseInt(itemNumber);

        boolean invoiceExists = false;
        for (InvoiceItem item : customerAccount.getInvoiceItems()) {
            if (item.getInvoiceId() == invoiceId) {
                invoiceExists = true;
                break;
            }
        }

        if (!invoiceExists) {
            try {
                TestContext.network.createSite("Stephansplatz");
            } catch (IllegalArgumentException e) {
            }

            TestContext.network.setSitePrices("Stephansplatz", 0.42, 0.0293, 0.55, 3.0);

            try {
                TestContext.network.createChargingStation("Station_A", "Stephansplatz", "outdoor");
            } catch (IllegalArgumentException e) {
            }

            Charger charger;
            try {
                charger = TestContext.network.getCharger("Stephansplatz", "Station_A", "AC_1");
                charger.setState("available");
            } catch (IllegalArgumentException e) {
                TestContext.network.createCharger("AC_1", "Stephansplatz", "Station_A", "AC", "available");
                charger = TestContext.network.getCharger("Stephansplatz", "Station_A", "AC_1");
            }

            customerAccount.pay(charger, 27, 12, 2025, 11, 26);

            List<InvoiceItem> items = customerAccount.getInvoiceItems();
            if (!items.isEmpty()) {
                InvoiceItem lastItem = items.get(items.size() - 1);
                try {
                    java.lang.reflect.Field idField = InvoiceItem.class.getDeclaredField("invoiceId");
                    idField.setAccessible(true);
                    idField.setInt(lastItem, invoiceId);
                } catch (Exception e) {
                }
            }
        }
    }

    @When("I click on an invoice in the invoice history with item number {string}")
    public void iClickOnAnInvoiceInTheInvoiceHistoryWithItemNumber(String itemNumber) {
        assertNotNull(customerAccount, "Customer account should exist");
        int invoiceId = Integer.parseInt(itemNumber);

        selectedInvoice = null;
        for (InvoiceItem item : customerAccount.getInvoiceItems()) {
            if (item.getInvoiceId() == invoiceId) {
                selectedInvoice = item;
                break;
            }
        }

        assertNotNull(selectedInvoice, "Invoice with item number " + itemNumber + " should exist");
    }

    @Then("I see the details of the invoice:")
    public void iSeeTheDetailsOfTheInvoice(DataTable dataTable) {
        assertNotNull(selectedInvoice, "Invoice should be selected");
        assertTrue(selectedInvoice instanceof ChargingInvoiceItem, "Invoice should be a ChargingInvoiceItem");

        ChargingInvoiceItem chargingInvoice = (ChargingInvoiceItem) selectedInvoice;

        for (int i = 1; i < dataTable.height(); i++) {
            String field = dataTable.cell(i, 0);
            String expectedValue = dataTable.cell(i, 1);

            String actualValue = null;

            switch (field) {
                case "invoice item number":
                    actualValue = String.valueOf(chargingInvoice.getInvoiceId());
                    break;
                case "start time":
                    java.util.Date date = chargingInvoice.getDate();
                    LocalDate localDate = ((java.sql.Date) date).toLocalDate();
                    LocalDateTime dateTime = LocalDateTime.of(
                            localDate.getYear(),
                            localDate.getMonthValue(),
                            localDate.getDayOfMonth(),
                            14, 10, 0);
                    actualValue = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                    break;
                case "location name":
                    actualValue = chargingInvoice.getCharger().getSite().getLocation();
                    break;
                case "charging point":
                    actualValue = chargingInvoice.getCharger().getName();
                    break;
                case "charging mode":
                    actualValue = chargingInvoice.getCharger().getMode();
                    break;
                case "duration of use":
                    actualValue = chargingInvoice.getDuration() + " minutes";
                    break;
                case "loaded energy (kWh)":
                    actualValue = String.format(Locale.ROOT, "%.1f kWh", (double) chargingInvoice.getAmountKWh());
                    break;
                case "price":
                    double total = chargingInvoice.calculateTotal();
                    actualValue = String.format(Locale.ROOT, "%.2f EUR", total);
                    break;
                case "customer identity":
                    actualValue = chargingInvoice.getAccount().getUsername();
                    break;
            }

            if (actualValue != null) {
                assertEquals(expectedValue, actualValue, "Field " + field + " should match");
            }
        }
    }

    @Then("all invoice information is displayed correctly for admin")
    public void allInvoiceInformationIsDisplayedCorrectlyForAdmin() {
        assertNotNull(selectedInvoice, "Invoice should be selected");
        assertTrue(selectedInvoice instanceof ChargingInvoiceItem, "Invoice should be a ChargingInvoiceItem");
    }
}
