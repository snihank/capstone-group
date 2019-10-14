package com.trilogyed.admin.service;

import com.trilogyed.admin.util.feign.ProductClient;
import com.trilogyed.admin.util.messages.Product;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductClient productClient;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        // configure mock objects
        setUpProductClientMock();
        
    }


    @Test
    public void addProduct() {
        Product product = new Product();
        product.setProductName("Red Epic dragon");
        product.setProductDescription("Shot 4K raw");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        product= productService.addProduct(product);

        Product product1 = productService.getProduct(product.getProductId());

        assertEquals(product, product1);
    }

    @Test
    public void getProduct() {

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Red Epic dragon");
        product.setProductDescription("Shot 4K raw");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product1 = productService.getProduct(product.getProductId());

        assertEquals(product, product1);
    }

    @Test
    public void findAllProducts() {

        Product product = new Product();
        product.setProductName("Red Epic dragon");
        product.setProductDescription("Shot 4K raw");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        productService.addProduct(product);

        product = new Product();
        product.setProductName("Phantom");
        product.setProductDescription("Shot ultra slow motion");
        product.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));


        productService.addProduct(product);

        List<Product> fromService = productService.getAllProducts();

        assertEquals(2, fromService.size());
    }

    @Test
    public void deleteProduct() {
        Product product = productService.getProduct(1);
        productService.deleteProduct(1);
        ArgumentCaptor<Integer> postCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(productClient).deleteProduct(postCaptor.capture());
        assertEquals(product.getProductId(), postCaptor.getValue().intValue());
    }

    @Test
    public void updateProduct() {

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Red Epic dragon");
        product.setProductDescription("Shot 4K raw");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(30.00).setScale(2, RoundingMode.HALF_UP));


        productService.updateProduct(product.getProductId(), product);
        ArgumentCaptor<Product> postCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productClient).updateProduct(any(Integer.class), postCaptor.capture());
        assertEquals(product.getUnitCost(), postCaptor.getValue().getUnitCost());

    }

    // tests if will return null if try to get product with non-existent id
    @Test
    public void getProductWithNonExistentId() {
        Product product = productService.getProduct(500);
        assertNull(product);
    }

    // tests default constructor for test coverage
    // so developers know something went wrong if less than 100%
    @Test
    public void createADefaultProduct() {

        Object productObj = new ProductService();

        assertEquals(true , productObj instanceof ProductService);

    }

   /*************************** Helper Method ******************************************************/

    public void setUpProductClientMock() {

        Product product = new Product();
        product.setProductName("Red Epic dragon");
        product.setProductDescription("Shot 4K raw");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product2 = new Product();
        product2.setProductId(1);
        product2.setProductName("Red Epic dragon");
        product2.setProductDescription("Shot 4K raw");
        product2.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product2.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product3 = new Product();
        product3.setProductName("Phantom");
        product3.setProductDescription("Shot ultra slow motion");
        product3.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product3.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        Product product4 = new Product();
        product4.setProductId(2);
        product4.setProductName("Phantom");
        product4.setProductDescription("Shot ultra slow motion");
        product4.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product4.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        doReturn(product2).when(productClient).addProduct(product);
        doReturn(product4).when(productClient).addProduct(product3);
        doReturn(product2).when(productClient).getProduct(1);
        doReturn(product4).when(productClient).getProduct(2);

        List<Product> productList = new ArrayList<>();
        productList.add(product2);
        productList.add(product4);

        doReturn(productList).when(productClient).getAllProducts();

    }

}

