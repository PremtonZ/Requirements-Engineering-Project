package org.example;

public class Charger {
    private String name;
    private ChargingStation chargingStation;
    private String mode;
    private String state;

    public Charger(String name, ChargingStation chargingStation, String mode, String state) {
        setName(name);
        setChargingStation(chargingStation);
        setMode(mode);
        setState(state);
    }

    public Charger(Charger charger) {
        setName(charger.getName());
        setChargingStation(charger.getChargingStation());
        setMode(charger.getMode());
        setState(charger.getState());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("The Name cannot be null or blank");
        }
        this.name = name;
    }

    public ChargingStation getChargingStation() {
        return chargingStation;
    }

    public void setChargingStation(ChargingStation chargingStation) {
        if(chargingStation != null) {
            this.chargingStation = chargingStation;
        } else {
            throw new NullPointerException("chargingStation is null");
        }
    }

    public Site getSite() {
        return chargingStation != null ? chargingStation.getSite() : null;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        if(mode != null && (mode.equals("AC") || mode.equals("DC"))) {
            this.mode = mode;
        } else {
            throw new IllegalArgumentException("The Mode \"" + mode +"\" is not a valid Mode\nValid Modes are AC and DC");
        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if(state != null) {
            if(state.equals("available") || state.equals("occupied") || state.equals("out of order")) {
                this.state = state;
            } else {
                throw new IllegalArgumentException("The State \"" + state +"\" is not a valid State\n" +
                        "Valid States are \"available\", \"occupied\" and \"out of order\"");
            }
        }
    }

    public void checkForExistence(String name, ChargingStation chargingStation) {
        if(this.name.equals(name) && this.chargingStation.equals(chargingStation)) {
            throw new IllegalArgumentException("A Charger with that Name at the chosen charging station already exists");
        }
    }

    public String toStringShort() {
        return name +" Mode: " + mode + ", State: " + state + "\n";
    }

    @Override
    public String toString() {
        return "Charger: " + name + ", Station: " + (chargingStation != null ? chargingStation.getName() : "null") +
                ", Location: " + (getSite() != null ? getSite().getLocation() : "null") +
                ", Mode: " + mode + ", State: " + state;
    }

}
