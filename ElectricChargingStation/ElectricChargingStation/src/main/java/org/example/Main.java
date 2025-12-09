package org.example;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ECSManager network = new ECSManager();
        LocalDate now = LocalDate.now();

        System.out.println("=== Electric Charging Station System Demo ===\n");

        // === Manage Infrastructure ===
        System.out.println("--- Manage Infrastructure ---");
        Site site1 = network.createSite("Deutschwagram");
        Site site2 = network.createSite("Wien");
        System.out.println("Sites: " + site1.getLocation() + ", " + site2.getLocation());

        // Create Charging Station first
        ChargingStation station1 = network.createChargingStation("Station_A", "Deutschwagram", "outdoor");
        System.out.println("Charging Station: " + station1.getName() + " at " + station1.getSite().getLocation());

        // Create Chargers at Charging Station
        Charger charger1 = network.createCharger("AC_1", "Deutschwagram", "Station_A", "AC", "available");
        Charger charger2 = network.createCharger("DC_1", "Deutschwagram", "Station_A", "DC", "available");
        System.out.println("Chargers: " + charger1.getName() + " (" + charger1.getMode() + "), " + charger2.getName() + " (" + charger2.getMode() + ")");

        Charger ac1 = network.getCharger("Deutschwagram", "Station_A", "AC_1");
        ac1.setMode("AC");
        System.out.println("Charger type: " + ac1.getName() + " = " + ac1.getMode());

        network.setSitePrices("Deutschwagram", 0.42, 1.0, 0.55, 3.0);
        Site site = network.getSite("Deutschwagram");
        System.out.println("Prices: AC " + site.getACKwh() + "/kWh, DC " + site.getDCKwh() + "/kWh");

        Site selected = network.getSite("Deutschwagram");
        System.out.println("Selected: " + selected.getLocation() + " (" + network.getChargerCountAtSite("Deutschwagram") + " chargers)");

        Charger ac = network.getCharger("Deutschwagram", "Station_A", "AC_1");
        Charger dc = network.getCharger("Deutschwagram", "Station_A", "DC_1");
        System.out.println("Status: " + ac.getName() + " = " + ac.getMode() + ", " + ac.getState());
        System.out.println("Status: " + dc.getName() + " = " + dc.getMode() + ", " + dc.getState());

        // === Manage Customer Account ===
        System.out.println("\n--- Manage Customer Account ---");
        Account account1 = network.createAccount("Max");
        Account account2 = network.createAccount("Philipp");
        System.out.println("Accounts: " + account1.getUsername() + " (ID:" + account1.getCustomerId() + "), " + account2.getUsername() + " (ID:" + account2.getCustomerId() + ")");

        Account max = network.getAccount("Max");
        System.out.println("Account: " + max.getUsername() + ", Credits: " + max.getCredit());

        network.addCredit("Max", 200.0, now.getDayOfMonth(), now.getMonthValue(), now.getYear());
        Account maxAfter = network.getAccount("Max");
        System.out.println("Credit: " + maxAfter.getCredit());

        Site location = network.getSite("Deutschwagram");
        System.out.println("Location " + location.getLocation() + ": AC " + location.getACKwh() + "/kWh, " + location.getACPpm() + "/min");
        System.out.println("Location " + location.getLocation() + ": DC " + location.getDCKwh() + "/kWh, " + location.getDCPpm() + "/min");

        System.out.println("\nUpdate Account: Max -> MaxMustermann");
        network.updateAccountUsername("Max", "MaxMustermann");
        System.out.println("Updated: " + network.getAccount("MaxMustermann").getUsername());

        System.out.println("\nDelete Account: TestUser");
        network.createAccount("TestUser");
        System.out.println("Total accounts: " + network.getAccountCount());
        network.deleteAccount("TestUser");
        System.out.println("Deleted. Total accounts: " + network.getAccountCount());

        // === Admin Manage Credits ===
        System.out.println("\n--- Admin Manage Credits ---");
        network.createAccount("CreditUser");
        network.addCredit("CreditUser", 150.0, now.getDayOfMonth(), now.getMonthValue(), now.getYear());
        System.out.println("Show Balance: CreditUser = " + network.getAccount("CreditUser").getCredit() + " credits");

        network.addCredit("CreditUser", 50.0, now.getDayOfMonth(), now.getMonthValue(), now.getYear());
        System.out.println("Top-Up: +50.0 credits (150.0 -> " + network.getAccount("CreditUser").getCredit() + ")");

        System.out.println("Balance History: " + network.getAccount("CreditUser").getInvoiceItems().size() + " transactions");
        network.printInvoiceHistory("CreditUser");

        // === Manage Infrastructure CRUD Operations ===
        System.out.println("\n--- Manage Infrastructure CRUD ---");

        // Update Location
        System.out.println("\nUpdate Location: Deutschwagram -> Deutschwagram_New");
        network.updateSiteLocation("Deutschwagram", "Deutschwagram_New");
        System.out.println("Updated: " + network.getSite("Deutschwagram_New").getLocation());

        // Update Charging Station
        System.out.println("\nUpdate Charging Station: Station_A -> Station_A_Updated");
        network.updateChargingStationName("Deutschwagram_New", "Station_A", "Station_A_Updated");
        System.out.println("Updated: " + network.getChargingStation("Deutschwagram_New", "Station_A_Updated").getName());

        // Update Charging Point
        System.out.println("\nUpdate Charging Point: AC_1 -> AC_1_Updated");
        network.updateChargerName("Deutschwagram_New", "Station_A_Updated", "AC_1", "AC_1_Updated");
        System.out.println("Updated name: " + network.getCharger("Deutschwagram_New", "Station_A_Updated", "AC_1_Updated").getName());

        network.updateChargerMode("Deutschwagram_New", "Station_A_Updated", "AC_1_Updated", "DC");
        System.out.println("Updated mode: " + network.getCharger("Deutschwagram_New", "Station_A_Updated", "AC_1_Updated").getMode());

        // Delete Charging Point
        System.out.println("\nDelete Charging Point: DC_1");
        network.deleteCharger("Deutschwagram_New", "Station_A_Updated", "DC_1");
        System.out.println("Deleted. Remaining chargers: " + network.getChargingStation("Deutschwagram_New", "Station_A_Updated").getChargers().size());

        // Delete Charging Station
        System.out.println("\nDelete Charging Station: Station_A_Updated");
        network.deleteChargingStation("Deutschwagram_New", "Station_A_Updated");
        System.out.println("Deleted. Remaining stations: " + network.getSite("Deutschwagram_New").getChargingStations().size());

        // Delete Location
        System.out.println("\nDelete Location: Wien");
        network.deleteSite("Wien");
        System.out.println("Deleted. Total sites: " + network.getSiteCount());

        // === Manage Charging Station Status ===
        System.out.println("\n--- Manage Charging Station Status ---");

        // Setup: Create locations and charging stations
        network.createSite("Hauptplatz");
        network.createSite("Stephansplatz");
        network.createChargingStation("Station_1", "Hauptplatz", "outdoor");
        network.createChargingStation("Station_2", "Hauptplatz", "indoor");
        network.createChargingStation("Station_A", "Stephansplatz", "outdoor");

        // Read Charging Station: Show list of stations at a location
        System.out.println("\nRead Charging Station at Hauptplatz:");
        network.printChargingStationsAtLocation("Hauptplatz");

        // Read Charging Point Status: Show status of points at a station
        System.out.println("\nRead Charging Point Status at Station_1:");
        network.createCharger("AC_1", "Hauptplatz", "Station_1", "AC", "available");
        network.createCharger("AC_2", "Hauptplatz", "Station_1", "AC", "occupied");
        network.createCharger("DC_1", "Hauptplatz", "Station_1", "DC", "out of order");
        network.printChargingPointStatusAtStation("Hauptplatz", "Station_1");

        System.out.println("\n=== Demo Complete ===");
    }
}