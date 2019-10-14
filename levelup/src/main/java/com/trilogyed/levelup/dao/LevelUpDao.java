package com.trilogyed.levelup.dao;

import com.trilogyed.levelup.model.LevelUp;

import java.util.List;

public interface LevelUpDao {

    LevelUp addLevelUp(LevelUp levelUp);

    LevelUp getLevelUp(int id);

    void updateLevelUp(LevelUp levelUp);

    void deleteLevelUp(int id);

    List<LevelUp> getAllLevelUps();

    LevelUp getLevelUpByCustomerId(int customerId);
}
