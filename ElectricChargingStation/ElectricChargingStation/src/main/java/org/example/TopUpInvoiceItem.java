package org.example;

import java.util.Calendar;
import java.util.Date;

public class TopUpInvoiceItem extends InvoiceItem {
    final double topUpAmount;

    public TopUpInvoiceItem(int invoiceId, Account account, double topUpAmount, int year, int month, int day) {
        this.invoiceId = invoiceId;
        this.account = account;
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        this.date = cal.getTime();
        this.topUpAmount = topUpAmount;
    }
    public TopUpInvoiceItem(int invoiceId, Account account, double topUpAmount) {
        this.invoiceId = invoiceId;
        this.account = account;
        this.date = new Date();
        this.topUpAmount = topUpAmount;
    }

    @Override
    public String toString() {
        return String.format("| %3d | %02d.%02d.%04d | %6.2f€ |\n",
                getInvoiceId(), getDate().getDate(), getDate().getMonth(), getDate().getYear(), topUpAmount);
    }
}
