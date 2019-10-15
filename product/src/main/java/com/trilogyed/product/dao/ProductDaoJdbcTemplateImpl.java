package com.trilogyed.product.dao;


import com.trilogyed.product.exception.NotFoundException;
import com.trilogyed.product.model.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductDaoJdbcTemplateImpl implements ProductDao {

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_PRODUCT_SQL =
            "insert into product (product_name, product_description, list_price, unit_cost) values (?, ?, ?, ?)";

    private static final String SELECT_PRODUCT_SQL =
            "select * from product where product_id = ?";

    private static final String UPDATE_PRODUCT_SQL =
            "update product set product_name = ?, product_description = ?, list_price = ?, unit_cost = ?" +
                    "where product_id = ?";

    private static final String DELETE_PRODUCT_SQL =
            "delete from product where product_id = ?";

    private static final String SELECT_ALL_PRODUCTS_SQL =
            "select * from product";


    public ProductDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private Product mapRowToProduct(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));
        product.setProductDescription(rs.getString("product_description"));
        product.setListPrice(rs.getBigDecimal("list_price"));
        product.setUnitCost(rs.getBigDecimal("unit_cost"));

        return product;
    }


    @Override
    @Transactional
    public Product addProduct(Product product) {
        jdbcTemplate.update(
                INSERT_PRODUCT_SQL,
                product.getProductName(),
                product.getProductDescription(),
                product.getListPrice(),
                product.getUnitCost()
        );

        int id = jdbcTemplate.queryForObject("select LAST_INSERT_ID()", Integer.class);

        product.setProductId(id);

        return product;
    }

    @Override
    public Product getProduct(int id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_PRODUCT_SQL, this::mapRowToProduct, id);
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {

        Product productInDB = getProduct(product.getProductId());
        if (productInDB == null) {
            throw new IllegalArgumentException("The id provided does not exist.");
        }

        jdbcTemplate.update(
                UPDATE_PRODUCT_SQL,
                product.getProductName(),
                product.getProductDescription(),
                product.getListPrice(),
                product.getUnitCost(),
                product.getProductId()
        );
    }

    @Override
    @Transactional
    public void deleteProduct(int id) {

        Product productInDB = getProduct(id);
        if (productInDB == null) {
            throw new NotFoundException("The id provided does not exist.");
        }

        jdbcTemplate.update(DELETE_PRODUCT_SQL, id);
    }

    @Override
    public List<Product> getAllProducts() {
        return jdbcTemplate.query(SELECT_ALL_PRODUCTS_SQL, this::mapRowToProduct);
    }



}
