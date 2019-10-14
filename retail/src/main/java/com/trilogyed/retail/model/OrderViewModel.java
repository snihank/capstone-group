package com.trilogyed.retail.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class OrderViewModel extends Invoice{
    private int invoiceId;
    @NotNull(message = "Please supply a customer id.")
    @Positive
    private Integer customerId;
    private LocalDate purchaseDate;
    @Valid
    private List<InvoiceItem> invoiceItems;

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
        if (!(o instanceof OrderViewModel)) return false;
        OrderViewModel that = (OrderViewModel) o;
        return getInvoiceId() == that.getInvoiceId() &&
                getCustomerId().equals(that.getCustomerId()) &&
                getPurchaseDate().equals(that.getPurchaseDate()) &&
                getInvoiceItems().equals(that.getInvoiceItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceId(), getCustomerId(), getPurchaseDate(), getInvoiceItems());
    }
}
