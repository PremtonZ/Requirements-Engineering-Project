package org.example;

import java.util.Date;

public class InvoiceItem {
    int invoiceId;
    Account account;
    Date date;

    public InvoiceItem() {
    }

    public InvoiceItem(int invoiceId, Account account, Date date) {
        this.invoiceId = invoiceId;
        this.account = account;
        this.date = date;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public Account getAccount() {
        return account;
    }

    public Date getDate() {
        return date;
    }
}
