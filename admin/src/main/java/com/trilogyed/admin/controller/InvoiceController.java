package com.trilogyed.admin.controller;


import com.trilogyed.admin.service.InvoiceService;
import com.trilogyed.admin.util.messages.Invoice;
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
@RequestMapping("/invoices")
@CacheConfig(cacheNames = {"invoices"})
public class InvoiceController {

    @Autowired
    InvoiceService service;

    @CachePut(key = "#result.getInvoiceId()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice addInvoice(@RequestBody Invoice ivm){
        return service.addInvoice(ivm);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> getAllInvoices(){
        return service.getAllInvoices();
    }


    @Cacheable
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invoice getInvoice(@PathVariable("id") int id){
        return service.getInvoice(id);
    }

    @CacheEvict(key = "#ivm.getInvoiceId()")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateInvoice(@RequestBody @Valid Invoice ivm, @PathVariable("id") int id){
        if (ivm.getInvoiceId() == 0)
            ivm.setInvoiceId(id);
        if (id != ivm.getInvoiceId()) {
            throw new IllegalArgumentException("ID on path must match the ID in the Invoice object");
        }
        service.updateInvoice(ivm, id);
    }

    @CacheEvict
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable("id") int id){
        service.deleteInvoice(id);
    }


    @GetMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> getInvoiceByCustomerId(@PathVariable("id") int id){
        return service.getInvoicesByCustomerId(id);
    }

}
