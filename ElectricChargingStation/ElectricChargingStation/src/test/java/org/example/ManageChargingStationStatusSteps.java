package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManageChargingStationStatusSteps {
    private Site selectedLocation;
    private ChargingStation selectedChargingStation;
    private List<ChargingStation> displayedStations;
    private List<Charger> displayedChargers;

    @Given("the following locations and charging stations exist:")
    public void theFollowingLocationsAndChargingStationsExist(DataTable dataTable) {
        for (int i = 1; i < dataTable.height(); i++) {
            String location = dataTable.cell(i, 0);
            String chargingStation = dataTable.cell(i, 1);

            try {
                TestContext.network.createSite(location);
            } catch (IllegalArgumentException e) {
                // Location already exists
            }

            try {
                TestContext.network.createChargingStation(chargingStation, location, "outdoor");
            } catch (IllegalArgumentException e) {
                // Charging station already exists
            }
        }
    }

    @When("I select the location {string}")
    public void iSelectTheLocation(String locationName) {
        selectedLocation = TestContext.network.getSite(locationName);
    }

    @Then("I see a list of charging stations assigned to {string}:")
    public void iSeeAListOfChargingStationsAssignedTo(String locationName, DataTable dataTable) {
        assertNotNull(selectedLocation, "Location should be selected");
        assertEquals(locationName, selectedLocation.getLocation(), "Selected location should match");

        displayedStations = TestContext.network.getChargingStationsAtLocation(locationName);
        List<String> expectedStations = new ArrayList<>();
        for (int i = 1; i < dataTable.height(); i++) {
            expectedStations.add(dataTable.cell(i, 0));
        }

        assertEquals(expectedStations.size(), displayedStations.size(),
                "Number of charging stations should match");

        for (String expectedStation : expectedStations) {
            boolean found = false;
            for (ChargingStation station : displayedStations) {
                if (station.getName().equals(expectedStation)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Charging station should be in list: " + expectedStation);
        }
    }

    @Then("the charging station list is displayed correctly")
    public void theChargingStationListIsDisplayedCorrectly() {
        assertNotNull(displayedStations, "Charging station list should be displayed");
        assertFalse(displayedStations.isEmpty(), "Charging station list should not be empty");
    }

    @Given("the following charging points exist for {string}:")
    public void theFollowingChargingPointsExistFor(String stationName, DataTable dataTable) {
        // Find the location and station
        Site foundSite = null;
        ChargingStation foundStation = null;

        for (Site site : TestContext.network.getSites()) {
            for (ChargingStation station : site.getChargingStations()) {
                if (station.getName().equals(stationName)) {
                    foundSite = site;
                    foundStation = station;
                    break;
                }
            }
            if (foundStation != null) break;
        }

        assertNotNull(foundStation, "Charging station should exist: " + stationName);

        for (int i = 1; i < dataTable.height(); i++) {
            String chargerName = dataTable.cell(i, 0);
            String status = dataTable.cell(i, 1);

            // Convert status format: out_of_order -> out of order
            String state = status.replace("_", " ");

            // Determine mode based on charger name (AC or DC)
            String mode = chargerName.startsWith("AC") ? "AC" : "DC";

            // Check if charger already exists
            Charger existingCharger = null;
            try {
                existingCharger = TestContext.network.getCharger(foundSite.getLocation(), stationName, chargerName);
            } catch (IllegalArgumentException e) {
                // Charger doesn't exist, will create it
            }

            if (existingCharger != null) {
                // Update existing charger - always update state and mode
                existingCharger.setState(state);
                existingCharger.setMode(mode);
            } else {
                // Create new charger
                TestContext.network.createCharger(chargerName, foundSite.getLocation(), stationName, mode, state);
            }
        }
    }

    @When("I select charging station {string} at location {string}")
    public void iSelectChargingStationAtLocation(String stationName, String locationName) {
        selectedChargingStation = TestContext.network.getChargingStation(locationName, stationName);
    }

    @Then("I see the status of each charging point:")
    public void iSeeTheStatusOfEachChargingPoint(DataTable dataTable) {
        assertNotNull(selectedChargingStation, "Charging station should be selected");

        displayedChargers = TestContext.network.getChargersAtStation(
                selectedChargingStation.getSite().getLocation(),
                selectedChargingStation.getName()
        );

        List<String> expectedChargers = new ArrayList<>();
        List<String> expectedStatuses = new ArrayList<>();
        for (int i = 1; i < dataTable.height(); i++) {
            expectedChargers.add(dataTable.cell(i, 0));
            expectedStatuses.add(dataTable.cell(i, 1));
        }

        // Check that we have at least the expected number of chargers
        assertTrue(displayedChargers.size() >= expectedChargers.size(),
                "Number of charging points should be at least " + expectedChargers.size() + " but was " + displayedChargers.size());

        for (int i = 0; i < expectedChargers.size(); i++) {
            String expectedCharger = expectedChargers.get(i);
            // Convert expected status: out_of_order -> out of order
            String expectedStatus = expectedStatuses.get(i).replace("_", " ");

            boolean found = false;
            for (Charger charger : displayedChargers) {
                if (charger.getName().equals(expectedCharger)) {
                    found = true;
                    String actualStatus = charger.getState();
                    assertEquals(expectedStatus, actualStatus,
                            "Charging point status should match for " + expectedCharger);
                    break;
                }
            }
            assertTrue(found, "Charging point should be in list: " + expectedCharger);
        }
    }

    @Then("each charging point status is displayed with a human-readable label \\(available, occupied, out of order)")
    public void eachChargingPointStatusIsDisplayedWithAHumanReadableLabel() {
        assertNotNull(displayedChargers, "Charging points should be displayed");
        for (Charger charger : displayedChargers) {
            String state = charger.getState();
            assertTrue(state.equals("available") || state.equals("occupied") || state.equals("out of order"),
                    "Status should be human-readable: " + state);
        }
    }

    @Then("the UI shows appropriate visual indicators for each status \\(e.g., green\\/yellow\\/red)")
    public void theUIShowsAppropriateVisualIndicatorsForEachStatus() {
        assertNotNull(displayedChargers, "Charging points should be displayed");
        for (Charger charger : displayedChargers) {
            String state = charger.getState();
            // Verify that status can be mapped to visual indicators
            assertTrue(state.equals("available") || state.equals("occupied") || state.equals("out of order"),
                    "Status should be mappable to visual indicator: " + state);
        }
    }
}