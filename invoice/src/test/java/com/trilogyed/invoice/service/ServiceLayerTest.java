package com.trilogyed.invoice.service;

import com.trilogyed.invoice.dao.InvoiceDao;
import com.trilogyed.invoice.dao.InvoiceDaoJdbcTemplateImpl;
import com.trilogyed.invoice.dao.InvoiceItemDao;
import com.trilogyed.invoice.dao.InvoiceItemDaoJdbcTemplateImpl;
import com.trilogyed.invoice.model.Invoice;
import com.trilogyed.invoice.model.InvoiceItem;
import com.trilogyed.invoice.viewModel.OrderViewModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

public class ServiceLayerTest {


    ServiceLayer service;
    InvoiceDao invoiceDao;
    InvoiceItemDao invoiceItemDao;

    @Before
    public void setUp() throws Exception {

        setUpInvoiceMock();
        setUpInvoiceItemMock();

        service = new ServiceLayer(invoiceDao, invoiceItemDao);

    }


    @Test
    public void addGetInvoice() {

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        OrderViewModel order = new OrderViewModel();
        order.setCustomerId(1);
        order.setPurchaseDate(LocalDate.of(2019, 7, 15));
        order.setInvoiceItems(invoiceItems);

        order = service.addInvoice(order);

        OrderViewModel o1 = service.getInvoice(order.getInvoiceId());

        assertEquals(order, o1);
    }


    @Test
    public void getAllInvoices() {

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        OrderViewModel invoiceVM = new OrderViewModel();
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);

        service.addInvoice(invoiceVM);

        invoiceVM = new OrderViewModel();
        invoiceVM.setCustomerId(2);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 5, 10));

        service.addInvoice(invoiceVM);

        List<OrderViewModel> fromService = service.getAllInvoices();

        assertEquals(2, fromService.size());

    }

    @Test
    public void deleteInvoice() {
        OrderViewModel order = service.getInvoice(1);
        service.deleteInvoice(1);
        ArgumentCaptor<Integer> postCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(invoiceDao).deleteInvoice(postCaptor.capture());
        assertEquals(order.getInvoiceId(), postCaptor.getValue().intValue());
    }

    @Test
    public void updateInvoice() {

        OrderViewModel order = new OrderViewModel();
        order.setInvoiceId(2);
        order.setCustomerId(3);
        order.setPurchaseDate(LocalDate.of(2019, 5, 10));
        order.setInvoiceItems(new ArrayList<>());

        service.updateInvoice(order, order.getInvoiceId());
        ArgumentCaptor<Invoice> postCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceDao).updateInvoice(postCaptor.capture());
        assertEquals(java.util.Optional.ofNullable(order.getCustomerId()), java.util.Optional.ofNullable(postCaptor.getValue().getCustomerId()));

    }

    @Test
    public void updateInvoiceItems() {

        OrderViewModel order = new OrderViewModel();
        order.setInvoiceId(2);
        order.setCustomerId(3);
        order.setPurchaseDate(LocalDate.of(2019, 5, 10));

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        List<InvoiceItem> itemList = new ArrayList<>();
        itemList.add(invoiceItem);

        order.setInvoiceItems(itemList);

        service.updateInvoice(order, order.getInvoiceId());
        ArgumentCaptor<Invoice> postCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceDao).updateInvoice(postCaptor.capture());
        assertEquals(java.util.Optional.ofNullable(order.getCustomerId()), java.util.Optional.ofNullable(postCaptor.getValue().getCustomerId()));

        verify(invoiceItemDao).getInvoiceItemsByInvoiceId(postCaptor.getValue().getInvoiceId());

    }

    @Test
    public void getInvoicesByCustomerId() {

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(1);
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        OrderViewModel order = new OrderViewModel();
        order.setInvoiceId(1);
        order.setCustomerId(1);
        order.setPurchaseDate(LocalDate.of(2019, 7, 15));
        order.setInvoiceItems(invoiceItems);

        OrderViewModel o2 = new OrderViewModel();
        o2.setInvoiceId(2);
        o2.setCustomerId(2);
        o2.setPurchaseDate(LocalDate.of(2019, 5, 10));
        o2.setInvoiceItems(new ArrayList<>());

        List<OrderViewModel> invoicesCust1 = new ArrayList<>();
        invoicesCust1.add(order);

        List<OrderViewModel> invoicesCust2 = new ArrayList<>();
        invoicesCust2.add(o2);

        List<OrderViewModel> invoicesForCust1FromService= service.getInvoicesByCustomerId(1);

        assertEquals(invoicesCust1, invoicesForCust1FromService);

        List<OrderViewModel> invoicesForCust2FromService= service.getInvoicesByCustomerId(2);

        assertEquals(invoicesCust2, invoicesForCust2FromService);
    }


    public void setUpInvoiceMock() {
        invoiceDao = mock(InvoiceDaoJdbcTemplateImpl.class);

        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.of(2019, 7, 15));

        Invoice invoice2 = new Invoice();
        invoice2.setInvoiceId(1);
        invoice2.setCustomerId(1);
        invoice2.setPurchaseDate(LocalDate.of(2019, 7, 15));

        Invoice invoice3 = new Invoice();
        invoice3.setCustomerId(2);
        invoice3.setPurchaseDate(LocalDate.of(2019, 5, 10));

        Invoice invoice4 = new Invoice();
        invoice4.setInvoiceId(2);
        invoice4.setCustomerId(2);
        invoice4.setPurchaseDate(LocalDate.of(2019, 5, 10));

        List<Invoice> invoiceList = new ArrayList<>();
        invoiceList.add(invoice2);
        invoiceList.add(invoice4);

        List<Invoice> invoiceListCust1 = new ArrayList<>();
        invoiceListCust1.add(invoice2);

        List<Invoice> invoiceListCust2 = new ArrayList<>();
        invoiceListCust2.add(invoice4);

        doReturn(invoice2).when(invoiceDao).addInvoice(invoice);
        doReturn(invoice4).when(invoiceDao).addInvoice(invoice3);

        doReturn(invoice2).when(invoiceDao).getInvoice(1);
        doReturn(invoice4).when(invoiceDao).getInvoice(2);

        doReturn(invoiceList).when(invoiceDao).getAllInvoices();

        doReturn(invoiceListCust1).when(invoiceDao).getInvoicesByCustomerId(1);
        doReturn(invoiceListCust2).when(invoiceDao).getInvoicesByCustomerId(2);
    }


    public void setUpInvoiceItemMock() {
        invoiceItemDao = mock(InvoiceItemDaoJdbcTemplateImpl.class);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceItemId(1);
        invoiceItem2.setInvoiceId(1);
        invoiceItem2.setInventoryId(5);
        invoiceItem2.setQuantity(2);
        invoiceItem2.setUnitPrice(new BigDecimal("29.99"));

        List<InvoiceItem> invoiceItemsOnInvoice1 = new ArrayList<>();
        invoiceItemsOnInvoice1.add(invoiceItem2);

        doReturn(invoiceItem2).when(invoiceItemDao).addInvoiceItem(invoiceItem);

        doReturn(invoiceItem2).when(invoiceItemDao).getInvoiceItem(1);

        doReturn(invoiceItemsOnInvoice1).when(invoiceItemDao).getInvoiceItemsByInvoiceId(1);

    }
}
