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
public class RetailApiService {

    private static final String EXCHANGE = "levelup-exchange";
    private static final String ROUTING_KEY = "levelup.create.retail.service";

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private CustomerClient customerClient;
    @Autowired
    private InventoryClient inventoryClient;
    @Autowired
    private LevelUpClient levelUpClient;
    @Autowired
    private InvoiceClient invoiceClient;
    @Autowired
    private ProductClient productClient;

    private final RestTemplate restTemplate;

    public RetailApiService(
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
    public InvoiceViewModelResponse addInvoice(InvoiceViewModel ivm) {

        try{
            checkIfCustomer(ivm);
        }catch(RuntimeException e){
            throw new IllegalArgumentException("NO customer found for id " + ivm.getCustomerId());
        }

        validateInvoiceItems(ivm);

        InvoiceViewModelResponse ivmRes = new InvoiceViewModelResponse();
        ivmRes.setCustomerId(ivm.getCustomerId());
        ivmRes.setInvoiceItems(ivm.getInvoiceItems());

        InvoiceViewModel invoiceVM = invoiceClient.addInvoice(ivm);

        ivmRes.setInvoiceId(invoiceVM.getInvoiceId());
        ivmRes.setPurchaseDate(invoiceVM.getPurchaseDate());

        List<InvoiceItem> invoiceItems = ivmRes.getInvoiceItems();
        for (int i = 0; i < ivmRes.getInvoiceItems().size(); i++) {
            invoiceItems.get(i).setInvoiceId(invoiceVM.getInvoiceId());
            invoiceItems.get(i).setInvoiceItemId(invoiceVM.getInvoiceItems().get(i).getInvoiceItemId());
        }

        Integer points = calculatePoints(ivm);

        ivmRes.setPoints(points);

        return ivmRes;
    }


    /**
     * Checks for an existing customer
     * @param ivm InvoiceViewModel
     */
    public void checkIfCustomer(InvoiceViewModel ivm) {
        if (customerClient.getCustomer(ivm.getCustomerId()) == null) {
            throw new IllegalArgumentException("There is no customer matching the given id");
        }
    }

    /**
     * Handle invoice item validation
     * @param ivm InvoiceViewModel
     */
    public void validateInvoiceItems(InvoiceViewModel ivm) {

        ivm.getInvoiceItems().forEach(ii -> {

            Inventory inventory = inventoryClient.getInventory(ii.getInventoryId());
            if (inventory == null) {
                throw new IllegalArgumentException("Inventory id " + ii.getInventoryId() + " is not valid");
            }

            int quantityInStock = inventory.getQuantity();

            if (ii.getQuantity() > quantityInStock || ii.getQuantity() < 0) {
                throw new InsufficientQuantityException("We do not have that quantity.");
            }

            // throw exception if item does not exist
            if (productClient.getProduct(inventory.getProductId()) == null) {
                throw new IllegalArgumentException("Product " + inventory.getProductId() + " does not exist");
            }

        });

    }

    /**
     * Sends info to queue, gets info from circuit breaker
     * @param ivm
     * @return
     */
    public Integer calculatePoints(InvoiceViewModel ivm) {

        BigDecimal invoiceTotal = new BigDecimal("0");
        for (InvoiceItem ii : ivm.getInvoiceItems()) {
            BigDecimal itemTotal = ii.getUnitPrice().multiply(new BigDecimal(ii.getQuantity()));
            invoiceTotal = invoiceTotal.add(itemTotal);
        }


        Integer pointsEarned =  invoiceTotal.divide(new BigDecimal("50")
                .setScale(0, BigDecimal.ROUND_FLOOR), BigDecimal.ROUND_FLOOR).intValue();


        Integer previousPoints = getPoints(ivm.getCustomerId());

        int totalPoints = pointsEarned;

        if (previousPoints != null) {
            totalPoints += previousPoints;
        }

        if (previousPoints == null) {
            LevelUp levelUp = new LevelUp(ivm.getCustomerId(), pointsEarned, LocalDate.now());
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, levelUp);
        }

        else {
            LevelUp levelUp = new LevelUp(
                    levelUpClient.getLevelUpByCustomerId(ivm.getCustomerId()).getLevelUpId(),
                    ivm.getCustomerId(),
                    totalPoints,
                    LocalDate.now());
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, levelUp);
        }

        return  totalPoints;

    }

    @HystrixCommand(fallbackMethod = "getPointsFallback")
    public Integer getPoints(int customerId) {
        if (levelUpClient.getLevelUpByCustomerId(customerId) == null) {
            return null;
        } else {
            return levelUpClient.getLevelUpByCustomerId(customerId).getPoints();
        }
    }


    public Integer getPointsFallback(int customerId){
        throw new NotFoundException("LevelUp could not be retrieved for customer id " + customerId);
    }


    public InvoiceViewModel getInvoice(int id) {
        return invoiceClient.getInvoice(id);
    }

    public List<InvoiceViewModel> getAllInvoices() {
        List<InvoiceViewModel> invoices = invoiceClient.getAllInvoices();
        return invoiceClient.getAllInvoices();
    }

    public List<InvoiceViewModel> getInvoicesByCustomerId(int id) {
        return invoiceClient.getInvoicesByCustomerId(id);
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

        InvoiceViewModel invoice = invoiceClient.getInvoice(id);

        invoice.getInvoiceItems().forEach(ii -> {

            Inventory inventory = inventoryClient.getInventory(ii.getInventoryId());

            products.add(productClient.getProduct(inventory.getProductId()));

        });

        return products;

    }

}
