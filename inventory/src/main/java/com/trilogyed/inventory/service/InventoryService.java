package com.trilogyed.inventory.service;

import com.trilogyed.inventory.dao.InventoryDao;
import com.trilogyed.inventory.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryService {

    InventoryDao inventoryDao;

    @Autowired
    public InventoryService(InventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    public Inventory addInventory(Inventory inventory) {
        return inventoryDao.addInventory(inventory);
    }

    public Inventory getInvenotry(int inventoryId) {
        return inventoryDao.getInventory(inventoryId);
    }

    public void updateInventory(Inventory inventory) {
        inventoryDao.updateInventory(inventory);
    }

    public void deleteInventory(int inventoryId) {
        inventoryDao.deleteInventory(inventoryId);
    }

    public List<Inventory> getAllInventory() {
        return inventoryDao.getAllInventory();
    }

}
