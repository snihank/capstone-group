package com.trilogyed.invoice.dao;

import com.trilogyed.invoice.model.Invoice;
import com.trilogyed.invoice.model.InvoiceItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceItemDaoJdbcTemplateImplTest {

    @Autowired
    private InvoiceItemDao invoiceItemDao;
    @Autowired
    private InvoiceDao invoiceDao;

    @Before
    public void setUp() throws Exception {
        invoiceItemDao.getAllInvoiceItems()
                .stream()
                .forEach(ii -> invoiceItemDao.deleteInvoiceItem(ii.getInvoiceItemId()));
        invoiceDao.getAllInvoices()
                .stream()
                .forEach(i -> invoiceDao.deleteInvoice(i.getInvoiceId()));
    }

    @Test
    public void addGetDeleteInvoiceItem() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice = invoiceDao.addInvoice(invoice);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(10);
        invoiceItem.setUnitPrice(new BigDecimal("5.00"));

        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);

        InvoiceItem invoiceItem1 = invoiceItemDao.getInvoiceItem(invoiceItem.getInvoiceItemId());
        assertEquals(invoiceItem, invoiceItem1);

        invoiceItemDao.deleteInvoiceItem(invoiceItem.getInvoiceItemId());
        assertEquals(0, invoiceItemDao.getAllInvoiceItems().size());
    }

    @Test
    public void updateInvoiceItem() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice = invoiceDao.addInvoice(invoice);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(10);
        invoiceItem.setUnitPrice(new BigDecimal("5.00"));

        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);

        invoiceItem.setQuantity(15);
        invoiceItemDao.updateInvoiceItem(invoiceItem);
        assertEquals(15, (int) invoiceItemDao.getInvoiceItem(invoiceItem.getInvoiceItemId()).getQuantity());

    }

    @Test
    public void getAllInvoiceItems() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice = invoiceDao.addInvoice(invoice);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(10);
        invoiceItem.setUnitPrice(new BigDecimal("5.00"));
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);
        invoiceItemDao.addInvoiceItem(invoiceItem);

        assertEquals(3, invoiceItemDao.getAllInvoiceItems().size());
    }

    @Test
    public void getInvoiceItemsByInvoiceId() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice = invoiceDao.addInvoice(invoice);

        Invoice invoice2 = new Invoice();
        invoice2.setCustomerId(1);
        invoice2 = invoiceDao.addInvoice(invoice2);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(10);
        invoiceItem.setUnitPrice(new BigDecimal("5.00"));
        invoiceItemDao.addInvoiceItem(invoiceItem);

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceId(invoice2.getInvoiceId());
        invoiceItem2.setInventoryId(1);
        invoiceItem2.setQuantity(10);
        invoiceItem2.setUnitPrice(new BigDecimal("5.00"));
        invoiceItemDao.addInvoiceItem(invoiceItem2);

        assertEquals(1, invoiceItemDao.getInvoiceItemsByInvoiceId(invoice2.getInvoiceId()).size());

        assertEquals(2, invoiceItemDao.getAllInvoiceItems().size());
    }

}