package com.trilogyed.customer.dao;


import com.trilogyed.customer.exception.NotFoundException;
import com.trilogyed.customer.model.Customer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerDaoJdbcTemplateImpl implements CustomerDao {

    private JdbcTemplate jdbcTemplate;



    private static final String INSERT_CUSTOMER_SQL =
            "insert into customer (first_name, last_name, street, city, zip, email, phone) values (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_CUSTOMER_SQL =
            "select * from customer where customer_id = ?";

    private static final String UPDATE_CUSTOMER_SQL =
            "update customer set first_name = ?, last_name = ?, street = ?, city = ?, zip = ?, email = ?, " +
                    "phone = ? where customer_id = ?";

    private static final String DELETE_CUSTOMER_SQL =
            "delete from customer where customer_id = ?";

    private static final String SELECT_ALL_CUSTOMERS_SQL =
            "select * from customer";



    public CustomerDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private Customer mapRowToCustomer(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setStreet(rs.getString("street"));
        customer.setCity(rs.getString("city"));
        customer.setZip(rs.getString("zip"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));

        return customer;
    }



    @Override
    @Transactional
    public Customer addCustomer(Customer customer) {
        jdbcTemplate.update(
                INSERT_CUSTOMER_SQL,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getStreet(),
                customer.getCity(),
                customer.getZip(),
                customer.getEmail(),
                customer.getPhone()
        );

        int id = jdbcTemplate.queryForObject("select LAST_INSERT_ID()", Integer.class);

        customer.setCustomerId(id);

        return customer;
    }

    @Override
    public Customer getCustomer(int id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_CUSTOMER_SQL, this::mapRowToCustomer, id);
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }

    @Override
    @Transactional
    public void updateCustomer(Customer customer) {

        Customer customerInDB = getCustomer(customer.getCustomerId());
        if (customerInDB == null) {
            throw new IllegalArgumentException("The id provided does not exist.");
        }

        jdbcTemplate.update(
                UPDATE_CUSTOMER_SQL,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getStreet(),
                customer.getCity(),
                customer.getZip(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCustomerId()
        );
    }

    @Override
    @Transactional
    public void deleteCustomer(int id) {

        Customer customerInDB = getCustomer(id);
        if (customerInDB == null) {
            throw new NotFoundException("The id provided does not exist.");
        }

        jdbcTemplate.update(DELETE_CUSTOMER_SQL, id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return jdbcTemplate.query(SELECT_ALL_CUSTOMERS_SQL, this::mapRowToCustomer);
    }

}
