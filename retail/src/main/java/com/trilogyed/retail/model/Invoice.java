package com.trilogyed.retail.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Invoice implements Serializable {


    private int invoiceId;
    @NotNull(message = "Please supply a customer id.")
    @Positive
    private Integer customerId;
    private LocalDate purchaseDate;
    @Valid
    private List<InvoiceItem> invoiceItems;

    public Invoice() {
    }

    public Invoice(int invoiceId, @NotNull(message = "Please supply a customer id.") @Positive Integer customerId, LocalDate purchaseDate, @Valid List<InvoiceItem> invoiceItems) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.purchaseDate = purchaseDate;
        this.invoiceItems = invoiceItems;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice)) return false;
        Invoice invoice = (Invoice) o;
        return getInvoiceId() == invoice.getInvoiceId() &&
                getCustomerId().equals(invoice.getCustomerId()) &&
                getPurchaseDate().equals(invoice.getPurchaseDate()) &&
                getInvoiceItems().equals(invoice.getInvoiceItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceId(), getCustomerId(), getPurchaseDate(), getInvoiceItems());
    }
}
