package com.trilogyed.admin.service;

import com.trilogyed.admin.util.feign.CustomerClient;
import com.trilogyed.admin.util.messages.Customer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {


    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerClient customerClient;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        setUpCustomerClientMock();

    }

    @Test
    public void addCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Will");
        customer.setLastName("Smith");
        customer.setStreet("1000 Sturdivant Street");
        customer.setCity("NY");
        customer.setZip("11455");
        customer.setEmail("smith@gmail.com");
        customer.setPhone("919-374-2901");

        customer = customerService.addCustomer(customer);

        Customer customer1 = customerService.getCustomer(customer.getCustomerId());

        assertEquals(customer, customer1);
    }

    @Test
    public void getCustomer() {

        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("Bernie");
        customer.setLastName("Sanders");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Hollis");
        customer.setZip("11423");
        customer.setEmail("bernie@yahoo.com");
        customer.setPhone("724-879-9234");

        Customer customer1 = customerService.getCustomer(customer.getCustomerId());

        assertEquals(customer, customer1);
    }


    @Test
    public void findAllCustomers() {

        Customer customer = new Customer();
        customer.setFirstName("Bernie");
        customer.setLastName("Sanders");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Hollis");
        customer.setZip("11423");
        customer.setEmail("bernie@yahoo.com");
        customer.setPhone("724-879-9234");

        customerService.addCustomer(customer);

        Customer customer3 = new Customer();
        customer3.setFirstName("Will");
        customer3.setLastName("Smith");
        customer3.setStreet("1000 Sturdivant Street");
        customer3.setCity("NY");
        customer3.setZip("11455");
        customer3.setEmail("smith@gmail.com");
        customer3.setPhone("919-374-2901");

        customerService.addCustomer(customer);

        List<Customer> fromService = customerService.getAllCustomers();

        assertEquals(2, fromService.size());

    }


    @Test
    public void deleteCustomer() {
        Customer customer = customerService.getCustomer(1);
        customerService.deleteCustomer(1);
        ArgumentCaptor<Integer> postCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(customerClient).deleteCustomer(postCaptor.capture());
        assertEquals(customer.getCustomerId(), postCaptor.getValue().intValue());
    }


    @Test
    public void updateCustomer() {

        Customer customer = new Customer();
        customer.setFirstName("Bernie");
        customer.setLastName("Sanders");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Hollis");
        customer.setZip("11423");
        customer.setEmail("bernie@gmail.com"); // instead of yahoo it's gmail
        customer.setPhone("724-879-9234");

        customerService.updateCustomer(customer.getCustomerId(), customer);
        ArgumentCaptor<Customer> postCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerClient).updateCustomer(any(Integer.class), postCaptor.capture());
        assertEquals(customer.getEmail(), postCaptor.getValue().getEmail());

    }


    @Test
    public void getCustomerWithNonExistentId() {
        Customer customer = customerService.getCustomer(500);
        assertNull(customer);
    }


    public void setUpCustomerClientMock() {

        Customer customer = new Customer();
        customer.setFirstName("Bernie");
        customer.setLastName("Sanders");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Hollis");
        customer.setZip("11423");
        customer.setEmail("bernie@yahoo.com");
        customer.setPhone("724-879-9234");

        Customer customer2 = new Customer();
        customer2.setCustomerId(1);
        customer2.setFirstName("Bernie");
        customer2.setLastName("Sanders");
        customer2.setStreet("2380 W US Hwy 89");
        customer2.setCity("Hollis");
        customer2.setZip("11423");
        customer2.setEmail("bernie@yahoo.com");
        customer2.setPhone("724-879-9234");

        Customer customer3 = new Customer();
        customer3.setFirstName("Will");
        customer3.setLastName("Smith");
        customer3.setStreet("1000 Sturdivant Street");
        customer3.setCity("NY");
        customer3.setZip("11455");
        customer3.setEmail("smith@gmail.com");
        customer3.setPhone("919-374-2901");

        Customer customer4 = new Customer();
        customer4.setCustomerId(2);
        customer4.setFirstName("Will");
        customer4.setLastName("Smith");
        customer4.setStreet("1000 Sturdivant Street");
        customer4.setCity("NY");
        customer4.setZip("11455");
        customer4.setEmail("smith@gmail.com");
        customer4.setPhone("919-374-2901");

        doReturn(customer2).when(customerClient).addCustomer(customer);
        doReturn(customer4).when(customerClient).addCustomer(customer3);
        doReturn(customer2).when(customerClient).getCustomer(1);
        doReturn(customer4).when(customerClient).getCustomer(2);

        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer2);
        customerList.add(customer4);

        doReturn(customerList).when(customerClient).getAllCustomers();

    }

}
