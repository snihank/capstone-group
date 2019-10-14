package com.trilogyed.product.dao;

import com.trilogyed.product.model.Product;

import java.util.List;

public interface ProductDao {

    // standard CRUD

    Product addProduct(Product product);

    Product getProduct(int id);

    void updateProduct(Product product);

    void deleteProduct(int id);

    List<Product> getAllProducts();

}
