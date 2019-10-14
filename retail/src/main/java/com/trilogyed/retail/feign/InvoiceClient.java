package com.trilogyed.retail.feign;

import com.trilogyed.retail.model.OrderViewModel;
import com.trilogyed.retail.viewModel.InvoiceViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "invoice-service")
public interface InvoiceClient {
    // add invoice
    @PostMapping(value = "/invoice")
    OrderViewModel addInvoice(@RequestBody OrderViewModel ivm);

    // get all invoices
    @GetMapping(value = "/invoice")
    List<OrderViewModel> getAllInvoices();

    // get invoice
    @GetMapping(value = "/invoice/{id}")
    OrderViewModel getInvoice(@PathVariable("id") int id);

    // get invoices by customer id
    @GetMapping(value = "/invoice/customer/{id}")
    List<OrderViewModel> getInvoiceByCustomerId(@PathVariable("id") int id);

}
