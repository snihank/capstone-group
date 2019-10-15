package com.trilogyed.invoice.controller;

import com.trilogyed.invoice.exception.NotFoundException;
import com.trilogyed.invoice.model.InvoiceViewModel;
import com.trilogyed.invoice.service.ServiceLayer;
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
    ServiceLayer service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceViewModel addInvoice(@RequestBody @Valid InvoiceViewModel ovm){
        return service.addInvoice(ovm);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceViewModel> getAllInvoices(){
        return service.getAllInvoices();
    }

    @Cacheable
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceViewModel getInvoice(@PathVariable("id") int id){
        InvoiceViewModel invoice = service.getInvoice(id);
        if (invoice == null) {
            throw new NotFoundException("Invoice could not be retrieved for id " + id);
        }
        return invoice;
    }

    @CacheEvict(key = "#ovm.getInvoiceId()")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String updateInvoice(@RequestBody @Valid InvoiceViewModel ovm, @PathVariable("id") int id){
        service.updateInvoice(ovm, id);
        return "Invoice successfully updated";
    }

    @CacheEvict
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteInvoice(@PathVariable("id") int id){
        service.deleteInvoice(id);
        return "Invoice successfully deleted";
    }

    @GetMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceViewModel> getInvoiceByCustomerId(@PathVariable("id") int id){
        return service.getInvoicesByCustomerId(id);
    }


}
