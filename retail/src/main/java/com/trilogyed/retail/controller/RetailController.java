package com.trilogyed.retail.controller;

import com.trilogyed.retail.model.OrderViewModel;
import com.trilogyed.retail.model.OrderViewModelResponse;
import com.trilogyed.retail.model.Product;
import com.trilogyed.retail.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RefreshScope
@CacheConfig(cacheNames = {"retail"})
@RequestMapping("/")
public class RetailController {

    @Autowired
    private ServiceLayer service;

    @GetMapping(value = "levelup/customer/{id}")
    public int getLevelUp(@PathVariable("id") int id) {
        return service.getPoints(id);
    }

    @CachePut(key = "#result.getInvoiceId()")
    @PostMapping(value = "invoices")
    public OrderViewModelResponse addInvoice(@RequestBody OrderViewModel ivm) {
        return service.addInvoice(ivm);
    }

    @Cacheable
    @RequestMapping(value = "invoices/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public OrderViewModel getInvoice(@PathVariable("id") int id){
        return service.getInvoice(id);
    }


    @GetMapping(value = "invoices")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderViewModel> getAllInvoices() {
        return service.getAllInvoices();
    }

    @GetMapping(value = "invoices/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderViewModel> getInvoiceByCustomerId(@PathVariable("id") int id){
        return service.getInvoicesByCustomerId(id);
    }

    @GetMapping(value = "products/inventory")
    public List<Product> getProductsInInventory() {
        return service.getProductsInInventory();
    }

    @Cacheable
    @GetMapping(value = "products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable int id) {
        return service.getProduct(id);
    }

    @Cacheable
    @GetMapping(value = "products/invoice/{id}")
    public List<Product> getProductsByInvoiceId(@PathVariable int id) {
        return service.getProductsByInvoiceId(id);
    }


}