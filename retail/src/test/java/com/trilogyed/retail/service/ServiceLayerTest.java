package com.trilogyed.retail.service;

import com.trilogyed.retail.exception.InsufficientQuantityException;
import com.trilogyed.retail.exception.NotFoundException;
import com.trilogyed.retail.feign.*;
import com.trilogyed.retail.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class ServiceLayerTest {


    @InjectMocks
    private ServiceLayer service;

    @Mock
    private ProductClient productClient;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private InvoiceClient invoiceClient;

    @Mock
    private LevelUpClient levelUpClient;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    static final String EXCHANGE = "levelup-exchange";
    static final String ROUTING_KEY = "levelup.create.retail.service";

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        // configure mock objects
        setUpProductClientMock();
        setUpInventoryClientMock();
        setUpInvoiceClientMock();
        setUpLevelUpClientMock();
        setUpCustomerClientMock();

    }


    @Test
    public void getProduct() {

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product1 = service.getProduct(product.getProductId());

        assertEquals(product, product1);
    }


    @Test
    public void getProductsInInventory() {

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product2 = new Product();
        product2.setProductId(2);
        product2.setProductName("The Legend of Zelda: Link's Awakening");
        product2.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product2.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product2.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        List<Product> productsFromService = service.getProductsInInventory();

        assertEquals(productList, productsFromService);

    }


    @Test
    public void getProductsByInvoiceId() {

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product2 = new Product();
        product2.setProductId(2);
        product2.setProductName("The Legend of Zelda: Link's Awakening");
        product2.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product2.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product2.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        List<Product> productsFromService = service.getProductsByInvoiceId(114);

        assertEquals(productList, productsFromService);

    }

    @Test
    public void getInvoice() {

        OrderViewModel invoiceVM = new OrderViewModel();
        invoiceVM.setInvoiceId(2);
        invoiceVM.setCustomerId(2);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoiceVM.setInvoiceItems(new ArrayList<>());

        OrderViewModel invoiceVM1 = service.getInvoice(invoiceVM.getInvoiceId());

        assertEquals(invoiceVM, invoiceVM1);

    }


    @Test
    public void getAllInvoices() {

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(1);
        invoiceItem.setInvoiceId(114);
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(1);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceItemId(2);
        invoiceItem2.setInvoiceId(114);
        invoiceItem2.setInventoryId(2);
        invoiceItem2.setQuantity(5);
        invoiceItem2.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem2);


        OrderViewModel invoiceVM = new OrderViewModel();
        invoiceVM.setInvoiceId(114);
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);


        OrderViewModel invoiceVM2 = new OrderViewModel();
        invoiceVM2.setInvoiceId(2);
        invoiceVM2.setCustomerId(2);
        invoiceVM2.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoiceVM2.setInvoiceItems(new ArrayList<>());

        List<OrderViewModel> invoiceList = new ArrayList<>();
        invoiceList.add(invoiceVM);
        invoiceList.add(invoiceVM2);

        List<OrderViewModel> fromService = service.getAllInvoices();

        assertEquals(2, fromService.size());
        assertEquals(invoiceList, fromService);

    }

    @Test
    public void getInvoicesByCustomerId() {

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(1);
        invoiceItem.setInvoiceId(114);
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(1);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceItemId(2);
        invoiceItem2.setInvoiceId(114);
        invoiceItem2.setInventoryId(2);
        invoiceItem2.setQuantity(5);
        invoiceItem2.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem2);

        OrderViewModel invoiceVM = new OrderViewModel();
        invoiceVM.setInvoiceId(114);
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);

        OrderViewModel invoiceVM2 = new OrderViewModel();
        invoiceVM2.setInvoiceId(2);
        invoiceVM2.setCustomerId(2);
        invoiceVM2.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoiceVM2.setInvoiceItems(new ArrayList<>());

        List<OrderViewModel> invoicesCust1 = new ArrayList<>();
        invoicesCust1.add(invoiceVM);

        List<OrderViewModel> invoicesCust2 = new ArrayList<>();
        invoicesCust2.add(invoiceVM2);

        List<OrderViewModel> invoicesForCust1FromService = service.getInvoicesByCustomerId(1);

        assertEquals(invoicesCust1, invoicesForCust1FromService);

        List<OrderViewModel> invoicesForCust2FromService = service.getInvoicesByCustomerId(2);

        assertEquals(invoicesCust2, invoicesForCust2FromService);

    }

    @Test
    public void getPoints() {

        LevelUp levelUpCust1 = new LevelUp();
        levelUpCust1.setLevelUpId(1);
        levelUpCust1.setCustomerId(1);
        levelUpCust1.setPoints(10);
        levelUpCust1.setMemberDate(LocalDate.of(2019, 7, 15));

        int levelUpFromService = service.getPoints(1);

        assertEquals(java.util.Optional.ofNullable(levelUpCust1.getPoints()), java.util.Optional.ofNullable(levelUpFromService));

    }

    @Test
    public void addInvoice() {

        List<InvoiceItem> invoiceItemsBefore = new ArrayList<>();

        InvoiceItem invoiceItemBefore = new InvoiceItem();
        invoiceItemBefore.setInventoryId(1);
        invoiceItemBefore.setQuantity(1);
        invoiceItemBefore.setUnitPrice(new BigDecimal("29.99"));

        invoiceItemsBefore.add(invoiceItemBefore);

        InvoiceItem invoiceItem2Before = new InvoiceItem();
        invoiceItem2Before.setInventoryId(2);
        invoiceItem2Before.setQuantity(5);
        invoiceItem2Before.setUnitPrice(new BigDecimal("29.99"));

        invoiceItemsBefore.add(invoiceItem2Before);

        OrderViewModel invoiceVM = new OrderViewModel();
        invoiceVM.setCustomerId(1);
        invoiceVM.setInvoiceItems(invoiceItemsBefore);

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(1);
        invoiceItem.setInvoiceId(114);
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(1);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceItemId(2);
        invoiceItem2.setInvoiceId(114);
        invoiceItem2.setInventoryId(2);
        invoiceItem2.setQuantity(5);
        invoiceItem2.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem2);


        OrderViewModelResponse ovmr = new OrderViewModelResponse();
        ovmr.setInvoiceId(114);
        ovmr.setCustomerId(1);
        ovmr.setPurchaseDate(LocalDate.of(2019, 7, 15));
        ovmr.setInvoiceItems(invoiceItems);

        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(1);
        levelUp.setCustomerId(1);
        levelUp.setPoints(13);
        levelUp.setMemberDate(LocalDate.of(2019, 8, 25));

        ovmr.setPoints(13);

        OrderViewModelResponse fromService = service.addInvoice(invoiceVM);

        verify(rabbitTemplate).convertAndSend(EXCHANGE, ROUTING_KEY, levelUp);
        assertEquals(ovmr, fromService);

    }

    // tests calculate points when previous points are null
    @Test
    public void calculatePoints() {

        InvoiceItem invoiceItemBefore = new InvoiceItem();
        invoiceItemBefore.setInventoryId(1);
        invoiceItemBefore.setQuantity(2);
        invoiceItemBefore.setUnitPrice(new BigDecimal("29.99"));

        List<InvoiceItem> invoiceItems3Before = new ArrayList<>();
        invoiceItems3Before.add(invoiceItemBefore);

         OrderViewModel ovm = new OrderViewModel();
        ovm.setInvoiceId(5);
        ovm.setCustomerId(5);
        ovm.setPurchaseDate(LocalDate.of(2019, 5, 10));
        ovm.setInvoiceItems(invoiceItems3Before);

        int points = service.calculatePoints(ovm);

        LevelUp levelUp = new LevelUp(ovm.getCustomerId(), 1, LocalDate.now());

        verify(rabbitTemplate).convertAndSend(EXCHANGE, ROUTING_KEY, levelUp);

        assertEquals(1, points);

    }

    @Test(expected  = IllegalArgumentException.class)
    public void IfCustomerExists() {

        OrderViewModel invoiceVM = new OrderViewModel();
        invoiceVM.setInvoiceId(114);
        invoiceVM.setCustomerId(20);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(new ArrayList<>());

        if (customerClient.getCustomer(invoiceVM.getCustomerId()) == null) {
            throw new IllegalArgumentException("Customer does not exist");
        }

    }

    @Test(expected = NotFoundException.class)
    public void getPointsFallback(){
        service.fallBack(100);
    }

    @Test(expected = InsufficientQuantityException.class)
    public void checkStockTest(){

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInventoryId(100);
        invoiceItem.setQuantity(5);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        OrderViewModel ivm = new OrderViewModelResponse();
        ivm.setCustomerId(1);
        ivm.setPurchaseDate(LocalDate.of(2019, 7, 15));
        ivm.setInvoiceItems(invoiceItems);

        service.checkStock(ivm);

    }

    @Test(expected = IllegalArgumentException.class)
    public void checkProductTest(){

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInventoryId(100);
        invoiceItem.setQuantity(1);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        OrderViewModel ivm = new OrderViewModelResponse();
        ivm.setCustomerId(1);
        ivm.setPurchaseDate(LocalDate.of(2019, 7, 15));
        ivm.setInvoiceItems(invoiceItems);

        service.checkStock(ivm);

    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInventoryTest(){

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInventoryId(200);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        // invoice
        OrderViewModel ivm = new OrderViewModelResponse();
        ivm.setCustomerId(1);
        ivm.setPurchaseDate(LocalDate.of(2019, 7, 15));
        ivm.setInvoiceItems(invoiceItems);

        service.checkStock(ivm);

    }



    public void setUpProductClientMock() {

        Product product = new Product();
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product2 = new Product();
        product2.setProductId(1);
        product2.setProductName("Mario Kart 8 Deluxe");
        product2.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product2.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product2.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product3 = new Product();
        product3.setProductName("The Legend of Zelda: Link's Awakening");
        product3.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product3.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product3.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        Product product4 = new Product();
        product4.setProductId(2);
        product4.setProductName("The Legend of Zelda: Link's Awakening");
        product4.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product4.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product4.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        doReturn(product2).when(productClient).getProduct(1);
        doReturn(product4).when(productClient).getProduct(2);
        doReturn(null).when(productClient).getProduct(100);

    }

    public void setUpInventoryClientMock() {

        Inventory inventory = new Inventory();
        inventory.setInventoryId(1);
        inventory.setProductId(1);
        inventory.setQuantity(10);

        Inventory inventory2 = new Inventory();
        inventory2.setInventoryId(2);
        inventory2.setProductId(2);
        inventory2.setQuantity(30);

        Inventory inventory3 = new Inventory();
        inventory3.setInventoryId(100);
        inventory3.setProductId(100);
        inventory3.setQuantity(1);

        List<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(inventory);
        inventoryList.add(inventory2);

        doReturn(inventoryList).when(inventoryClient).getAllInventory();
        doReturn(inventory).when(inventoryClient).getInventory(inventory.getInventoryId());
        doReturn(inventory2).when(inventoryClient).getInventory(inventory2.getInventoryId());
        doReturn(inventory3).when(inventoryClient).getInventory(inventory3.getInventoryId());
        doReturn(null).when(inventoryClient).getInventory(200);

    }

    public void setUpInvoiceClientMock() {

        List<InvoiceItem> invoiceItemsBefore = new ArrayList<>();

        InvoiceItem invoiceItemBefore = new InvoiceItem();
        invoiceItemBefore.setInventoryId(1);
        invoiceItemBefore.setQuantity(1);
        invoiceItemBefore.setUnitPrice(new BigDecimal("29.99"));

        invoiceItemsBefore.add(invoiceItemBefore);

        InvoiceItem invoiceItem2Before = new InvoiceItem();
        invoiceItem2Before.setInventoryId(2);
        invoiceItem2Before.setQuantity(5);
        invoiceItem2Before.setUnitPrice(new BigDecimal("29.99"));

        invoiceItemsBefore.add(invoiceItem2Before);

        OrderViewModel orderVMBefore = new OrderViewModel();
        orderVMBefore.setCustomerId(1);
        orderVMBefore.setInvoiceItems(invoiceItemsBefore);

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(1);
        invoiceItem.setInvoiceId(114);
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(1);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceItemId(2);
        invoiceItem2.setInvoiceId(114);
        invoiceItem2.setInventoryId(2);
        invoiceItem2.setQuantity(5);
        invoiceItem2.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem2);

        OrderViewModel orderVM = new OrderViewModel();
        orderVM.setInvoiceId(114);
        orderVM.setCustomerId(1);
        orderVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        orderVM.setInvoiceItems(invoiceItems);

        OrderViewModel orderVM2 = new OrderViewModel();
        orderVM2.setInvoiceId(2);
        orderVM2.setCustomerId(2);
        orderVM2.setPurchaseDate(LocalDate.of(2019, 5, 10));
        orderVM2.setInvoiceItems(new ArrayList<>());

        List<OrderViewModel> invoiceList = new ArrayList<>();
        invoiceList.add(orderVM);
        invoiceList.add(orderVM2);

        doReturn(orderVM).when(invoiceClient).getInvoice(orderVM.getInvoiceId());
        doReturn(orderVM2).when(invoiceClient).getInvoice(2);
        doReturn(invoiceList).when(invoiceClient).getAllInvoices();

        doReturn(orderVM).when(invoiceClient).addInvoice(orderVMBefore);

        List<OrderViewModel> invoiceListCust1 = new ArrayList<>();
        invoiceItem.setInvoiceItemId(1);
        invoiceListCust1.add(orderVM);

        List<OrderViewModel> invoiceListCust2 = new ArrayList<>();
        invoiceListCust2.add(orderVM2);

        doReturn(invoiceListCust1).when(invoiceClient).getInvoiceByCustomerId(1);
        doReturn(invoiceListCust2).when(invoiceClient).getInvoiceByCustomerId(2);

    }

    public void setUpLevelUpClientMock() {

        LevelUp levelUpCust1 = new LevelUp();
        levelUpCust1.setLevelUpId(1);
        levelUpCust1.setCustomerId(1);
        levelUpCust1.setPoints(10);
        levelUpCust1.setMemberDate(LocalDate.of(2019, 7, 15));

        doReturn(levelUpCust1).when(levelUpClient).getLevelUpByCustomerId(1);
        doReturn(null).when(levelUpClient).getLevelUpByCustomerId(5);

    }

    public void setUpCustomerClientMock() {

        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("Test");
        customer.setLastName("Test");
        customer.setStreet("Test");
        customer.setCity("Test");
        customer.setZip("11416");
        customer.setEmail("test@test.com");
        customer.setPhone("121212112");

        doReturn(customer).when(customerClient).getCustomer(1);
        doReturn(null).when(customerClient).getCustomer(20);

    }
}
