package org.example;

public class Main {
    public static void main(String[] args) {
        ECSManager network = new ECSManager();

        Site site1 = network.createSite("Deutschwagram");
        Site site2 = network.createSite("Wien");
        System.out.println("Sites: " + site1.getLocation() + ", " + site2.getLocation());

        Charger charger1 = network.createCharger("AC_1", "Deutschwagram", "AC", "in operation free");
        Charger charger2 = network.createCharger("DC_1", "Deutschwagram", "DC", "in operation free");
        System.out.println("Chargers: " + charger1.getName() + " (" + charger1.getMode() + "), " + charger2.getName() + " (" + charger2.getMode() + ")");

        Charger ac1 = network.getCharger("Deutschwagram", "AC_1");
        ac1.setMode("AC");
        System.out.println("Charger type: " + ac1.getName() + " = " + ac1.getMode());

        network.setSitePrices("Deutschwagram", 0.42, 1.0, 0.55, 3.0);
        Site site = network.getSite("Deutschwagram");
        System.out.println("Prices: AC " + site.getACKwh() + "/kWh, DC " + site.getDCKwh() + "/kWh");

        Site selected = network.getSite("Deutschwagram");
        System.out.println("Selected: " + selected.getLocation() + " (" + network.getChargerCountAtSite("Deutschwagram") + " chargers)");

        Charger ac = network.getCharger("Deutschwagram", "AC_1");
        Charger dc = network.getCharger("Deutschwagram", "DC_1");
        System.out.println("Status: " + ac.getName() + " = " + ac.getMode() + ", " + ac.getState());
        System.out.println("Status: " + dc.getName() + " = " + dc.getMode() + ", " + dc.getState());

        Account account1 = network.createAccount("Max");
        Account account2 = network.createAccount("Philipp");
        System.out.println("Accounts: " + account1.getUsername() + " (ID:" + account1.getCustomerId() + "), " + account2.getUsername() + " (ID:" + account2.getCustomerId() + ")");

        Account max = network.getAccount("Max");
        System.out.println("Account: " + max.getUsername() + ", Credits: " + max.getCredit());

        network.addCredit("Max", 200.0);
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
    }
}
