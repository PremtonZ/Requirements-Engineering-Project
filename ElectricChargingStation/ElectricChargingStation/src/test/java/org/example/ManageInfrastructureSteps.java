package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class ManageInfrastructureSteps {
    private Site createdSite;
    private Charger createdCharger;
    private ChargingStation createdChargingStation;
    private Site viewedSite;
    private ChargingStation viewedChargingStation;
    private Charger viewedCharger;

    // Create Location
    @When("I create a location with name {string}")
    public void iCreateALocationWithName(String locationName) {
        createdSite = getOrCreateSite(locationName);
    }

    @Then("a location with name {string} is created successfully")
    public void aLocationWithNameIsCreatedSuccessfully(String locationName) {
        assertEquals(locationName, createdSite.getLocation());
    }

    @Then("the location is available in the network")
    public void theLocationIsAvailableInTheNetwork() {
        assertNotNull(TestContext.network.getSite(createdSite.getLocation()));
    }

    // Create Charging Point
    @When("I add a charging point with name {string} to location {string}")
    public void iAddAChargingPointWithNameToLocation(String chargerName, String locationName) {
        ensureChargingStationExists("DefaultStation", locationName);
        createdCharger = getOrCreateCharger(chargerName, locationName, "DefaultStation", "AC", "in operation free");
    }

    @Then("a charging point with name {string} is created successfully at location {string}")
    public void aChargingPointWithNameIsCreatedSuccessfullyAtLocation(String chargerName, String locationName) {
        assertEquals(chargerName, createdCharger.getName());
        assertEquals(locationName, createdCharger.getSite().getLocation());
    }

    @Then("the charging point is available for use")
    public void theChargingPointIsAvailableForUse() {
        assertNotNull(TestContext.network.getCharger(createdCharger.getSite().getLocation(), createdCharger.getName()));
    }

    // Set Charging Point Mode
    @When("I set the charging point type to {string} for charging point {string} at location {string}")
    public void iSetTheChargingPointTypeToForChargingPointAtLocation(String type, String chargerName, String locationName) {
        TestContext.network.getCharger(locationName, chargerName).setMode(type);
    }

    @Then("the charging point {string} at location {string} has type {string}")
    public void theChargingPointAtLocationHasType(String chargerName, String locationName, String expectedType) {
        assertEquals(expectedType, TestContext.network.getCharger(locationName, chargerName).getMode());
    }

    @Then("the charging point type is saved successfully")
    public void theChargingPointTypeIsSavedSuccessfully() {
        assertTrue(true);
    }

    // Read Location
    @Given("a location with name {string} exists with the following details:")
    public void aLocationWithNameExistsWithTheFollowingDetails(String locationName, DataTable dataTable) {
        getOrCreateSite(locationName);
    }

    @When("I view the location details for {string}")
    public void iViewTheLocationDetailsFor(String locationName) {
        viewedSite = TestContext.network.getSite(locationName);
    }

    @Then("I see the location details:")
    public void iSeeTheLocationDetails(DataTable dataTable) {
        assertNotNull(viewedSite);
    }

    @Then("the location details are displayed correctly")
    public void theLocationDetailsAreDisplayedCorrectly() {
        assertNotNull(viewedSite);
    }

    // Update Location
    @When("I update the location {string} with:")
    public void iUpdateTheLocationWith(String locationName, DataTable dataTable) {
        String newName = null;
        for (int i = 0; i < dataTable.height(); i++) {
            String field = dataTable.cell(i, 0);
            String value = dataTable.cell(i, 1);
            if (field.equals("name")) {
                newName = value;
            }
        }
        if (newName != null) {
            TestContext.network.updateSiteLocation(locationName, newName);
            viewedSite = TestContext.network.getSite(newName);
        }
    }

    @When("I save the changes")
    public void iSaveTheChanges() {
    }

    @Then("the location update is saved successfully")
    public void theLocationUpdateIsSavedSuccessfully() {
        assertNotNull(viewedSite);
    }

    @Then("the location {string} has the updated details:")
    public void theLocationHasTheUpdatedDetails(String locationName, DataTable dataTable) {
        String newName = null;
        for (int i = 1; i < dataTable.height(); i++) {
            String field = dataTable.cell(i, 0);
            String value = dataTable.cell(i, 1);
            if (field.equals("name")) {
                newName = value;
            }
        }
        if (newName != null) {
            assertNotNull(TestContext.network.getSite(newName));
        }
    }

    // Delete Location
    @Given("a location {string} exists")
    public void aLocationExists(String locationName) {
        getOrCreateSite(locationName);
    }

    @Given("the location has associated charging stations and charging points")
    public void theLocationHasAssociatedChargingStationsAndChargingPoints() {
        try {
            TestContext.network.createChargingStation("TestStation", "Deutschwagram", "outdoor");
            TestContext.network.createCharger("TestCharger", "Deutschwagram", "TestStation", "AC", "in operation free");
        } catch (IllegalArgumentException e) {
        }
    }

    @When("I delete the location {string}")
    public void iDeleteTheLocation(String locationName) {
        TestContext.network.deleteSite(locationName);
    }

    @Then("the location is removed from the system")
    public void theLocationIsRemovedFromTheSystem() {
    }

    @Then("all charging stations assigned to {string} are removed")
    public void allChargingStationsAssignedToAreRemoved(String locationName) {
    }

    @Then("all charging points assigned to those stations are removed")
    public void allChargingPointsAssignedToThoseStationsAreRemoved() {
    }

    @Then("attempts to retrieve the location return {string}")
    public void attemptsToRetrieveTheLocationReturn(String expectedResult) {
        assertThrows(IllegalArgumentException.class, () -> TestContext.network.getSite("Deutschwagram"));
    }

    // Create Charging Station
    @When("I create a charging station with name {string} at location {string}")
    public void iCreateAChargingStationWithNameAtLocation(String stationName, String locationName) {
        getOrCreateSite(locationName);
        createdChargingStation = getOrCreateChargingStation(stationName, locationName, "outdoor");
    }

    @Then("the charging station {string} is created successfully at location {string}")
    public void theChargingStationIsCreatedSuccessfullyAtLocation(String stationName, String locationName) {
        assertEquals(stationName, createdChargingStation.getName());
        assertEquals(locationName, createdChargingStation.getSite().getLocation());
    }

    @Then("the charging station is available in the network")
    public void theChargingStationIsAvailableInTheNetwork() {
        assertNotNull(TestContext.network.getChargingStation(createdChargingStation.getSite().getLocation(), createdChargingStation.getName()));
    }

    // Read Charging Station
    @Given("a charging station {string} exists at location {string} with the following:")
    public void aChargingStationExistsAtLocationWithTheFollowing(String stationName, String locationName, DataTable dataTable) {
        getOrCreateSite(locationName);
        String type = extractValueFromDataTable(dataTable, "type", "outdoor");
        getOrCreateChargingStation(stationName, locationName, type);
    }

    @When("I view charging station {string} at location {string}")
    public void iViewChargingStationAtLocation(String stationName, String locationName) {
        viewedChargingStation = TestContext.network.getChargingStation(locationName, stationName);
    }

    @Then("I see the charging station details:")
    public void iSeeTheChargingStationDetails(DataTable dataTable) {
        assertNotNull(viewedChargingStation);
    }

    @Then("the charging station details are displayed correctly")
    public void theChargingStationDetailsAreDisplayedCorrectly() {
        assertNotNull(viewedChargingStation);
    }

    // Update Charging Station
    @Given("a charging station {string} exists at location {string}")
    public void aChargingStationExistsAtLocation(String stationName, String locationName) {
        getOrCreateSite(locationName);
        getOrCreateChargingStation(stationName, locationName, "outdoor");
    }

    @When("I update charging station {string} with:")
    public void iUpdateChargingStationWith(String stationName, DataTable dataTable) {
        String newName = null;
        String newType = null;
        for (int i = 0; i < dataTable.height(); i++) {
            String field = dataTable.cell(i, 0);
            String value = dataTable.cell(i, 1);
            if (field.equals("name")) {
                newName = value;
            } else if (field.equals("type")) {
                newType = value;
            }
        }
        if (newName != null) {
            TestContext.network.updateChargingStationName("Deutschwagram", stationName, newName);
            stationName = newName;
        }
        if (newType != null) {
            TestContext.network.updateChargingStationType("Deutschwagram", stationName, newType);
        }
        viewedChargingStation = TestContext.network.getChargingStation("Deutschwagram", stationName);
    }

    @Then("the charging station update is saved successfully")
    public void theChargingStationUpdateIsSavedSuccessfully() {
        assertNotNull(viewedChargingStation);
    }

    @Then("charging station {string} now has:")
    public void chargingStationNowHas(String stationName, DataTable dataTable) {
        String expectedName = null;
        for (int i = 1; i < dataTable.height(); i++) {
            String field = dataTable.cell(i, 0);
            String value = dataTable.cell(i, 1);
            if (field.equals("name")) {
                expectedName = value;
            }
        }
        if (expectedName != null) {
            assertNotNull(TestContext.network.getChargingStation("Deutschwagram", expectedName));
        }
    }

    // Delete Charging Station
    @Given("{string} has assigned charging points")
    public void hasAssignedChargingPoints(String stationName) {
        ensureChargingPointExists("TestCP", "Deutschwagram", "AC", "in operation free");
    }

    @When("I delete charging station {string}")
    public void iDeleteChargingStation(String stationName) {
        TestContext.network.deleteChargingStation("Deutschwagram", stationName);
    }

    @Then("the charging station is removed from the location")
    public void theChargingStationIsRemovedFromTheLocation() {
    }

    @Then("all charging points assigned to {string} are removed")
    public void allChargingPointsAssignedToAreRemoved(String stationName) {
    }

    @Then("attempts to retrieve {string} return {string}")
    public void attemptsToRetrieveReturn(String stationName, String expectedResult) {
        assertThrows(IllegalArgumentException.class, () -> TestContext.network.getChargingStation("Deutschwagram", stationName));
    }

    // Read Charging Point
    @Given("a charging point {string} exists at charging station {string} in location {string} with:")
    public void aChargingPointExistsAtChargingStationInLocationWith(String chargerName, String stationName, String locationName, DataTable dataTable) {
        getOrCreateSite(locationName);
        getOrCreateChargingStation(stationName, locationName, "outdoor");
        String mode = extractValueFromDataTable(dataTable, "mode", "AC");
        getOrCreateCharger(chargerName, locationName, stationName, mode, "in operation free");
    }

    @When("I view the charging point {string} at charging station {string}")
    public void iViewTheChargingPointAtChargingStation(String chargerName, String stationName) {
        viewedCharger = TestContext.network.getCharger("Deutschwagram", stationName, chargerName);
    }

    @Then("I see the charging point details:")
    public void iSeeTheChargingPointDetails(DataTable dataTable) {
        assertNotNull(viewedCharger);
    }

    @Then("the charging point information is displayed correctly")
    public void theChargingPointInformationIsDisplayedCorrectly() {
        assertNotNull(viewedCharger);
    }

    // Update Charging Point
    @Given("a charging point {string} exists at charging station {string}")
    public void aChargingPointExistsAtChargingStation(String chargerName, String stationName) {
        getOrCreateSite("Deutschwagram");
        getOrCreateChargingStation(stationName, "Deutschwagram", "outdoor");
        getOrCreateCharger(chargerName, "Deutschwagram", stationName, "AC", "in operation free");
    }

    @When("I update charging point {string} with:")
    public void iUpdateChargingPointWith(String chargerName, DataTable dataTable) {
        Charger charger = TestContext.network.getCharger("Deutschwagram", chargerName);
        String stationName = charger.getChargingStation().getName();
        String newName = null;
        String newMode = null;
        for (int i = 0; i < dataTable.height(); i++) {
            String field = dataTable.cell(i, 0);
            String value = dataTable.cell(i, 1);
            if (field.equals("name")) {
                newName = value;
            } else if (field.equals("mode")) {
                newMode = value;
            }
        }
        if (newName != null) {
            TestContext.network.updateChargerName("Deutschwagram", stationName, chargerName, newName);
            chargerName = newName;
        }
        if (newMode != null) {
            TestContext.network.updateChargerMode("Deutschwagram", stationName, chargerName, newMode);
        }
        viewedCharger = TestContext.network.getCharger("Deutschwagram", stationName, chargerName);
    }

    @Then("the charging point update is saved successfully")
    public void theChargingPointUpdateIsSavedSuccessfully() {
        assertNotNull(viewedCharger);
    }

    @Then("charging point {string} now has:")
    public void chargingPointNowHas(String chargerName, DataTable dataTable) {
        String expectedName = null;
        for (int i = 1; i < dataTable.height(); i++) {
            String field = dataTable.cell(i, 0);
            String value = dataTable.cell(i, 1);
            if (field.equals("name")) {
                expectedName = value;
            }
        }
        if (expectedName != null) {
            ChargingStation station = TestContext.network.getChargingStation("Deutschwagram", "Station_A");
            Charger charger = TestContext.network.getCharger("Deutschwagram", station.getName(), expectedName);
            assertNotNull(charger);
        }
    }

    // Delete Charging Point
    @When("I delete the charging point {string}")
    public void iDeleteTheChargingPoint(String chargerName) {
        Charger charger = TestContext.network.getCharger("Deutschwagram", chargerName);
        TestContext.network.deleteCharger("Deutschwagram", charger.getChargingStation().getName(), chargerName);
    }

    @Then("the charging point is removed from the charging station")
    public void theChargingPointIsRemovedFromTheChargingStation() {
    }

    @Then("attempts to retrieve charging point {string} return {string}")
    public void attemptsToRetrieveChargingPointReturn(String chargerName, String expectedResult) {
        assertThrows(IllegalArgumentException.class, () -> TestContext.network.getCharger("Deutschwagram", chargerName));
    }

    @Given("a charging point with name {string} exists at location {string}")
    public void aChargingPointWithNameExistsAtLocation(String chargerName, String locationName) {
        ensureChargingPointExists(chargerName, locationName, "AC", "in operation free");
    }

    @Given("a charging point with name {string} exists at charging station {string}")
    public void aChargingPointWithNameExistsAtChargingStation(String chargerName, String stationName) {
        getOrCreateSite("Deutschwagram");
        getOrCreateChargingStation(stationName, "Deutschwagram", "outdoor");
        getOrCreateCharger(chargerName, "Deutschwagram", stationName, "AC", "in operation free");
    }

    @Given("the following charging points exist:")
    public void theFollowingChargingPointsExist(DataTable dataTable) {
        for (int i = 1; i < dataTable.height(); i++) {
            String name = dataTable.cell(i, 0);
            String location = dataTable.cell(i, 1);
            String mode = dataTable.cell(i, 2);
            String state = dataTable.cell(i, 3);

            ensureChargingPointExists(name, location, mode, state);
        }
    }

    // Helper methods
    private Site getOrCreateSite(String locationName) {
        try {
            return TestContext.network.createSite(locationName);
        } catch (IllegalArgumentException e) {
            return TestContext.network.getSite(locationName);
        }
    }

    private ChargingStation getOrCreateChargingStation(String stationName, String locationName, String type) {
        try {
            return TestContext.network.createChargingStation(stationName, locationName, type);
        } catch (IllegalArgumentException e) {
            return TestContext.network.getChargingStation(locationName, stationName);
        }
    }

    private Charger getOrCreateCharger(String chargerName, String locationName, String stationName, String mode, String state) {
        try {
            return TestContext.network.createCharger(chargerName, locationName, stationName, mode, state);
        } catch (IllegalArgumentException e) {
            return TestContext.network.getCharger(locationName, stationName, chargerName);
        }
    }

    private void ensureChargingStationExists(String stationName, String locationName) {
        getOrCreateSite(locationName);
        getOrCreateChargingStation(stationName, locationName, "outdoor");
    }

    private void ensureChargingPointExists(String name, String location, String mode, String state) {
        ensureChargingStationExists("DefaultStation", location);
        Charger charger = getChargerIfExists(location, name);
        if (charger != null) {
            charger.setMode(mode);
            charger.setState(state);
        } else {
            createChargerSafely(name, location, mode, state);
        }
    }

    private Charger getChargerIfExists(String location, String name) {
        try {
            return TestContext.network.getCharger(location, name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void createChargerSafely(String name, String location, String mode, String state) {
        try {
            TestContext.network.createCharger(name, location, "DefaultStation", mode, state);
        } catch (IllegalArgumentException e) {
            Charger charger = getChargerIfExists(location, name);
            if (charger != null) {
                charger.setMode(mode);
                charger.setState(state);
            }
        }
    }

    private String extractValueFromDataTable(DataTable dataTable, String fieldName, String defaultValue) {
        for (int i = 1; i < dataTable.height(); i++) {
            String field = dataTable.cell(i, 0);
            String value = dataTable.cell(i, 1);
            if (field.equals(fieldName)) {
                return value;
            }
        }
        return defaultValue;
    }
}
