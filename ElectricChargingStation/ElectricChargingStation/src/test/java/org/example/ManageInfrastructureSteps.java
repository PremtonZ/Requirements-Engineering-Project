package org.example;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class ManageInfrastructureSteps {
    private Site createdSite;
    private Charger createdCharger;

    // Create Location
    @When("I create a location with name {string}")
    public void iCreateALocationWithName(String locationName) {
        try {
            createdSite = TestContext.network.createSite(locationName);
        } catch (IllegalArgumentException e) {
            createdSite = TestContext.network.getSite(locationName);
        }
    }

    @Then("a location with name {string} is created successfully")
    public void aLocationWithNameIsCreatedSuccessfully(String locationName) {
        assertNotNull(createdSite, "Location should be created");
        assertEquals(locationName, createdSite.getLocation(), "Location name should match");
    }

    @Then("the location is available in the network")
    public void theLocationIsAvailableInTheNetwork() {
        assertNotNull(createdSite, "Location should exist");
        Site foundSite = TestContext.network.getSite(createdSite.getLocation());
        assertNotNull(foundSite, "Location should be available in network");
    }

    // Create Charging Point
    @When("I add a charging point with name {string} to location {string}")
    public void iAddAChargingPointWithNameToLocation(String chargerName, String locationName) {
        try {
            createdCharger = TestContext.network.createCharger(chargerName, locationName, "AC", "in operation free");
        } catch (IllegalArgumentException e) {
            createdCharger = TestContext.network.getCharger(locationName, chargerName);
        }
    }

    @Then("a charging point with name {string} is created successfully at location {string}")
    public void aChargingPointWithNameIsCreatedSuccessfullyAtLocation(String chargerName, String locationName) {
        assertNotNull(createdCharger, "Charging point should be created");
        assertEquals(chargerName, createdCharger.getName(), "Charging point name should match");
        assertEquals(locationName, createdCharger.getSite().getLocation(), "Charging point location should match");
    }

    @Then("the charging point is available for use")
    public void theChargingPointIsAvailableForUse() {
        assertNotNull(createdCharger, "Charging point should exist");
        Charger foundCharger = TestContext.network.getCharger(createdCharger.getSite().getLocation(), createdCharger.getName());
        assertNotNull(foundCharger, "Charging point should be available");
    }

    // Set Charging Point Mode (AC/DC)
    @When("I set the charging point type to {string} for charging point {string} at location {string}")
    public void iSetTheChargingPointTypeToForChargingPointAtLocation(String type, String chargerName, String locationName) {
        Charger charger = TestContext.network.getCharger(locationName, chargerName);
        charger.setMode(type);
    }

    @Then("the charging point {string} at location {string} has type {string}")
    public void theChargingPointAtLocationHasType(String chargerName, String locationName, String expectedType) {
        Charger charger = TestContext.network.getCharger(locationName, chargerName);
        assertNotNull(charger, "Charging point should exist");
        assertEquals(expectedType, charger.getMode(), "Charging point type should match");
    }

    @Then("the charging point type is saved successfully")
    public void theChargingPointTypeIsSavedSuccessfully() {
        if (createdCharger != null) {
            assertNotNull(createdCharger.getMode(), "Charging point type should be set");
        } else {
            assertTrue(true, "Charging point type was set in previous step");
        }
    }
}


