package com.trilogyed.product.service;

import com.trilogyed.product.dao.ProductDao;
import com.trilogyed.product.dao.ProductDaoJdbcTemplateImpl;
import com.trilogyed.product.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    ProductService productService;
    ProductDao productDao;

    @Before
    public void setUp() throws Exception {


        setUpProductMock();


        productService = new ProductService(productDao);

    }

    @Test
    public void addProduct() {
        Product product = new Product();
        product.setProductName("Red Epic dragon");
        product.setProductDescription("Shot 4K raw");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        product = productService.addProduct(product);

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
        verify(productDao).deleteProduct(postCaptor.capture());
        assertEquals(product.getProductId(), postCaptor.getValue().intValue());
    }


    @Test
    public void updateProduct() {

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Red Epic dragon");
        product.setProductDescription("Shot 4K raw");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));


        productService.updateProduct(product);
        ArgumentCaptor<Product> postCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productDao).updateProduct(postCaptor.capture());
        assertEquals(product.getUnitCost(), postCaptor.getValue().getUnitCost());

    }


    @Test
    public void getProductWithNonExistentId() {
        Product product = productService.getProduct(500);
        assertNull(product);
    }

    /*************************** Helper Method ******************************************************/


    public void setUpProductMock() {

        productDao = mock(ProductDaoJdbcTemplateImpl.class);

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

        doReturn(product2).when(productDao).addProduct(product);
        doReturn(product4).when(productDao).addProduct(product3);
        doReturn(product2).when(productDao).getProduct(1);
        doReturn(product4).when(productDao).getProduct(2);

        List<Product> productList = new ArrayList<>();
        productList.add(product2);
        productList.add(product4);

        doReturn(productList).when(productDao).getAllProducts();

    }

}
