package com.trilogyed.inventory.model;


import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Inventory {

    private int inventoryId;


    @NotNull(message = "Please supply a product id.")
    private Integer productId;

    @NotNull(message = "Please supply a quantity.")
    private Integer quantity;

    // constructors

    public Inventory() {
    }

    public Inventory(int inventoryId, Integer productId, Integer quantity) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // getters and setters

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
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

    // override methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return getInventoryId() == inventory.getInventoryId() &&
                getProductId().equals(inventory.getProductId()) &&
                getQuantity().equals(inventory.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInventoryId(), getProductId(), getQuantity());
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }

}


