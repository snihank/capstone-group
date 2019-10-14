package com.trilogyed.retail.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceItem implements Serializable {
    private int invoiceItemId;
    private Integer invoiceId;
    @Positive
    private Integer inventoryId;
    private Integer quantity;
    @DecimalMin(value = "0.0", inclusive = true, message = "The min value you can enter for unit price is {value}.")
    @DecimalMax(value = "99999.99", inclusive = true, message = "The max value you can enter for unit price is {value}")
    private BigDecimal unitPrice;

    public InvoiceItem() {
    }

    public InvoiceItem(int invoiceItemId, Integer invoiceId, @Positive Integer inventoryId, Integer quantity, @DecimalMin(value = "0.0", inclusive = true, message = "The min value you can enter for unit price is {value}.") @DecimalMax(value = "99999.99", inclusive = true, message = "The max value you can enter for unit price is {value}") BigDecimal unitPrice) {
        this.invoiceItemId = invoiceItemId;
        this.invoiceId = invoiceId;
        this.inventoryId = inventoryId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(int invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceItem)) return false;
        InvoiceItem that = (InvoiceItem) o;
        return getInvoiceItemId() == that.getInvoiceItemId() &&
                getInvoiceId().equals(that.getInvoiceId()) &&
                getInventoryId().equals(that.getInventoryId()) &&
                getQuantity().equals(that.getQuantity()) &&
                getUnitPrice().equals(that.getUnitPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceItemId(), getInvoiceId(), getInventoryId(), getQuantity(), getUnitPrice());
    }

}
