package com.trilogyed.admin.service;


import com.trilogyed.admin.util.feign.InventoryClient;
import com.trilogyed.admin.util.messages.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryService {

    InventoryClient inventoryClient;

    public InventoryService() {
    }

    @Autowired
    public InventoryService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    public Inventory addInventory(Inventory inventory) {
        return inventoryClient.addInventory(inventory);
    }

    public Inventory getInvenotry(int inventoryId) {
        return inventoryClient.getInventory(inventoryId);
    }

    public void updateInventory(int id, Inventory inventory) {
        inventoryClient.updateInventory(id, inventory);
    }

    public void deleteInventory(int inventoryId) {
        inventoryClient.deleteInventory(inventoryId);
    }

    public List<Inventory> getAllInventory() {
        return inventoryClient.getAllInventory();
    }

}