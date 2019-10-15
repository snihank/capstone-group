package com.trilogyed.admin.controller;


import com.trilogyed.admin.service.CustomerService;

import com.trilogyed.admin.util.messages.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RefreshScope
@CacheConfig(cacheNames = {"customers"})
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @CachePut(key = "#result.getCustomerId()")
    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Customer addCustomer(@RequestBody @Valid Customer customer) {
        return customerService.addCustomer(customer);
    }

    @Cacheable
    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Customer getCustomer(@PathVariable int id) {
        Customer customer = customerService.getCustomer(id);
        return customer;
    }

    @CacheEvict(key = "#customer.getCustomerId()")
    @RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCustomer(@PathVariable int id, @RequestBody @Valid Customer customer) {
        if (customer.getCustomerId() == 0)
            customer.setCustomerId(id);
        if (id != customer.getCustomerId()) {
            throw new IllegalArgumentException("ID on path must match the ID in the Customer object");
        }
        customerService.updateCustomer(id, customer);
    }


    @CacheEvict
    @RequestMapping(value = "/customers/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomer(id);
    }


    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return customers;
    }

    /**************************FOR SECURITY**********************************************/

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loggedIn(Principal principal) {
        return "Hello " + principal.getName() + "! Looks like you're logged in!";
    }
    @RequestMapping(value = "/allDone", method = RequestMethod.GET)
    public String allDone() {
        return "That's All Folks!";
    }

}

