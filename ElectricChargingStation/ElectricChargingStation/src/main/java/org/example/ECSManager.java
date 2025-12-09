package org.example;

import java.util.ArrayList;
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

    public Charger createCharger(String name, String siteLocation, String mode, String state) {
        Site site = getSite(siteLocation);
        for(Charger c : site.getChargers()) {
            if(c.getName().equals(name)) {
                throw new IllegalArgumentException("Charger name already exists at this location");
            }
        }
        Charger charger = new Charger(name, site, mode, state);
        site.addCharger(charger);
        return charger;
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

    public void addCredit(String username, double amount) {
        Account account = getAccount(username);
        account.topUp(amount);
    }

    public Site getSite(String siteLocation) {
        for(Site s : sites) {
            if(s.getLocation().equals(siteLocation)) return s;
        }
        throw new IllegalArgumentException("Site not found: " + siteLocation);
    }

    public Charger getCharger(String siteLocation, String chargerName) {
        Site site = getSite(siteLocation);
        for(Charger c : site.getChargers()) {
            if(c.getName().equals(chargerName)) return c;
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
}

