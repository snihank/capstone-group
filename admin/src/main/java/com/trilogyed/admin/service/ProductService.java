package com.trilogyed.admin.service;


import com.trilogyed.admin.util.feign.ProductClient;
import com.trilogyed.admin.util.messages.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductService {

    @Autowired
    private ProductClient productClient;

    public ProductService() {
    }

    public ProductService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public Product addProduct(Product product) {
        return productClient.addProduct(product);
    }

    public Product getProduct(int id) {
        return productClient.getProduct(id);
    }

    public void updateProduct(int id, Product product) {
        productClient.updateProduct(id, product);
    }

    public void deleteProduct(int id) {
        productClient.deleteProduct(id);
    }

    public List<Product> getAllProducts() {
        return productClient.getAllProducts();
    }

}