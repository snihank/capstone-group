package com.trilogyed.admin.controller;


import com.trilogyed.admin.service.ProductService;
import com.trilogyed.admin.util.messages.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@CacheConfig(cacheNames = {"products"})
public class ProductController {

    @Autowired
    ProductService productService;



    public ProductController() {
    }

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @CachePut(key = "#result.getProductId()")
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody @Valid Product product) {
        return productService.addProduct(product);
    }

    @Cacheable
    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable int id) {
        Product product = productService.getProduct(id);
        return product;
    }

    @CacheEvict(key = "#product.getProductId()")
    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable int id, @RequestBody @Valid Product product) {
        if (product.getProductId() == 0)
            product.setProductId(id);
        if (id != product.getProductId()) {
            throw new IllegalArgumentException("ID on path must match the ID in the Product object");
        }
        productService.updateProduct(id, product);
    }

    @CacheEvict
    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

}
