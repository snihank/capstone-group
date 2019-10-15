package com.trilogyed.product.model;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Product implements Serializable {

    private int productId;

    @Size(min = 1, max = 50, message = "Product name must not exceed 50 characters.")
    private String productName;

    @Size(max = 255, message = "Product description must not exceed {max} characters.")
    private String productDescription;

    @NotNull(message = "Please supply a list price.")
    private BigDecimal listPrice;

    @NotNull(message = "Please supply a unit cost.")
    private BigDecimal unitCost;


    public Product() {
    }

    public Product(int productId, String productName, String productDescription, BigDecimal listPrice, BigDecimal unitCost) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.listPrice = listPrice;
        this.unitCost = unitCost;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getListPrice() {
        // avoid NullPointer Exceptions when doing calculations
        if (listPrice == null) {
            return new BigDecimal(0);
        }
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public BigDecimal getUnitCost() {
        // avoid NullPointer Exceptions when doing calculations
        if (unitCost == null) {
            return new BigDecimal(0);
        }
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    // override methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return getProductId() == product.getProductId() &&
                getProductName().equals(product.getProductName()) &&
                getProductDescription().equals(product.getProductDescription()) &&
                getListPrice().equals(product.getListPrice()) &&
                getUnitCost().equals(product.getUnitCost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId(), getProductName(), getProductDescription(), getListPrice(), getUnitCost());
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", listPrice=" + listPrice +
                ", unitCost=" + unitCost +
                '}';
    }

}
