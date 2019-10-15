package com.trilogyed.invoice.dao;

import com.trilogyed.invoice.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class InvoiceDaoJdbcTemplateImpl implements InvoiceDao{



    private JdbcTemplate jdbc;


    private static final String INSERT_INVOICE_SQL = "insert into invoice (customer_id, purchase_date) values(?,?)";
    private static final String SELECT_INVOICE_SQL = "select * from invoice where invoice_id=?";
    private static final String SELECT_ALL_INVOICES_SQL = "select * from invoice";
    private static final String SELECT_INVOICES_BY_CUSTOMER_ID_SQL = "select * from invoice where customer_id=?";
    private static final String UPDATE_INVOICE_SQL = "update invoice set customer_id=?, purchase_date=? where invoice_id=?";
    private static final String DELETE_INVOICE_SQL = "delete from invoice where invoice_id=?";


    @Autowired
    public InvoiceDaoJdbcTemplateImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    public Invoice addInvoice(Invoice invoice) {
        invoice.setPurchaseDate(LocalDate.now());
        jdbc.update(
                INSERT_INVOICE_SQL,
                invoice.getCustomerId(),
                invoice.getPurchaseDate()
        );
        invoice.setInvoiceId(jdbc.queryForObject("select LAST_INSERT_ID()", Integer.class));
        return invoice;
    }

    @Override
    public Invoice getInvoice(int id) {
        try {
            return jdbc.queryForObject(SELECT_INVOICE_SQL, this::mapRowToInvoice, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updateInvoice(Invoice invoice) {
        jdbc.update(
                UPDATE_INVOICE_SQL,
                invoice.getCustomerId(),
                invoice.getPurchaseDate(),
                invoice.getInvoiceId()
        );
    }

    @Override
    public void deleteInvoice(int id) {
        jdbc.update(DELETE_INVOICE_SQL, id);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return jdbc.query(SELECT_ALL_INVOICES_SQL, this::mapRowToInvoice);
    }

    @Override
    public List<Invoice> getInvoicesByCustomerId(int customerId) {
        return jdbc.query(SELECT_INVOICES_BY_CUSTOMER_ID_SQL, this::mapRowToInvoice, customerId);
    }

    private Invoice mapRowToInvoice(ResultSet rs, int rowNum) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("invoice_id"));
        invoice.setCustomerId(rs.getInt("customer_id"));
        invoice.setPurchaseDate(rs.getDate("purchase_date").toLocalDate());
        return invoice;
    }
}
