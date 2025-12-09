package org.example;

import java.util.ArrayList;
import java.util.List;

public class Site {
    private String location;
    private double acKwh = 0;
    private double acPpm = 0;
    private double dcKwh = 0;
    private double dcPpm = 0;
    private final List<ChargingStation> chargingStations = new ArrayList<>();

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

    public void addChargingStation(ChargingStation chargingStation) {
        if(chargingStation.getSite() == this) {
            for(ChargingStation cs : chargingStations) {
                if(cs.getName().equals(chargingStation.getName())) {
                    throw new IllegalArgumentException("Charging station with this name already exists at this location");
                }
            }
            chargingStations.add(chargingStation);
        } else {
            throw new IllegalArgumentException("The Site registered in the ChargingStation is not the same as the one it is going to be added");
        }
    }

    public void removeChargingStation(ChargingStation chargingStation) {
        if(chargingStations.contains(chargingStation)) {
            if(chargingStation.getChargers().isEmpty() ||
                    chargingStation.getChargers().stream().noneMatch(c -> c.getState().equals("occupied"))) {
                chargingStations.remove(chargingStation);
            } else {
                throw new IllegalArgumentException("Charging station has chargers in use");
            }
        } else {
            throw new IllegalArgumentException("Charging station does not exist at this Site");
        }
    }

    public List<ChargingStation> getChargingStations() {
        return chargingStations;
    }

    // Helper method to get all chargers from all charging stations
    public List<Charger> getChargers() {
        List<Charger> allChargers = new ArrayList<>();
        for(ChargingStation cs : chargingStations) {
            allChargers.addAll(cs.getChargers());
        }
        return allChargers;
    }

    @Override
    public String toString() {
        return "Location " + location +
                "\n  Current Prices: AC kWh: " + acKwh + ", AC Ppm: " + acPpm +
                ", DC kWh: "+ dcKwh + ", DC Ppm: "+dcPpm;
    }
}

