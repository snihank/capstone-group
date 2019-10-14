package com.trilogyed.retail.model;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Inventory {
    private Integer inventoryId;
    @NotNull(message = "Please enter a Product Id")
    private Integer productId;
    @NotNull(message = "Please enter Quantity")
    private Integer quantity;

    public Inventory() {
    }

    public Inventory(Integer inventoryId, @NotNull(message = "Please enter a Product Id") Integer productId, @NotNull(message = "Please enter Quantity") Integer quantity) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;
        Inventory inventory = (Inventory) o;
        return getInventoryId().equals(inventory.getInventoryId()) &&
                getProductId().equals(inventory.getProductId()) &&
                getQuantity().equals(inventory.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInventoryId(), getProductId(), getQuantity());
    }
}
