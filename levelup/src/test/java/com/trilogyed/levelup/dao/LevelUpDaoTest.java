package com.trilogyed.levelup.dao;

import com.trilogyed.levelup.model.LevelUp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest
public class LevelUpDaoTest {

        @Autowired
        private LevelUpDao dao;

        @Before
        public void setUp() throws Exception{
            List<LevelUp> levelUp = dao.getAllLevelUps();
            for (LevelUp l : levelUp) {
                dao.deleteLevelUp(l.getLevelUpId());
            }

        }

        @Test
        public void addGetDeleteLevelUp() {
            LevelUp levelUp = new LevelUp();
            levelUp.setCustomerId(1);
            levelUp.setPoints(1);
            levelUp.setMemberDate(LocalDate.of(2019,10,12));

            levelUp = dao.addLevelUp(levelUp);

            LevelUp levelUp1 = dao.getLevelUp(levelUp.getLevelUpId());
            assertEquals(levelUp, levelUp1);

            dao.deleteLevelUp(levelUp.getLevelUpId());
            assertEquals(0, dao.getAllLevelUps().size());
        }

        @Test
        public void updateLevelUp() {
            LevelUp levelUp = new LevelUp();
            levelUp.setCustomerId(1);
            levelUp.setPoints(1);
            levelUp.setMemberDate(LocalDate.of(2019,10,12));
            levelUp = dao.addLevelUp(levelUp);

            levelUp.setPoints(10);
            dao.updateLevelUp(levelUp);

            assertEquals(10, dao.getLevelUp(levelUp.getLevelUpId()).getPoints());
        }

        @Test
        public void getAllLevelUps() {
            LevelUp levelUp = new LevelUp();
            levelUp.setCustomerId(1);
            levelUp.setPoints(1);
            levelUp.setMemberDate(LocalDate.of(2019,10,12));

            levelUp = dao.addLevelUp(levelUp);

            levelUp = new LevelUp();
            levelUp.setCustomerId(2);
            levelUp.setPoints(2);
            levelUp.setMemberDate(LocalDate.of(2019,10,11));
            dao.addLevelUp(levelUp);

            levelUp = new LevelUp();
            levelUp.setCustomerId(3);
            levelUp.setPoints(3);
            levelUp.setMemberDate(LocalDate.of(2019,10,10));
            dao.addLevelUp(levelUp);

            assertEquals(3, dao.getAllLevelUps().size());
        }

        @Test
        public void getLevelUpPointsByCustomerId() {
            LevelUp levelUp = new LevelUp();
            levelUp.setCustomerId(1);
            levelUp.setPoints(1);
            levelUp.setMemberDate(LocalDate.of(2019,10,12));
            dao.addLevelUp(levelUp);

            LevelUp fromService = dao.getLevelUpByCustomerId(levelUp.getCustomerId());
            assertEquals(fromService, levelUp);
        }

    }

