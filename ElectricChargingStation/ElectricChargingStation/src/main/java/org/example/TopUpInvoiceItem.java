package org.example;

import java.sql.Date;
import java.time.LocalDate;

public class TopUpInvoiceItem extends InvoiceItem {
    final double topUpAmount;

    public TopUpInvoiceItem(int invoiceId, Account account, double topUpAmount, int year, int month, int day) {
        this.invoiceId = invoiceId;
        this.account = account;
        LocalDate localDate = LocalDate.of(year, month, day);
        this.date = Date.valueOf(localDate);
        this.topUpAmount = topUpAmount;
    }
    public TopUpInvoiceItem(int invoiceId, Account account, double topUpAmount) {
        this.invoiceId = invoiceId;
        this.account = account;
        LocalDate localDate = LocalDate.now();
        this.date = Date.valueOf(localDate);
        this.topUpAmount = topUpAmount;
    }

    @Override
    public String toString() {
        java.util.Date date = getDate();
        return String.format("| %3d | %02d.%02d.%04d | %6.2f€ |\n",
                getInvoiceId(), date.getDate(), date.getMonth() + 1, date.getYear() + 1900, topUpAmount);
    }
}