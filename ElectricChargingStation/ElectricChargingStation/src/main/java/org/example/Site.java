package org.example;

import java.util.ArrayList;
import java.util.List;

public class Site {
    private String location;
    private double acKwh = 0;
    private double acPpm = 0;
    private double dcKwh = 0;
    private double dcPpm = 0;
    private final List<Charger> chargers = new ArrayList<Charger>();

    public Site(String location) {
        this.location = location;
    }

    public void setLocation(String location, List<Site> sites) {
        if(location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location Name cannot be null or blank");
        }
        for (Site site : sites) {
            if(site.getLocation().equals(location)) {
                throw new IllegalArgumentException("A Site with that Name already exists");
            }
        }
        this.location = location;
    }

    public void setACKwh(double acKwh) {
        if(acKwh > 0) {
            this.acKwh = acKwh;
        } else {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }

    public void setACPpm(double acPpm) {
        if(acPpm > 0) {
            this.acPpm = acPpm;
        } else {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }

    public void setDCKwh(double dcKwh) {
        if(dcKwh > 0) {
            this.dcKwh = dcKwh;
        } else {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }

    public void setDCPpm(double dcPpm) {
        if(dcPpm > 0) {
            this.dcPpm = dcPpm;
        } else {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }

    public String getLocation() {
        return location;
    }

    public double getACKwh() {
        return acKwh;
    }

    public double getACPpm() {
        return acPpm;
    }

    public double getDCKwh() {
        return dcKwh;
    }

    public double getDCPpm() {
        return dcPpm;
    }

    public void checkForExistence(String location) {
        if(location.equals(this.location)) {
            throw new IllegalArgumentException("A Site at that location already exists");
        }
    }

    public void setAllPrices(Double acKwh, Double acPpm, Double dcKwh, Double dcPpm) {
        setACKwh(acKwh);
        setACPpm(acPpm);
        setDCKwh(dcKwh);
        setDCPpm(dcPpm);
    }

    public void addCharger(Charger charger) {
        boolean exists = false;
        if(charger.getSite() == this) {
            for(Charger c : chargers) {
                if (charger.getName().equals(c.getName())) {
                    exists = true;
                    break;
                }
            }
        } else {
            throw new IllegalArgumentException("The Site registered in the Charger is not the same as the one it is going to be added");
        }
        if(!exists) {
            chargers.add(charger);
        }
    }

    public void removeCharger(Charger charger) {
        if(chargers.contains(charger)) {
            if(!charger.getState().equals("occupied")) {
                chargers.remove(charger);
            } else {
                throw new IllegalArgumentException("The Charger is currently in use");
            }
        } else {
            throw new IllegalArgumentException("Charger does not exists in this Site");
        }
    }

    public List<Charger> getChargers() {
        return chargers;
    }

    @Override
    public String toString() {
        return "Location " + location +
                "\n  Current Prices: AC kWh: " + acKwh + ", AC Ppm: " + acPpm +
                ", DC kWh: "+ dcKwh + ", DC Ppm: "+dcPpm;
    }
}
