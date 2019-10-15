package com.trilogyed.admin.controller;


import com.trilogyed.admin.service.InventoryService;
import com.trilogyed.admin.util.messages.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @RequestMapping(value = "/inventory", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Inventory addInventory(@RequestBody @Valid Inventory inventory) {
        return inventoryService.addInventory(inventory);
    }

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Inventory getInventory(@PathVariable int id) {
        Inventory inventory = inventoryService.getInvenotry(id);
        return inventory;
    }

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateInventory(@PathVariable int id, @RequestBody @Valid Inventory inventory) {
        if (inventory.getInventoryId() == 0)
            inventory.setInventoryId(id);
        if (id != inventory.getInventoryId()) {
            throw new IllegalArgumentException("ID on path must match the ID in the Inventory object");
        }
        inventoryService.updateInventory(id, inventory);
    }

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInventory(@PathVariable int id) {
        inventoryService.deleteInventory(id);
    }

    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> getAllInventory() {
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        return inventoryList;
    }

}

