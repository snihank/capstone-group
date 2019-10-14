package com.trilogyed.levelup.service;

import com.trilogyed.levelup.dao.LevelUpDao;
import com.trilogyed.levelup.model.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceLayer{
    LevelUpDao levelUpDao;

    @Autowired
    public ServiceLayer(LevelUpDao levelUpDao) {
        this.levelUpDao = levelUpDao;
    }

    public LevelUp addLevelUp(LevelUp levelUp) {
        return levelUpDao.addLevelUp(levelUp);
    }

    public LevelUp getLevelUp(int id) {
        return levelUpDao.getLevelUp(id);
    }

    public void updateLevelUp(LevelUp levelUp) {
        levelUpDao.updateLevelUp(levelUp);
    }

    public void deleteLevelUp(int id) {
        levelUpDao.deleteLevelUp(id);
    }

    public List<LevelUp> getAllLevelUps() {
        return levelUpDao.getAllLevelUps();
    }

    public LevelUp getLevelUpByCustomerId(int customerId) {
        return levelUpDao.getLevelUpByCustomerId(customerId);
    }

}
