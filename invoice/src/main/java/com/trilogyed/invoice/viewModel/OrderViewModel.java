package com.trilogyed.invoice.viewModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.trilogyed.invoice.model.Invoice;
import com.trilogyed.invoice.model.InvoiceItem;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class OrderViewModel extends Invoice implements Serializable {

    private int invoiceId;
    @NotNull(message = "Please supply a customer id.")
    @Positive
    private Integer customerId;
    private LocalDate purchaseDate;
    @JsonProperty("InvoiceItem")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<InvoiceItem> invoiceItems;

    public OrderViewModel() {
    }

//    public OrderViewModel(int invoiceId, @NotNull(message = "Please supply a customer id.") @Positive Integer customerId, LocalDate purchaseDate, List<InvoiceItem> invoiceItems) {
//        this.invoiceId = invoiceId;
//        this.customerId = customerId;
//        this.purchaseDate = purchaseDate;
//        this.invoiceItems = invoiceItems;
//    }

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

    @Override
    public String toString() {
        return "OrderViewModel{" +
                "invoiceId=" + invoiceId +
                ", customerId=" + customerId +
                ", purchaseDate=" + purchaseDate +
                ", invoiceItems=" + invoiceItems +
                '}';
    }
}
