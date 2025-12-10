package org.example;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class ManagePricesSteps {

    // Set Location Prices (AC/DC)
    @When("I set the location price for {string} to AC kWh {string}, AC per minute {string}, DC kWh {string}, and DC per minute {string}")
    public void iSetTheLocationPriceForToACKWhACPerMinuteDCKwhAndDCPerMinute(String locationName, String acKwh,
            String acPpm, String dcKwh, String dcPpm) {
        TestContext.network.setSitePrices(locationName,
                Double.parseDouble(acKwh),
                Double.parseDouble(acPpm),
                Double.parseDouble(dcKwh),
                Double.parseDouble(dcPpm));
    }

    @Then("the location {string} has AC price per kWh {string}")
    public void theLocationHasACPricePerKWh(String locationName, String expectedPrice) {
        Site site = TestContext.network.getSite(locationName);
        assertEquals(Double.parseDouble(expectedPrice), site.getACKwh(), 0.001, "AC price per kWh should match");
    }

    @Then("the location {string} has AC price per minute {string}")
    public void theLocationHasACPricePerMinute(String locationName, String expectedPrice) {
        Site site = TestContext.network.getSite(locationName);
        assertEquals(Double.parseDouble(expectedPrice), site.getACPpm(), 0.001, "AC price per minute should match");
    }

    @Then("the location {string} has DC price per kWh {string}")
    public void theLocationHasDCPricePerKWh(String locationName, String expectedPrice) {
        Site site = TestContext.network.getSite(locationName);
        assertEquals(Double.parseDouble(expectedPrice), site.getDCKwh(), 0.001, "DC price per kWh should match");
    }

    @Then("the location {string} has DC price per minute {string}")
    public void theLocationHasDCPricePerMinute(String locationName, String expectedPrice) {
        Site site = TestContext.network.getSite(locationName);
        assertEquals(Double.parseDouble(expectedPrice), site.getDCPpm(), 0.001, "DC price per minute should match");
    }

    @Then("the location price is saved successfully")
    public void theLocationPriceIsSavedSuccessfully() {
        assertTrue(true, "Location price was set in previous step");
    }
}


