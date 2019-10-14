package com.trilogyed.invoice.dao;

import com.trilogyed.invoice.model.InvoiceItem;

import java.util.List;

public interface InvoiceItemDao {

    InvoiceItem addInvoiceItem(InvoiceItem invoiceItem);

    InvoiceItem getInvoiceItem(int id);

    void updateInvoiceItem(InvoiceItem invoiceItem);

    void deleteInvoiceItem(int id);

    List<InvoiceItem> getAllInvoiceItems();

    List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId);
}
