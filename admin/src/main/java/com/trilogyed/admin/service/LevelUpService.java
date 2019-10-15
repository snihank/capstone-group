package com.trilogyed.admin.service;

import com.trilogyed.admin.util.feign.LevelUpClient;
import com.trilogyed.admin.util.messages.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LevelUpService {

    LevelUpClient levelUpClient;


    public LevelUpService() {
    }

    @Autowired
    public LevelUpService(LevelUpClient levelUpClient) {
        this.levelUpClient = levelUpClient;
    }


    public LevelUp addLevelUp(LevelUp levelUp) {
        return levelUpClient.addLevelUp(levelUp);
    }

    public LevelUp getLevelUp(int id) {
        return levelUpClient.getLevelUp(id);
    }

    public void updateLevelUp(int id, LevelUp levelUp) {
        levelUpClient.updateLevelUp(id, levelUp);
    }

    public void deleteLevelUp(int id) {
        levelUpClient.deleteLevelUp(id);
    }

    public List<LevelUp> getAllLevelUps() {
        return levelUpClient.getAllLevelUps();
    }

    public Integer getLevelUpPointsByCustomerId(int customerId) {
        return levelUpClient.getLevelUpPointsByCustomerId(customerId);
    }

}
