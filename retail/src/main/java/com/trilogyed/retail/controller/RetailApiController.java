package com.trilogyed.retail.controller;

import com.trilogyed.retail.model.InvoiceViewModel;
import com.trilogyed.retail.model.InvoiceViewModelResponse;
import com.trilogyed.retail.model.Product;
import com.trilogyed.retail.service.RetailApiService;
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
@CacheConfig(cacheNames = "retail")
public class RetailApiController {

    @Autowired
    private RetailApiService service;

    @Autowired
    public RetailApiController(RetailApiService service) {
        this.service = service;
    }

    @RequestMapping(value = "/levelup/customer/{id}", method = RequestMethod.GET)
    public int getLevelUp(@PathVariable("id") int id) {
        return service.getPoints(id);
    }


    @CachePut(key = "#result.getInvoiceId()")
    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    public InvoiceViewModelResponse addInvoice(@RequestBody InvoiceViewModel ivm) {
        return service.addInvoice(ivm);
    }


    @Cacheable
    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public InvoiceViewModel getInvoice(@PathVariable("id") int id){
        return service.getInvoice(id);
    }


    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceViewModel> getAllInvoices() {
        return service.getAllInvoices();
    }

    @RequestMapping(value = "/invoices/customer/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceViewModel> getInvoiceByCustomerId(@PathVariable("id") int id){
        return service.getInvoicesByCustomerId(id);
    }

    @RequestMapping(value = "/products/inventory", method = RequestMethod.GET)
    public List<Product> getProductsInInventory() {
        return service.getProductsInInventory();
    }


    @Cacheable
    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable int id) {
        return service.getProduct(id);
    }


    @Cacheable
    @RequestMapping(value = "/products/invoice/{id}", method = RequestMethod.GET)
    public List<Product> getProductsByInvoiceId(@PathVariable int id) {
        return service.getProductsByInvoiceId(id);
    }

}
