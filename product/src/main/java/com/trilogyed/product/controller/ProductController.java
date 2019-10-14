package com.trilogyed.product.controller;

import com.trilogyed.product.exception.NotFoundException;
import com.trilogyed.product.model.Product;
import com.trilogyed.product.service.ProductService;
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

    // adds the return value of the method to the cache using product id as the key
    @CachePut(key = "#result.getProductId()")
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody @Valid Product product) {
        return productService.addProduct(product);
    }

    // caches the result of the method - it automatically uses id as the key
    @Cacheable
    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable int id) {
        Product product = productService.getProduct(id);
        if (product == null)
            throw new NotFoundException("Product could not be retrieved for id " + id);
        return product;
    }

    // removes product with given product id as the key from the cache
    @CacheEvict(key = "#product.getProductId()")
    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable int id, @RequestBody @Valid Product product) {
        if (product.getProductId() == 0)
            product.setProductId(id);
        if (id != product.getProductId()) {
            throw new IllegalArgumentException("ID on path must match the ID in the Product object");
        }
        productService.updateProduct(product);
    }

    // removes product with given product id as the key from the cache
    @CacheEvict
    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products;
    }

}

