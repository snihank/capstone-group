package com.trilogyed.levelup.service;

import com.trilogyed.levelup.dao.LevelUpDao;
import com.trilogyed.levelup.dao.LevelUpDaoJdcbTemplateImpl;
import com.trilogyed.levelup.model.LevelUp;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

public class ServiceLayerTest {

    ServiceLayer service;
    LevelUpDao dao;


    @Before
    public void setUp() throws Exception {

        setUpLevelUpMock();

        service = new ServiceLayer(dao);
    }

    @Test
    public void addLevelUp() {
        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(1);
        levelUp.setPoints(10);
        levelUp.setMemberDate(LocalDate.of(2019, 10, 13));

        levelUp = service.addLevelUp(levelUp);

        LevelUp fromService = service.getLevelUp(levelUp.getLevelUpId());

        assertEquals(levelUp, fromService);
    }

    @Test
    public void getLevelUp() {

        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(1);
        levelUp.setCustomerId(1);
        levelUp.setPoints(10);
        levelUp.setMemberDate(LocalDate.of(2019, 10, 13));

        LevelUp fromService = service.getLevelUp(levelUp.getLevelUpId());

        assertEquals(levelUp, fromService);
    }

    @Test
    public void getAllLevelUps() {

        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(1);
        levelUp.setPoints(10);
        levelUp.setMemberDate(LocalDate.of(2019, 10, 13));

        service.addLevelUp(levelUp);

        levelUp = new LevelUp();
        levelUp.setCustomerId(2);
        levelUp.setPoints(20);
        levelUp.setMemberDate(LocalDate.of(2019, 10, 15));

        service.addLevelUp(levelUp);

        List<LevelUp> fromService = service.getAllLevelUps();

        assertEquals(2, fromService.size());

    }

    @Test
    public void deleteLevelUp() {
        LevelUp levelUp = service.getLevelUp(1);
        service.deleteLevelUp(1);
        ArgumentCaptor<Integer> c = ArgumentCaptor.forClass(Integer.class);
        verify(dao).deleteLevelUp(c.capture());
        assertEquals(levelUp.getLevelUpId(), c.getValue().intValue());
    }

    @Test
    public void updateLevelUp() {

        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(1);
        levelUp.setCustomerId(1);
        levelUp.setPoints(25);
        levelUp.setMemberDate(LocalDate.of(2019, 10, 13));

        service.updateLevelUp(levelUp);
        ArgumentCaptor<LevelUp> c = ArgumentCaptor.forClass(LevelUp.class);
        verify(dao).updateLevelUp(c.capture());
        assertEquals(levelUp.getPoints(), c.getValue().getPoints());

    }


    @Test
    public void getLevelUpPointsByCustomerId() {

        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(1);
        levelUp.setCustomerId(1);
        levelUp.setPoints(10);
        levelUp.setMemberDate(LocalDate.of(2019, 10, 13));

        LevelUp levelUp2 = new LevelUp();
        levelUp2.setLevelUpId(2);
        levelUp2.setCustomerId(2);
        levelUp2.setPoints(20);
        levelUp2.setMemberDate(LocalDate.of(2019, 10, 15));

        LevelUp c1 = service.getLevelUpByCustomerId(levelUp.getLevelUpId());

        assertEquals(levelUp, c1);

        LevelUp c2 = service.getLevelUpByCustomerId(levelUp2.getLevelUpId());

        assertEquals(levelUp2, c2);
    }

    public void setUpLevelUpMock() {

        dao = mock(LevelUpDaoJdcbTemplateImpl.class);

        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(1);
        levelUp.setPoints(10);
        levelUp.setMemberDate(LocalDate.of(2019, 10, 13));

        LevelUp levelUp2 = new LevelUp();
        levelUp2.setLevelUpId(1);
        levelUp2.setCustomerId(1);
        levelUp2.setPoints(10);
        levelUp2.setMemberDate(LocalDate.of(2019, 10, 13));

        LevelUp levelUp3 = new LevelUp();
        levelUp3.setCustomerId(2);
        levelUp3.setPoints(20);
        levelUp3.setMemberDate(LocalDate.of(2019, 10, 15));

        LevelUp levelUp4 = new LevelUp();
        levelUp4.setLevelUpId(2);
        levelUp4.setCustomerId(2);
        levelUp4.setPoints(20);
        levelUp4.setMemberDate(LocalDate.of(2019, 10, 15));

        doReturn(levelUp2).when(dao).addLevelUp(levelUp);
        doReturn(levelUp4).when(dao).addLevelUp(levelUp3);
        doReturn(levelUp2).when(dao).getLevelUp(1);
        doReturn(levelUp4).when(dao).getLevelUp(2);

        doReturn(levelUp2).when(dao).getLevelUpByCustomerId(1);
        doReturn(levelUp4).when(dao).getLevelUpByCustomerId(2);

        List<LevelUp> levelUpList = new ArrayList<>();
        levelUpList.add(levelUp2);
        levelUpList.add(levelUp4);

        doReturn(levelUpList).when(dao).getAllLevelUps();

    }
}
