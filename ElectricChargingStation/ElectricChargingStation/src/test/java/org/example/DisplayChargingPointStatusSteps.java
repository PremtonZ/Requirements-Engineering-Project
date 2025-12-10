package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class DisplayChargingPointStatusSteps {

    // Show Location
    @When("I view the location {string}")
    public void iViewTheLocation(String locationName) {
        try {
            TestContext.selectedLocation = TestContext.network.getSite(locationName);
        } catch (Exception e) {
            TestContext.selectedLocation = null;
        }
    }

    @Then("I receive the following location information:")
    public void iReceiveTheFollowingLocationInformation(DataTable dataTable) {
        assertNotNull(TestContext.selectedLocation, "Location should be found");
        String expectedLocation = dataTable.cell(1, 0);
        double expectedACKwh = Double.parseDouble(dataTable.cell(1, 1));
        double expectedACPpm = Double.parseDouble(dataTable.cell(1, 2));
        double expectedDCKwh = Double.parseDouble(dataTable.cell(1, 3));
        double expectedDCPpm = Double.parseDouble(dataTable.cell(1, 4));
        int expectedChargingPoints = Integer.parseInt(dataTable.cell(1, 5));

        assertEquals(expectedLocation, TestContext.selectedLocation.getLocation(), "Location name should match");
        assertEquals(expectedACKwh, TestContext.selectedLocation.getACKwh(), 0.001, "AC kWh price should match");
        assertEquals(expectedACPpm, TestContext.selectedLocation.getACPpm(), 0.001, "AC per minute price should match");
        assertEquals(expectedDCKwh, TestContext.selectedLocation.getDCKwh(), 0.001, "DC kWh price should match");
        assertEquals(expectedDCPpm, TestContext.selectedLocation.getDCPpm(), 0.001, "DC per minute price should match");
        assertEquals(expectedChargingPoints,
                TestContext.network.getChargerCountAtSite(TestContext.selectedLocation.getLocation()),
                "Charging point count should match");
    }

    @Then("the location information is displayed correctly")
    public void theLocationInformationIsDisplayedCorrectly() {
        assertNotNull(TestContext.selectedLocation, "Location should be found");
        assertNotNull(TestContext.selectedLocation.getLocation(), "Location name should be set");
    }

    // Read Charging Point Price
    @When("I view the AC charging point price at location {string}")
    public void iViewTheACChargingPointPriceAtLocation(String location) {
        TestContext.selectedLocation = TestContext.network.getSite(location);
    }

    @Then("I see that AC price per kWh is {double}")
    public void iSeeThatACPricePerKWhIs(double expectedPrice) {
        assertNotNull(TestContext.selectedLocation, "Location should be found");
        assertEquals(expectedPrice, TestContext.selectedLocation.getACKwh(), 0.001, "AC price per kWh should match");
    }

    @Then("I see that AC price per minute is {double}")
    public void iSeeThatACPricePerMinuteIs(double expectedPrice) {
        assertNotNull(TestContext.selectedLocation, "Location should be found");
        assertEquals(expectedPrice, TestContext.selectedLocation.getACPpm(), 0.001, "AC price per minute should match");
    }

    @When("I view the DC charging point price at location {string}")
    public void iViewTheDCChargingPointPriceAtLocation(String location) {
        TestContext.selectedLocation = TestContext.network.getSite(location);
    }

    @Then("I see that DC price per kWh is {double}")
    public void iSeeThatDCPricePerKWhIs(double expectedPrice) {
        assertNotNull(TestContext.selectedLocation, "Location should be found");
        assertEquals(expectedPrice, TestContext.selectedLocation.getDCKwh(), 0.001, "DC price per kWh should match");
    }

    @Then("I see that DC price per minute is {double}")
    public void iSeeThatDCPricePerMinuteIs(double expectedPrice) {
        assertNotNull(TestContext.selectedLocation, "Location should be found");
        assertEquals(expectedPrice, TestContext.selectedLocation.getDCPpm(), 0.001, "DC price per minute should match");
    }

    @Then("the charging point prices are displayed correctly")
    public void theChargingPointPricesAreDisplayedCorrectly() {
        assertNotNull(TestContext.selectedLocation, "Location should be found");
        assertTrue(TestContext.selectedLocation.getACKwh() > 0, "AC kWh price should be set");
        assertTrue(TestContext.selectedLocation.getACPpm() > 0, "AC per minute price should be set");
        assertTrue(TestContext.selectedLocation.getDCKwh() > 0, "DC kWh price should be set");
        assertTrue(TestContext.selectedLocation.getDCPpm() > 0, "DC per minute price should be set");
    }

    // Read Charging Point Status
    @When("I open the charging point {string} details")
    public void iOpenTheChargingPointDetails(String chargerName) {
        assertTrue(TestContext.isCustomerLoggedIn, "Customer should be logged in");
        
        // Find the charger by name
        // First try to use selectedLocation if it's set
        if (TestContext.selectedLocation != null) {
            try {
                TestContext.selectedCharger = TestContext.network.getCharger(
                        TestContext.selectedLocation.getLocation(), 
                        chargerName);
            } catch (IllegalArgumentException e) {
                // Try to find it manually at the selected location
                TestContext.selectedCharger = null;
                for (ChargingStation station : TestContext.selectedLocation.getChargingStations()) {
                    for (Charger charger : station.getChargers()) {
                        if (charger.getName().equals(chargerName)) {
                            TestContext.selectedCharger = charger;
                            break;
                        }
                    }
                    if (TestContext.selectedCharger != null) {
                        break;
                    }
                }
            }
        } else {
            // If no location is selected, search across all sites
            TestContext.selectedCharger = null;
            for (Site site : TestContext.network.getSites()) {
                try {
                    TestContext.selectedCharger = TestContext.network.getCharger(
                            site.getLocation(), 
                            chargerName);
                    TestContext.selectedLocation = site;
                    break;
                } catch (IllegalArgumentException e) {
                    // Try to find it manually at this site
                    for (ChargingStation station : site.getChargingStations()) {
                        for (Charger charger : station.getChargers()) {
                            if (charger.getName().equals(chargerName)) {
                                TestContext.selectedCharger = charger;
                                TestContext.selectedLocation = site;
                                break;
                            }
                        }
                        if (TestContext.selectedCharger != null) {
                            break;
                        }
                    }
                    if (TestContext.selectedCharger != null) {
                        break;
                    }
                }
            }
        }
        
        assertNotNull(TestContext.selectedCharger, "Charging point " + chargerName + " should exist");
    }

    @Then("I see the charging point status being {string}")
    public void iSeeTheChargingPointStatusBeing(String expectedStatus) {
        assertNotNull(TestContext.selectedCharger, "Charging point should be selected");
        assertEquals(expectedStatus, TestContext.selectedCharger.getState(), 
                "Charging point status should match");
    }
}
