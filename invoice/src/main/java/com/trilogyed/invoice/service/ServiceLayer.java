package com.trilogyed.invoice.service;

import com.trilogyed.invoice.dao.InvoiceDao;
import com.trilogyed.invoice.dao.InvoiceItemDao;
import com.trilogyed.invoice.model.Invoice;
import com.trilogyed.invoice.model.InvoiceItem;
import com.trilogyed.invoice.viewModel.OrderViewModel;
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
    public OrderViewModel addInvoice(OrderViewModel ovm) {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(ovm.getCustomerId());
        invoice.setPurchaseDate(ovm.getPurchaseDate());
        invoice = invoiceDao.addInvoice(invoice);

        if (ovm.getInvoiceItems() == null) {
            ovm.setInvoiceItems(new ArrayList<>());
        }
        for(InvoiceItem item: ovm.getInvoiceItems()){
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setQuantity(item.getQuantity());
            invoiceItem.setInvoiceId(invoice.getInvoiceId());
            invoiceItem.setUnitPrice(item.getUnitPrice());
            invoiceItem.setInventoryId(item.getInventoryId());
            invoiceItemDao.addInvoiceItem(invoiceItem);
        }
        return buildOrderViewModel(invoice);
    }

    public OrderViewModel getInvoice(int id) {
        Invoice invoice = invoiceDao.getInvoice(id);
        if(invoice == null )
            return null;
        else
            return buildOrderViewModel(invoice);
    }

    public void updateInvoice(OrderViewModel ovm, int id) {

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(id);
        invoice.setCustomerId(ovm.getCustomerId());
        invoice.setPurchaseDate(ovm.getPurchaseDate());
        invoiceDao.updateInvoice(invoice);

        invoiceItemDao.getInvoiceItemsByInvoiceId(id)
                .forEach(ii -> invoiceItemDao.deleteInvoiceItem(ii.getInvoiceItemId()));
        for(InvoiceItem item: ovm.getInvoiceItems()){
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setQuantity(item.getQuantity());
            invoiceItem.setInvoiceId(invoice.getInvoiceId());
            invoiceItem.setUnitPrice(item.getUnitPrice());
            invoiceItem.setInventoryId(item.getInventoryId());
            invoiceItemDao.addInvoiceItem(invoiceItem);
        }
    }

    public void deleteInvoice(int id) {
        invoiceItemDao.getInvoiceItemsByInvoiceId(id)
                .forEach(item -> invoiceItemDao.deleteInvoiceItem(item.getInvoiceItemId()));
        invoiceDao.deleteInvoice(id);
    }

    public List<OrderViewModel> getAllInvoices() {
        List<OrderViewModel> ovm = new ArrayList<>();
        for (Invoice i : invoiceDao.getAllInvoices()) {
            ovm.add(buildOrderViewModel(i));
        }
        return ovm;
    }


    public List<OrderViewModel> getInvoicesByCustomerId(int id) {
        List<OrderViewModel> ovm = new ArrayList<>();
        for (Invoice i : invoiceDao.getInvoicesByCustomerId(id)) {
            ovm.add(buildOrderViewModel(i));
        }
        return ovm;
    }

    private OrderViewModel buildOrderViewModel(Invoice invoice) {
        OrderViewModel ovm = new OrderViewModel();
        ovm.setInvoiceId(invoice.getInvoiceId());
        ovm.setCustomerId(invoice.getCustomerId());
        ovm.setPurchaseDate(invoice.getPurchaseDate());
        ovm.setInvoiceItems(invoiceItemDao.getInvoiceItemsByInvoiceId(invoice.getInvoiceId()));
        return ovm;
    }


}
