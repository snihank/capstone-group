package com.trilogyed.admin.service;


import com.trilogyed.admin.util.feign.CustomerClient;
import com.trilogyed.admin.util.messages.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerService {

    CustomerClient customerClient;

    // constructors

    public CustomerService() {
    }

    @Autowired
    public CustomerService(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    // methods

    public Customer addCustomer(Customer customer) {
        return customerClient.addCustomer(customer);
    }

    public Customer getCustomer(int id) {
        return customerClient.getCustomer(id);
    }

    public void updateCustomer(int id, Customer customer) {
        customerClient.updateCustomer(id, customer);
    }

    public void deleteCustomer(int id) {
        customerClient.deleteCustomer(id);
    }

    public List<Customer> getAllCustomers() {
        return customerClient.getAllCustomers();
    }

}
