package org.example;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class ManageChargingPointStatusSteps  {

    // Select Charging Point Location
    @When("I select location {string}")
    public void iSelectLocation(String locationName) {
        TestContext.selectedLocation = TestContext.network.getSite(locationName);
    }

    @Then("location {string} is selected")
    public void locationIsSelected(String locationName) {
        assertNotNull(TestContext.selectedLocation, "Location should be selected");
        assertEquals(locationName, TestContext.selectedLocation.getLocation(), "Selected location should match");
    }

    @Then("I can view charging points at location {string}")
    public void iCanViewChargingPointsAtLocation(String locationName) {
        assertNotNull(TestContext.selectedLocation, "Location should be selected");
        Site site = TestContext.network.getSite(locationName);
        assertNotNull(site, "Location should exist");
    }

    // Display Charging Point Status
    @When("I display the charging point status for location {string}")
    public void iDisplayTheChargingPointStatusForLocation(String locationName) {
        TestContext.selectedLocation = TestContext.network.getSite(locationName);
    }

    @Then("I see {int} charging points at location {string}")
    public void iSeeChargingPointsAtLocation(int expectedCount, String locationName) {
        int actualCount = TestContext.network.getChargerCountAtSite(locationName);
        assertEquals(expectedCount, actualCount, "Charging point count should match");
    }

    @Then("I see charging point {string} with mode {string} and state {string}")
    public void iSeeChargingPointWithModeAndState(String chargerName, String expectedMode, String expectedState) {
        assertNotNull(TestContext.selectedLocation, "Location should be selected");
        Charger charger = TestContext.network.getCharger(TestContext.selectedLocation.getLocation(), chargerName);
        assertNotNull(charger, "Charging point should exist");
        assertEquals(expectedMode, charger.getMode(), "Charging point mode should match");
        assertEquals(expectedState, charger.getState(), "Charging point state should match");
    }
}

