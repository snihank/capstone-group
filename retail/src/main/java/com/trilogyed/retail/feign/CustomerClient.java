package com.trilogyed.retail.feign;


import com.trilogyed.retail.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerClient {
    @GetMapping(value = "/customers/{id}")
    Customer getCustomer(@PathVariable("id") int id);
}
