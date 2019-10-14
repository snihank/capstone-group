package com.trilogyed.levelup.controller;

import com.trilogyed.levelup.exception.NotFoundException;
import com.trilogyed.levelup.model.LevelUp;
import com.trilogyed.levelup.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/levelup")
public class LevelUpController {

    @Autowired
    ServiceLayer service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LevelUp addLevelUp(@RequestBody @Valid LevelUp levelUp) {
        return service.addLevelUp(levelUp);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LevelUp getLevelUp(@PathVariable int id) {
        LevelUp levelUp = service.getLevelUp(id);
        if (levelUp == null)
            throw new NotFoundException("Cannot find id: " + id);
        return levelUp;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String updateLevelUp(@PathVariable int id, @RequestBody @Valid LevelUp levelUp) {
        service.updateLevelUp(levelUp);
        return "Level up successfully updated";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteLevelUp(@PathVariable int id) {
        service.deleteLevelUp(id);
        return "Level up successfully deleted";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LevelUp> getAllLevelUps() {
        return service.getAllLevelUps();
    }

    @GetMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LevelUp getLevelUpByCustomerId(@PathVariable("id") int id) {
        LevelUp levelUp = service.getLevelUpByCustomerId(id);
        if (levelUp == null) {
            throw new NotFoundException("Customer not found.");
        }
        return levelUp;
    }

}
