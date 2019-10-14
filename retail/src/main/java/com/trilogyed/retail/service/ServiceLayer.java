package com.trilogyed.retail.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.trilogyed.retail.exception.InsufficientQuantityException;
import com.trilogyed.retail.exception.NotFoundException;
import com.trilogyed.retail.feign.*;
import com.trilogyed.retail.model.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceLayer {

    private static final String EXCHANGE = "levelup-exchange";
    private static final String ROUTING_KEY = "levelup.create.retail.service";

    private RabbitTemplate rabbitTemplate;
    private CustomerClient customerClient;
    private InventoryClient inventoryClient;
    private LevelUpClient levelUpClient;
    private InvoiceClient invoiceClient;
    private ProductClient productClient;

    private final RestTemplate restTemplate;

    @Autowired
    public ServiceLayer(
            RabbitTemplate rabbitTemplate, CustomerClient customerClient,
            InventoryClient inventoryClient, LevelUpClient levelUpClient,
            InvoiceClient invoiceClient, ProductClient productClient, RestTemplate restTemplate) {
        this.customerClient = customerClient;
        this.inventoryClient = inventoryClient;
        this.invoiceClient = invoiceClient;
        this.levelUpClient = levelUpClient;
        this.productClient = productClient;
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public OrderViewModelResponse addInvoice(OrderViewModel ovm) {

        if (customerClient.getCustomer(ovm.getCustomerId()) == null) {
            throw new IllegalArgumentException("Customer does not exist");
        }

        checkStock(ovm);

        OrderViewModelResponse ovmr = new OrderViewModelResponse();
        ovmr.setCustomerId(ovm.getCustomerId());
        ovmr.setInvoiceItems(ovm.getInvoiceItems());

        OrderViewModel o = invoiceClient.addInvoice(ovm);

        ovmr.setInvoiceId(o.getInvoiceId());
        ovmr.setPurchaseDate(o.getPurchaseDate());

        List<InvoiceItem> invoiceItems = ovmr.getInvoiceItems();
        for (int i = 0; i < ovmr.getInvoiceItems().size(); i++) {
            invoiceItems.get(i).setInvoiceId(o.getInvoiceId());
            invoiceItems.get(i).setInvoiceItemId(o.getInvoiceItems().get(i).getInvoiceItemId());
        }

        Integer points = calculatePoints(ovm);
        ovmr.setPoints(points);

        return ovmr;
    }

    public void checkStock(OrderViewModel ovm) {

        ovm.getInvoiceItems().forEach(ii -> {

            Inventory inventory = inventoryClient.getInventory(ii.getInventoryId());

            if (inventory == null) {
                throw new IllegalArgumentException("Inventory id " + ii.getInventoryId() + " is not valid");
            }

            int quantityInStock = inventory.getQuantity();

            if (ii.getQuantity() > quantityInStock || ii.getQuantity() < 0) {
                throw new InsufficientQuantityException("Insufficient Stock");
            }

            if (productClient.getProduct(inventory.getProductId()) == null) {
                throw new IllegalArgumentException("No Product with this id: " + inventory.getProductId());
            }

        });

    }

    public Integer calculatePoints(OrderViewModel ovm) {

        BigDecimal invoiceTotal = new BigDecimal("0");
        for (InvoiceItem ii : ovm.getInvoiceItems()) {
            BigDecimal itemTotal = ii.getUnitPrice().multiply(new BigDecimal(ii.getQuantity()));
            invoiceTotal = invoiceTotal.add(itemTotal);
        }

        Integer pointsEarned =  invoiceTotal.divide(new BigDecimal("50")
                .setScale(0, BigDecimal.ROUND_FLOOR), BigDecimal.ROUND_FLOOR).intValue();

        Integer previousPoints = getPoints(ovm.getCustomerId());

        int totalPoints = pointsEarned;

        if (previousPoints != null) {
            totalPoints += previousPoints;
        }

        if (previousPoints == null) {
            LevelUp levelUp = new LevelUp(ovm.getCustomerId(), pointsEarned, LocalDate.now());
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, levelUp);
        }

        else {
            LevelUp levelUp = new LevelUp(
                    levelUpClient.getLevelUpByCustomerId(ovm.getCustomerId()).getLevelUpId(),
                    ovm.getCustomerId(),
                    totalPoints,
                    LocalDate.now());
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, levelUp);
        }

        return  totalPoints;

    }

    @HystrixCommand(fallbackMethod = "fallBack")
    public Integer getPoints(int customerId) {
        if (levelUpClient.getLevelUpByCustomerId(customerId) == null) {
            return null;
        } else {
            return levelUpClient.getLevelUpByCustomerId(customerId).getPoints();
        }
    }

    public Integer fallBack(int customerId){
        throw new NotFoundException("LevelUp could not be retrieved for customer id " + customerId);
    }


    public OrderViewModel getInvoice(int id) {
        return invoiceClient.getInvoice(id);
    }

    public List<OrderViewModel> getAllInvoices() {
        List<OrderViewModel> invoices = invoiceClient.getAllInvoices();
        return invoices;
    }

    public List<OrderViewModel> getInvoicesByCustomerId(int id) {
        return invoiceClient.getInvoiceByCustomerId(id);
    }

    public Product getProduct(int id) {
        return productClient.getProduct(id);
    }

    public List<Product> getProductsInInventory() {

        List<Product> products = new ArrayList<>();

        List<Inventory> inventory = inventoryClient.getAllInventory();

        inventory.forEach(ii ->
                products.add(productClient.getProduct(ii.getProductId())));

        return products;

    }

    public List<Product> getProductsByInvoiceId(int id) {

        List<Product> products = new ArrayList<>();

        OrderViewModel invoice = invoiceClient.getInvoice(id);

        invoice.getInvoiceItems().forEach(ii -> {

            Inventory inventory = inventoryClient.getInventory(ii.getInventoryId());

            products.add(productClient.getProduct(inventory.getProductId()));

        });

        return products;

    }

}
