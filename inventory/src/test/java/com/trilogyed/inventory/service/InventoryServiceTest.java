package com.trilogyed.inventory.service;

import com.trilogyed.inventory.dao.InventoryDao;
import com.trilogyed.inventory.dao.InventoryDaoJdbcTemplateImpl;
import com.trilogyed.inventory.model.Inventory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {

    InventoryService inventoryService;
    InventoryDao inventoryDao;

    @Before
    public void setUp() throws Exception {


        setUpInventoryMock();


        inventoryService = new InventoryService(inventoryDao);

    }

    @Test
    public void addInventory() {
        Inventory inventory = new Inventory();
        inventory.setProductId(1);
        inventory.setQuantity(10);

        inventory = inventoryService.addInventory(inventory);

        Inventory inventory1 = inventoryService.getInvenotry(inventory.getInventoryId());

        assertEquals(inventory, inventory1);
    }

    @Test
    public void getInventory() {

        Inventory inventory = new Inventory();
        inventory.setInventoryId(1);
        inventory.setProductId(1);
        inventory.setQuantity(10);

        Inventory inventory1 = inventoryService.getInvenotry(inventory.getInventoryId());

        assertEquals(inventory, inventory1);
    }

    @Test
    public void getAllInventory() {

        Inventory inventory = new Inventory();
        inventory.setProductId(1);
        inventory.setQuantity(10);

        inventoryService.addInventory(inventory);

        inventory = new Inventory();
        inventory.setProductId(2);
        inventory.setQuantity(30);

        inventoryService.addInventory(inventory);

        List<Inventory> fromService = inventoryService.getAllInventory();

        assertEquals(2, fromService.size());

    }

    @Test
    public void deleteInventory() {
        Inventory inventory = inventoryService.getInvenotry(1);
        inventoryService.deleteInventory(1);
        ArgumentCaptor<Integer> postCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(inventoryDao).deleteInventory(postCaptor.capture());
        assertEquals(inventory.getInventoryId(), postCaptor.getValue().intValue());
    }

    @Test
    public void updateInventory() {

        Inventory inventory = new Inventory();
        inventory.setProductId(1);
        inventory.setQuantity(100);

        inventoryService.updateInventory(inventory);
        ArgumentCaptor<Inventory> postCaptor = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryDao).updateInventory(postCaptor.capture());
        assertEquals(inventory.getQuantity(), postCaptor.getValue().getQuantity());

    }


    @Test
    public void getInventoryWithNonExistentId() {
        Inventory inventory = inventoryService.getInvenotry(500);
        assertNull(inventory);
    }

    /********************************* Helper Method *******************************/

    public void setUpInventoryMock() {

        inventoryDao = mock(InventoryDaoJdbcTemplateImpl.class);

        Inventory inventory = new Inventory();
        inventory.setProductId(1);
        inventory.setQuantity(10);

        Inventory inventory2 = new Inventory();
        inventory2.setInventoryId(1);
        inventory2.setProductId(1);
        inventory2.setQuantity(10);

        Inventory inventory3 = new Inventory();
        inventory3.setProductId(2);
        inventory3.setQuantity(30);

        Inventory inventory4 = new Inventory();
        inventory4.setInventoryId(2);
        inventory4.setProductId(2);
        inventory4.setQuantity(30);

        doReturn(inventory2).when(inventoryDao).addInventory(inventory);
        doReturn(inventory4).when(inventoryDao).addInventory(inventory3);
        doReturn(inventory2).when(inventoryDao).getInventory(1);
        doReturn(inventory4).when(inventoryDao).getInventory(2);

        List<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(inventory2);
        inventoryList.add(inventory4);

        doReturn(inventoryList).when(inventoryDao).getAllInventory();

    }
}


