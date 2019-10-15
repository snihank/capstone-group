package com.trilogyed.customer.dao;


import com.trilogyed.customer.model.Customer;

import java.util.List;

public interface CustomerDao {


    Customer addCustomer(Customer customer);

    Customer getCustomer(int id);

    void updateCustomer(Customer customer);

    void deleteCustomer(int id);

    List<Customer> getAllCustomers();

}
