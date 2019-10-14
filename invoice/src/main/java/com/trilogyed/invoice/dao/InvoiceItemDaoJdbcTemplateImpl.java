package com.trilogyed.invoice.dao;

import com.trilogyed.invoice.exception.NotFoundException;
import com.trilogyed.invoice.model.InvoiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InvoiceItemDaoJdbcTemplateImpl implements InvoiceItemDao{


    private JdbcTemplate jdbc;


    private static final String INSERT_INVOICE_ITEM =
            "insert into invoice_item (invoice_id, inventory_id, quantity, unit_price) values(?,?,?,?)";
    private static final String SELECT_INVOICE_ITEM =
            "select * from invoice_item where invoice_item_id=?";
    private static final String SELECT_ALL_INVOICE_ITEMS =
            "select * from invoice_item";
    private static final String SELECT_INVOICE_ITEMS_BY_INVOICE_ID =
            "select * from invoice_item where invoice_id=?";
    private static final String UPDATE_INVOICE =
            "update invoice_item set invoice_id=?, inventory_id=?, quantity=?, unit_price=? where invoice_item_id=?";
    private static final String DELETE_INVOICE_ITEM =
            "delete from invoice_item where invoice_item_id=?";


    @Autowired
    public InvoiceItemDaoJdbcTemplateImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    @Transactional
    public InvoiceItem addInvoiceItem(InvoiceItem invoiceItem) {
        jdbc.update(
                INSERT_INVOICE_ITEM,
                invoiceItem.getInvoiceId(),
                invoiceItem.getInventoryId(),
                invoiceItem.getQuantity(),
                invoiceItem.getUnitPrice()
        );
        invoiceItem.setInvoiceItemId(jdbc.queryForObject("select LAST_INSERT_ID()", Integer.class));
        return invoiceItem;
    }

    @Override
    public InvoiceItem getInvoiceItem(int id) {
        try {
            return jdbc.queryForObject(SELECT_INVOICE_ITEM, this::mapRowToInvoiceItem, id);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    @Transactional
    public void updateInvoiceItem(InvoiceItem invoiceItem) {
        if(getInvoiceItem(invoiceItem.getInvoiceItemId())== null){
            throw new IllegalArgumentException("Invoice not found");
        }
        jdbc.update(
                UPDATE_INVOICE,
                invoiceItem.getInvoiceId(),
                invoiceItem.getInvoiceItemId(),
                invoiceItem.getQuantity(),
                invoiceItem.getUnitPrice(),
                invoiceItem.getInvoiceItemId()
        );
    }

    @Override
    @Transactional
    public void deleteInvoiceItem(int id) {
        if(getInvoiceItem(id)== null){
            throw new NotFoundException("Invoice not found");
        }
        jdbc.update(DELETE_INVOICE_ITEM, id);
    }

    @Override
    public List<InvoiceItem> getAllInvoiceItems() {
        return jdbc.query(SELECT_ALL_INVOICE_ITEMS, this::mapRowToInvoiceItem);

    }

    @Override
    public List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId) {
        return jdbc.query(SELECT_INVOICE_ITEMS_BY_INVOICE_ID, this::mapRowToInvoiceItem, invoiceId);

    }

    private InvoiceItem mapRowToInvoiceItem(ResultSet rs, int rowNum) throws SQLException {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(rs.getInt("invoice_item_id"));
        invoiceItem.setInvoiceId(rs.getInt("invoice_id"));
        invoiceItem.setInventoryId(rs.getInt("inventory_id"));
        invoiceItem.setQuantity(rs.getInt("quantity"));
        invoiceItem.setUnitPrice(rs.getBigDecimal("unit_price"));
        return invoiceItem;
    }
}

