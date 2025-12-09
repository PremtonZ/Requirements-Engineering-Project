package org.example;

import java.util.ArrayList;
import java.util.List;

public class ChargingStation {
    private String name;
    private Site site;
    private String type;
    private final List<Charger> chargers = new ArrayList<>();

    public ChargingStation(String name, Site site, String type) {
        setName(name);
        setSite(site);
        setType(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Charging station name cannot be null or blank");
        }
        this.name = name;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        if(site == null) {
            throw new NullPointerException("Site cannot be null");
        }
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if(type != null && (type.equals("indoor") || type.equals("outdoor"))) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Type must be 'indoor' or 'outdoor'");
        }
    }

    public void addCharger(Charger charger) {
        if(charger.getChargingStation() == this) {
            for(Charger c : chargers) {
                if(c.getName().equals(charger.getName())) {
                    throw new IllegalArgumentException("Charger with this name already exists at this station");
                }
            }
            chargers.add(charger);
        } else {
            throw new IllegalArgumentException("Charger's charging station does not match");
        }
    }

    public void removeCharger(Charger charger) {
        if(chargers.contains(charger)) {
            if(!charger.getState().equals("occupied")) {
                chargers.remove(charger);
            } else {
                throw new IllegalArgumentException("Charger is currently in use");
            }
        } else {
            throw new IllegalArgumentException("Charger does not exist at this station");
        }
    }

    public List<Charger> getChargers() {
        return chargers;
    }

    @Override
    public String toString() {
        return "ChargingStation: " + name + ", Type: " + type + ", Location: " + site.getLocation();
    }
}

