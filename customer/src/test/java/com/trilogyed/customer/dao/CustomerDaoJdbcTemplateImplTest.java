package com.trilogyed.customer.dao;


import com.trilogyed.customer.exception.NotFoundException;
import com.trilogyed.customer.model.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerDaoJdbcTemplateImplTest {

    @Autowired
    CustomerDao customerDao;

    // clear customer table in database
    @Before
    public void setUp() throws Exception {
        List<Customer> customers = customerDao.getAllCustomers();
        for (Customer c : customers) {
            customerDao.deleteCustomer(c.getCustomerId());
        }
    }

    @Test
    public void addGetDeleteCustomer() {

        //Populating the customer
        Customer customer = new Customer();
        customer.setFirstName("Robert");
        customer.setLastName("De NIro");
        customer.setStreet("120 Lafayette Street");
        customer.setCity("NY");
        customer.setZip("10013");
        customer.setEmail("rniro@yahoo.com");
        customer.setPhone("724-879-9234");

        customer = customerDao.addCustomer(customer);

        Customer customer1 = customerDao.getCustomer(customer.getCustomerId());
        assertEquals(customer, customer1);

        customerDao.deleteCustomer(customer.getCustomerId());
        customer1 = customerDao.getCustomer(customer.getCustomerId());
        assertNull(customer1);
    }


    @Test
    public void getCustomerWithNonExistentId() {
        Customer customer = customerDao.getCustomer(500);
        assertNull(customer);
    }

    // throw exception if id provided does not exist when trying to delete customer
    @Test(expected  = NotFoundException.class)
    public void deleteCustomerWithNonExistentId() {

        customerDao.deleteCustomer(500);

    }

    // tests updateCustomer()
    @Test
    public void updateCustomer() {

        Customer customer = new Customer();
        customer.setFirstName("Robert");
        customer.setLastName("De NIro");
        customer.setStreet("120 Lafayette Street");
        customer.setCity("NY");
        customer.setZip("10013");
        customer.setEmail("rniro@yahoo.com");
        customer.setPhone("724-879-9234");

        customer = customerDao.addCustomer(customer);

        customer.setEmail("updatedemail@yahoo.com");

        customerDao.updateCustomer(customer);

        Customer customer1 = customerDao.getCustomer(customer.getCustomerId());
        assertEquals(customer, customer1);
    }

    // tests if will throw exception if id provided does not exist when trying to update customer
    @Test(expected  = IllegalArgumentException.class)
    public void updateCustomerWithIllegalArgumentException() {

        Customer customer = new Customer();
        customer.setCustomerId(500);
        customer.setFirstName("Robert");
        customer.setLastName("De NIro");
        customer.setStreet("120 Lafayette Street");
        customer.setCity("NY");
        customer.setZip("10013");
        customer.setEmail("rniro@yahoo.com");
        customer.setPhone("724-879-9234");

        customerDao.updateCustomer(customer);

    }

    // tests getAllCustomers()
    @Test
    public void getAllCustomers() {

        Customer customer = new Customer();
        customer.setFirstName("Robert");
        customer.setLastName("De NIro");
        customer.setStreet("120 Lafayette Street");
        customer.setCity("NY");
        customer.setZip("10013");
        customer.setEmail("rniro@yahoo.com");
        customer.setPhone("724-879-9234");

        customerDao.addCustomer(customer);

        customer = new Customer();
        customer.setFirstName("Mary");
        customer.setLastName("watson");
        customer.setStreet("130 sinclair Street");
        customer.setCity("Fremont");
        customer.setZip("27511");
        customer.setEmail("mjane@gmail.com");
        customer.setPhone("919-374-2901");

        customerDao.addCustomer(customer);

        List<Customer> cList = customerDao.getAllCustomers();
        assertEquals(2, cList.size());
    }

}
