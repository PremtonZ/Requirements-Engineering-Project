package org.example;

import java.util.Calendar;

public class ChargingInvoiceItem extends InvoiceItem {
    final double priceKwh;
    final double ppm;
    final Charger charger;
    private int duration;
    private int amountKWh;

    public ChargingInvoiceItem(int invoiceId, Charger charger, Account account,
                               int duration, int kWh, int day, int month, int year) {

        this.invoiceId = invoiceId;
        this.account = account;
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        this.date = cal.getTime();

        checkForChargerState(charger);
        this.charger = charger;

        if(charger.getMode().equals("AC")) {
            this.priceKwh = charger.getSite().getACKwh();
            this.ppm = charger.getSite().getACPpm();
        } else {
            this.priceKwh = charger.getSite().getDCKwh();
            this.ppm = charger.getSite().getDCPpm();
        }

        setDuration(duration);
        setAmountKWh(kWh);
        charger.setState("occupied");
    }

    public double getPriceKwh() {
        return priceKwh;
    }

    public double getPpm() {
        return ppm;
    }

    public Charger getCharger() {
        return charger;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if(duration > 0) {
            this.duration = duration;
        } else {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
    }

    public int getAmountKWh() {
        return amountKWh;
    }
    
    public void setAmountKWh(int amountKWh) {
        if(amountKWh > 0) {
            this.amountKWh = amountKWh;
        } else {
            throw new IllegalArgumentException("AmountKWh must be greater than 0");
        }
    }

    public double calculateTotal() {
        return priceKwh * amountKWh + ppm * duration;
    }

    public void checkForChargerState(Charger charger) {
        switch(charger.getState()){
            case "in operation free": return;
            case "occupied": throw new IllegalStateException("Charger is already occupied");
            case "out of order": throw new IllegalStateException("Charger is out of order");
        }
    }

    @Override
    public String toString() {
        String line = String.format("| %3d | %02d.%02d.%04d | %-18s | %-8s |  %2s  | %12.2f€ | %15.2f€ | %3d | %7d | %6.2f€ |\n",
                getInvoiceId(), getDate().getDate(), getDate().getMonth(), getDate().getYear(),
                charger.getSite().getLocation(), charger.getName(), charger.getMode(), priceKwh, ppm, amountKWh, duration, calculateTotal());
        return line.replace(",", ".");
    }
}
