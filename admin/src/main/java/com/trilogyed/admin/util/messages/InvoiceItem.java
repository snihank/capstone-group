package com.trilogyed.admin.util.messages;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceItem implements Serializable {

    private int invoiceItemId;

    private Integer invoiceId;

    // wrapper and not primitive int to validate if supplied
    @Positive
    private Integer inventoryId;

    // wrapper and not primitive int to validate if supplied
    private Integer quantity;

    @DecimalMin(value = "0.0", inclusive = true, message = "The min value you can enter for unit price is {value}.")
    @DecimalMax(value = "99999.99", inclusive = true, message = "The max value you can enter for unit price is {value}")
    private BigDecimal unitPrice;

    // constructors

    public InvoiceItem() {
    }

    public InvoiceItem(int invoiceItemId, Integer invoiceId, Integer inventoryId, Integer quantity, BigDecimal unitPrice) {
        this.invoiceItemId = invoiceItemId;
        this.invoiceId = invoiceId;
        this.inventoryId = inventoryId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // getters and setters

    public int getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(int invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getInventoryId() {
        // added b/c @Valid not working to test @NotNull of list objects
        if (inventoryId == null) {
            throw new NullPointerException("An inventory id is required");
        }
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getQuantity() {
        // added b/c @Valid not working to test @NotNull of list objects
        if (quantity == null) {
            throw new NullPointerException("A quantity is required");
        }
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        // added b/c @Valid not working to test @NotNull of list objects
        if (unitPrice == null) {
            throw new NullPointerException("A unit price is required");
        }
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    // override methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceItem that = (InvoiceItem) o;
        boolean check = false;
            if((getInvoiceId() == null && that.getInvoiceId() == null) || getInvoiceId().equals(that.getInvoiceId())) {
                check = true;
            }else{
                return false;
            }
            if((getInventoryId() == null && that.getInventoryId() == null) || getInventoryId().equals(that.getInventoryId())){
                check=true;
            }else{
                return false;
            }
            if((getQuantity() == null && that.getQuantity() == null) || getQuantity().equals(that.getQuantity())){
                check = true;
            }else{
                return false;
            }
            if((getUnitPrice() == null && that.getUnitPrice() == null) ||getUnitPrice().equals(that.getUnitPrice())){
                check = true;
            }else{
                return false;
            }
        return check ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceItemId(), getInvoiceId(), getInventoryId(), getQuantity(), getUnitPrice());
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
                "invoiceItemId=" + invoiceItemId +
                ", invoiceId=" + invoiceId +
                ", inventoryId=" + inventoryId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }

}
