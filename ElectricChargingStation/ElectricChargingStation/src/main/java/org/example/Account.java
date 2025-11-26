package org.example;

import java.util.ArrayList;
import java.util.List;

public class Account {
    final int customerId;
    private String username;
    private double credit = 0;
    private final List<InvoiceItem> invoiceItems = new ArrayList<>();

    public Account(int customerId, String username) {
        this.customerId = customerId;
        setUsername(username);
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setUsername(String username) {
        if(username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if(username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if(username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void topUp(double credits) {
        if(credits > 0) {
            credit += credits;
            invoiceItems.add(new TopUpInvoiceItem(invoiceItems.size(), this, credits));
        } else {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    public void topUp(double credits, int day, int month, int year) {
        if(credits > 0) {
            credit += credits;
            invoiceItems.add(new TopUpInvoiceItem(invoiceItems.size(), this, credits, year, month, day));
        } else {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    public void pay(Charger charger, int duration, int kWh, int year, int month, int day) {
        ChargingInvoiceItem charging = new ChargingInvoiceItem(invoiceItems.size(), charger, this, duration, kWh, year, month, day);
        double amount = charging.calculateTotal();
        if(amount <= credit) {
            credit -= amount;
            invoiceItems.add(charging);
        } else {
            throw new IllegalArgumentException("Not enough credits");
        }
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public double getCredit() {
        return credit;
    }

    public void checkForExistence(String username) {
        if(username.equals(this.username)) {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    public String getBill() {
        if(invoiceItems.isEmpty()) {
            return "No invoices";
        }
        return "Invoice for " + username + " - " + invoiceItems.size() + " items";
    }

    @Override
    public String toString() {
        return "Username: " + username + ", Credits: " + credit;
    }
}
