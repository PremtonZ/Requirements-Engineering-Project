package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ECSManager {
    private static final List<Site> sites = new ArrayList<>();
    private static final List<Account> accounts = new ArrayList<>();

    public Site createSite(String location) {
        if(location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location cannot be null or blank");
        }
        for(Site s : sites) {
            if(s.getLocation().equals(location)) {
                throw new IllegalArgumentException("Site already exists");
            }
        }
        Site site = new Site(location);
        sites.add(site);
        return site;
    }

    public ChargingStation createChargingStation(String name, String siteLocation, String type) {
        Site site = getSite(siteLocation);
        for(ChargingStation cs : site.getChargingStations()) {
            if(cs.getName().equals(name)) {
                throw new IllegalArgumentException("Charging station name already exists at this location");
            }
        }
        ChargingStation chargingStation = new ChargingStation(name, site, type);
        site.addChargingStation(chargingStation);
        return chargingStation;
    }

    public Charger createCharger(String name, String siteLocation, String chargingStationName, String mode, String state) {
        ChargingStation chargingStation = getChargingStation(siteLocation, chargingStationName);
        for(Charger c : chargingStation.getChargers()) {
            if(c.getName().equals(name)) {
                throw new IllegalArgumentException("Charger name already exists at this charging station");
            }
        }
        Charger charger = new Charger(name, chargingStation, mode, state);
        chargingStation.addCharger(charger);
        return charger;
    }

    public Charger createCharger(String name, String siteLocation, String mode, String state) {
        Site site = getSite(siteLocation);
        ChargingStation defaultStation = null;
        for(ChargingStation cs : site.getChargingStations()) {
            if(cs.getName().equals("Default")) {
                defaultStation = cs;
                break;
            }
        }
        if(defaultStation == null) {
            defaultStation = createChargingStation("Default", siteLocation, "outdoor");
        }
        return createCharger(name, siteLocation, "Default", mode, state);
    }

    public Account createAccount(String username) {
        if(username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        for(Account acc : accounts) {
            if(acc.getUsername().equals(username)) {
                throw new IllegalArgumentException("Username already exists");
            }
        }
        int id = getNextCustomerId();
        Account account = new Account(id, username);
        accounts.add(account);
        return account;
    }

    private int getNextCustomerId() {
        if(accounts.isEmpty()) {
            return 0;
        }
        int max = -1;
        for(Account acc : accounts) {
            if(acc.getCustomerId() > max) {
                max = acc.getCustomerId();
            }
        }
        return max + 1;
    }

    public void setSitePrices(String location, double acKwh, double acPpm, double dcKwh, double dcPpm) {
        Site site = getSite(location);
        site.setAllPrices(acKwh, acPpm, dcKwh, dcPpm);
    }

    public void addCredit(String username, double amount, int day, int month, int year) {
        Account account = getAccount(username);
        account.topUp(amount, day, month, year);
    }

    public Site getSite(String siteLocation) {
        for(Site s : sites) {
            if(s.getLocation().equals(siteLocation)) return s;
        }
        throw new IllegalArgumentException("Site not found: " + siteLocation);
    }

    public ChargingStation getChargingStation(String siteLocation, String chargingStationName) {
        Site site = getSite(siteLocation);
        for(ChargingStation cs : site.getChargingStations()) {
            if(cs.getName().equals(chargingStationName)) return cs;
        }
        throw new IllegalArgumentException("Charging station not found: " + chargingStationName + " at " + siteLocation);
    }

    public Charger getCharger(String siteLocation, String chargingStationName, String chargerName) {
        ChargingStation chargingStation = getChargingStation(siteLocation, chargingStationName);
        for(Charger c : chargingStation.getChargers()) {
            if(c.getName().equals(chargerName)) return c;
        }
        throw new IllegalArgumentException("Charger not found: " + chargerName + " at charging station " + chargingStationName + " at " + siteLocation);
    }

    // Legacy method for backward compatibility
    public Charger getCharger(String siteLocation, String chargerName) {
        Site site = getSite(siteLocation);
        for(ChargingStation cs : site.getChargingStations()) {
            for(Charger c : cs.getChargers()) {
                if(c.getName().equals(chargerName)) return c;
            }
        }
        throw new IllegalArgumentException("Charger not found: " + chargerName + " at " + siteLocation);
    }

    public Account getAccount(String username) {
        for(Account acc : accounts) {
            if(acc.getUsername().equals(username)) return acc;
        }
        throw new IllegalArgumentException("Account not found: " + username);
    }

    public int getChargerCountAtSite(String siteLocation) {
        Site site = getSite(siteLocation);
        return site.getChargers().size();
    }

    public int getSiteCount() {
        return sites.size();
    }

    public int getAccountCount() {
        return accounts.size();
    }

    public List<Site> getSites() {
        return sites;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void updateAccountUsername(String oldUsername, String newUsername) {
        if(newUsername == null || newUsername.isBlank()) {
            throw new IllegalArgumentException("New username cannot be null or blank");
        }
        Account account = getAccount(oldUsername);
        for(Account acc : accounts) {
            if(acc.getUsername().equals(newUsername) && !acc.equals(account)) {
                throw new IllegalArgumentException("Username already exists");
            }
        }
        account.setUsername(newUsername);
    }

    public void deleteAccount(String username) {
        Account account = getAccount(username);
        accounts.remove(account);
    }

    public void printInvoiceHistory(String username) {
        Account account = getAccount(username);
        List<InvoiceItem> invoiceItems = new ArrayList<>(account.getInvoiceItems());

        if (invoiceItems.isEmpty()) {
            System.out.println("No invoice history for " + username);
            return;
        }

        // Sort by date (oldest first)
        Collections.sort(invoiceItems, Comparator.comparing(InvoiceItem::getDate));

        System.out.println("Invoice History for " + username + ":");
        System.out.println("Total transactions: " + invoiceItems.size());
        for (InvoiceItem item : invoiceItems) {
            System.out.print("  " + item);
        }
    }

    // Update Location
    public void updateSiteLocation(String oldLocation, String newLocation) {
        if(newLocation == null || newLocation.isBlank()) {
            throw new IllegalArgumentException("New location cannot be null or blank");
        }
        Site site = getSite(oldLocation);
        for(Site s : sites) {
            if(s.getLocation().equals(newLocation) && !s.equals(site)) {
                throw new IllegalArgumentException("Location already exists");
            }
        }
        site.setLocation(newLocation, sites);
    }

    // Delete Location
    public void deleteSite(String location) {
        Site site = getSite(location);
        // Remove all charging stations first (which will remove their chargers)
        List<ChargingStation> stationsToRemove = new ArrayList<>(site.getChargingStations());
        for(ChargingStation cs : stationsToRemove) {
            // Remove all chargers from the station first
            List<Charger> chargersToRemove = new ArrayList<>(cs.getChargers());
            for(Charger charger : chargersToRemove) {
                cs.removeCharger(charger);
            }
            site.removeChargingStation(cs);
        }
        sites.remove(site);
    }

    // Update Charging Station
    public void updateChargingStationName(String siteLocation, String oldName, String newName) {
        if(newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("New charging station name cannot be null or blank");
        }
        ChargingStation chargingStation = getChargingStation(siteLocation, oldName);
        Site site = getSite(siteLocation);
        for(ChargingStation cs : site.getChargingStations()) {
            if(cs.getName().equals(newName) && !cs.equals(chargingStation)) {
                throw new IllegalArgumentException("Charging station name already exists at this location");
            }
        }
        chargingStation.setName(newName);
    }

    public void updateChargingStationType(String siteLocation, String chargingStationName, String newType) {
        ChargingStation chargingStation = getChargingStation(siteLocation, chargingStationName);
        chargingStation.setType(newType);
    }

    // Delete Charging Station
    public void deleteChargingStation(String siteLocation, String chargingStationName) {
        Site site = getSite(siteLocation);
        ChargingStation chargingStation = getChargingStation(siteLocation, chargingStationName);
        site.removeChargingStation(chargingStation);
    }

    // Update Charger
    public void updateChargerName(String siteLocation, String chargingStationName, String oldName, String newName) {
        if(newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("New charger name cannot be null or blank");
        }
        Charger charger = getCharger(siteLocation, chargingStationName, oldName);
        ChargingStation chargingStation = getChargingStation(siteLocation, chargingStationName);
        for(Charger c : chargingStation.getChargers()) {
            if(c.getName().equals(newName) && !c.equals(charger)) {
                throw new IllegalArgumentException("Charger name already exists at this charging station");
            }
        }
        charger.setName(newName);
    }

    public void updateChargerMode(String siteLocation, String chargingStationName, String chargerName, String newMode) {
        Charger charger = getCharger(siteLocation, chargingStationName, chargerName);
        charger.setMode(newMode);
    }

    // Delete Charger
    public void deleteCharger(String siteLocation, String chargingStationName, String chargerName) {
        ChargingStation chargingStation = getChargingStation(siteLocation, chargingStationName);
        Charger charger = getCharger(siteLocation, chargingStationName, chargerName);
        chargingStation.removeCharger(charger);
    }

    // Legacy methods for backward compatibility
    public void updateChargerName(String siteLocation, String oldName, String newName) {
        Charger charger = getCharger(siteLocation, oldName);
        updateChargerName(siteLocation, charger.getChargingStation().getName(), oldName, newName);
    }

    public void updateChargerMode(String siteLocation, String chargerName, String newMode) {
        Charger charger = getCharger(siteLocation, chargerName);
        updateChargerMode(siteLocation, charger.getChargingStation().getName(), chargerName, newMode);
    }

    public void deleteCharger(String siteLocation, String chargerName) {
        Charger charger = getCharger(siteLocation, chargerName);
        deleteCharger(siteLocation, charger.getChargingStation().getName(), chargerName);
    }
}

