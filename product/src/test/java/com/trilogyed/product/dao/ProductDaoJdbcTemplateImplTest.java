package com.trilogyed.product.dao;


import com.trilogyed.product.exception.NotFoundException;
import com.trilogyed.product.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProductDaoJdbcTemplateImplTest {

    @Autowired
    ProductDao productDao;

    // clear product table in database
    @Before
    public void setUp() throws Exception {
        List<Product> products = productDao.getAllProducts();
        for (Product p : products) {
            productDao.deleteProduct(p.getProductId());
        }
    }

    // tests addProduct(), getProduct() and deleteProduct()
    @Test
    public void addGetDeleteProduct() {

        Product product = new Product();
        product.setProductName("Red Epic dragon");
        product.setProductDescription("Shot 4K raw");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        product = productDao.addProduct(product);

        Product product1 = productDao.getProduct(product.getProductId());
        assertEquals(product, product1);

        productDao.deleteProduct(product.getProductId());
        product1 = productDao.getProduct(product.getProductId());
        assertNull(product1);
    }

    // tests if will return null if try to get product with non-existent id
    @Test
    public void getProductWithNonExistentId() {
        Product product = productDao.getProduct(500);
        assertNull(product);
    }

    // tests if will throw exception if id provided does not exist when trying to delete product
    @Test(expected  = NotFoundException.class)
    public void deleteProductWithNonExistentId() {

        productDao.deleteProduct(500);

    }

    // tests updateProduct()
    @Test
    public void updateProduct() {

        Product product = new Product();
        product.setProductName("Red Epic dragon");
        product.setProductDescription("Shot 4K raw");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        product = productDao.addProduct(product);

        product.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));

        productDao.updateProduct(product);

        Product product1 = productDao.getProduct(product.getProductId());
        assertEquals(product, product1);
    }

    // tests if will throw exception if id provided does not exist when trying to update product
    @Test(expected  = IllegalArgumentException.class)
    public void updateProductWithIllegalArgumentException() {

        Product product = new Product();
        product.setProductName("Red Epic dragon");
        product.setProductDescription("Shot 4K raw");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        productDao.updateProduct(product);

    }

    // tests getAllProducts()
    @Test
    public void getAllProducts() {

        Product product = new Product();
        product.setProductName("Red Epic dragon");
        product.setProductDescription("Shot 4K raw");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        productDao.addProduct(product);

        product = new Product();
        product.setProductName("Phantom");
        product.setProductDescription("Shot ultra slow motion");
        product.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        productDao.addProduct(product);

        List<Product> pList = productDao.getAllProducts();
        assertEquals(2, pList.size());
    }

}
