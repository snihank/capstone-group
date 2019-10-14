package com.trilogyed.invoice.dao;

import com.trilogyed.invoice.model.Invoice;

import java.util.List;

public interface InvoiceDao {

    Invoice addInvoice(Invoice invoice);

    Invoice getInvoice(int id);

    void updateInvoice(Invoice invoice);

    void deleteInvoice(int id);

    List<Invoice> getAllInvoices();

    List<Invoice> getInvoicesByCustomerId(int customerId);

}
