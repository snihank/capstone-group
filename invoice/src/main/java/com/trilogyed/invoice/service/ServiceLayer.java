package com.trilogyed.invoice.service;

import com.trilogyed.invoice.dao.InvoiceDao;
import com.trilogyed.invoice.dao.InvoiceItemDao;
import com.trilogyed.invoice.model.Invoice;
import com.trilogyed.invoice.model.InvoiceItem;
import com.trilogyed.invoice.model.InvoiceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceLayer {

    InvoiceDao invoiceDao;
    InvoiceItemDao invoiceItemDao;

    @Autowired
    public ServiceLayer(InvoiceDao invoiceDao, InvoiceItemDao invoiceItemDao) {
        this.invoiceDao = invoiceDao;
        this.invoiceItemDao = invoiceItemDao;
    }

    @Transactional
    public InvoiceViewModel addInvoice(InvoiceViewModel ivm) {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(ivm.getCustomerId());
        invoice.setPurchaseDate(ivm.getPurchaseDate());
        invoice = invoiceDao.addInvoice(invoice);

        if (ivm.getInvoiceItems() == null) {
            ivm.setInvoiceItems(new ArrayList<>());
        }
        for(InvoiceItem ii: ivm.getInvoiceItems()){
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setQuantity(ii.getQuantity());
            invoiceItem.setInvoiceId(invoice.getInvoiceId());
            invoiceItem.setUnitPrice(ii.getUnitPrice());
            invoiceItem.setInventoryId(ii.getInventoryId());
            invoiceItemDao.addInvoiceItem(invoiceItem);
        }
        return buildInvoiceViewModel(invoice);
    }


    public InvoiceViewModel getInvoice(int id) {

        Invoice invoice = invoiceDao.getInvoice(id);
        if(invoice == null )
            return null;
        else
            return buildInvoiceViewModel(invoice);
    }


    public void updateInvoice(InvoiceViewModel ivm, int id) {


        Invoice invoice = new Invoice();
        invoice.setInvoiceId(id);
        invoice.setCustomerId(ivm.getCustomerId());
        invoice.setPurchaseDate(ivm.getPurchaseDate());
        invoiceDao.updateInvoice(invoice);


        invoiceItemDao.getInvoiceItemsByInvoiceId(id)
                .forEach(ii -> invoiceItemDao.deleteInvoiceItem(ii.getInvoiceItemId()));
        for(InvoiceItem ii: ivm.getInvoiceItems()){
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setQuantity(ii.getQuantity());
            invoiceItem.setInvoiceId(invoice.getInvoiceId());
            invoiceItem.setUnitPrice(ii.getUnitPrice());
            invoiceItem.setInventoryId(ii.getInventoryId());
            invoiceItemDao.addInvoiceItem(invoiceItem);
        }
    }


    public void deleteInvoice(int id) {

        invoiceItemDao.getInvoiceItemsByInvoiceId(id)
                .forEach(ii -> invoiceItemDao.deleteInvoiceItem(ii.getInvoiceItemId()));
        invoiceDao.deleteInvoice(id);
    }


    public List<InvoiceViewModel> getAllInvoices() {
        List<InvoiceViewModel> ivms = new ArrayList<>();
        for (Invoice i : invoiceDao.getAllInvoices()) {
            ivms.add(buildInvoiceViewModel(i));
        }
        return ivms;
    }

    public List<InvoiceViewModel> getInvoicesByCustomerId(int id) {
        List<InvoiceViewModel> ivms = new ArrayList<>();
        for (Invoice i : invoiceDao.getInvoicesByCustomerId(id)) {
            ivms.add(buildInvoiceViewModel(i));
        }
        return ivms;
    }

    private InvoiceViewModel buildInvoiceViewModel(Invoice invoice) {
        InvoiceViewModel ivm = new InvoiceViewModel();
        ivm.setInvoiceId(invoice.getInvoiceId());
        ivm.setCustomerId(invoice.getCustomerId());
        ivm.setPurchaseDate(invoice.getPurchaseDate());
        ivm.setInvoiceItems(invoiceItemDao.getInvoiceItemsByInvoiceId(invoice.getInvoiceId()));
        return ivm;
    }
}
