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
import java.time.LocalDate;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceDaoJdbcTemplateImplTest {

    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private InvoiceItemDao invoiceItemDao;

    @Before
    public void setUp() throws Exception {
        invoiceItemDao.getAllInvoiceItems()
                .stream()
                .forEach(ii -> invoiceItemDao.deleteInvoiceItem(ii.getInvoiceItemId()));
        invoiceDao.getAllInvoices()
                .stream()
                .forEach(i->invoiceDao.deleteInvoice(i.getInvoiceId()));
    }

    @Test
    public void addGetDeleteInvoice() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);

        invoice = invoiceDao.addInvoice(invoice);
        Invoice invoice1 = invoiceDao.getInvoice(invoice.getInvoiceId());
        assertEquals(invoice, invoice1);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(10);
        invoiceItem.setUnitPrice(new BigDecimal("5.00"));
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);

        invoiceItemDao.deleteInvoiceItem(invoiceItem.getInvoiceItemId());
        invoiceDao.deleteInvoice(invoice.getInvoiceId());
        assertEquals(0, invoiceDao.getAllInvoices().size());
    }

    @Test
    public void updateInvoice() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice = invoiceDao.addInvoice(invoice);

        invoice.setPurchaseDate(LocalDate.of(2019, 10, 13));
        invoiceDao.updateInvoice(invoice);
        assertEquals(LocalDate.of(2019, 10, 13), invoiceDao.getInvoice(invoice.getInvoiceId()).getPurchaseDate());
    }

    @Test
    public void getAllInvoices() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);


        invoiceDao.addInvoice(invoice);
        invoiceDao.addInvoice(invoice);

        assertEquals(2, invoiceDao.getAllInvoices().size());
    }

    @Test
    public void getInvoicesByCustomerId() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoiceDao.addInvoice(invoice);

        Invoice invoice2 = new Invoice();
        invoice2.setCustomerId(1);
        invoiceDao.addInvoice(invoice2);

        Invoice invoice3 = new Invoice();
        invoice2.setCustomerId(2);
        invoiceDao.addInvoice(invoice2);

        assertEquals(3, invoiceDao.getAllInvoices().size());

        assertEquals(2, invoiceDao.getInvoicesByCustomerId(1).size());

    }
}