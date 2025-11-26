package org.example;

public class Charger {
    private String name;
    private Site site;
    private String mode;
    private String state;

    public Charger(String name, Site site, String mode, String state) {
        setName(name);
        setSite(site);
        setMode(mode);
        setState(state);
    }

    public Charger(Charger charger) {
        setName(charger.getName());
        setSite(charger.getSite());
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

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        if(site != null) {
            this.site = site;
        } else {
            throw new NullPointerException("site is null");
        }
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
            if(state.equals("in operation free") || state.equals("occupied") || state.equals("out of order")) {
                this.state = state;
            } else {
                throw new IllegalArgumentException("The State \"" + state +"\" is not a valid State\n" +
                        "Valid States are \"in operation free\", \"occupied\" and \"out of order\"");
            }
        }
    }

    public void checkForExistence(String name, Site site) {
        if(this.name.equals(name) && this.site.equals(site)) {
            throw new IllegalArgumentException("A Charger with that Name at the chosen location already exists");
        }
    }

    public String toStringShort() {
        return name +" Mode: " + mode + ", State: " + state + "\n";
    }

    @Override
    public String toString() {
        return "Charger: " + name + ", Site: " + site.getLocation() + ", Mode: " + mode + ", State: " + state;
    }

}
