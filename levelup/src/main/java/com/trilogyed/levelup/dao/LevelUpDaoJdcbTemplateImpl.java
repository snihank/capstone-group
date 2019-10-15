package com.trilogyed.levelup.dao;

import com.trilogyed.levelup.exception.AmbiguousResultException;
import com.trilogyed.levelup.exception.NotFoundException;
import com.trilogyed.levelup.model.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class LevelUpDaoJdcbTemplateImpl implements LevelUpDao {


    private JdbcTemplate jdbc;

    private static final String INSERT_LEVEL_UP =
            "insert into level_up (customer_id, points, member_date) values(?,?,?)";
    private static final String SELECT_LEVEL_UP =
            "select * from level_up where level_up_id=?";
    private static final String SELECT_ALL_LEVEL_UPS =
            "select * from level_up";
    private static final String SELECT_POINTS_BY_CUSTOMER_ID =
            "select * from level_up where customer_id=?";
    private static final String UPDATE_LEVEL_UP =
            "update level_up set customer_id=?, points=?, member_date=? where level_up_id=?";
    private static final String DELETE_LEVEL_UP =
            "delete from level_up where level_up_id=?";

    @Autowired
    public LevelUpDaoJdcbTemplateImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public LevelUp addLevelUp(LevelUp levelUp) {
        levelUp.setMemberDate(LocalDate.now());
        jdbc.update(
                INSERT_LEVEL_UP,
                levelUp.getCustomerId(),
                levelUp.getPoints(),
                levelUp.getMemberDate()
        );

        levelUp.setLevelUpId(jdbc.queryForObject("select LAST_INSERT_ID()", Integer.class));
        return levelUp;
    }

    @Override
    public LevelUp getLevelUp(int id) {
        try {
            return jdbc.queryForObject(SELECT_LEVEL_UP, this::mapRowToLevelUp, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateLevelUp(LevelUp levelUp) {

        if (getLevelUp(levelUp.getLevelUpId()) == null) {
            throw new IllegalArgumentException("Level up id not found");
        }
        jdbc.update(
                UPDATE_LEVEL_UP,
                levelUp.getCustomerId(),
                levelUp.getPoints(),
                levelUp.getMemberDate(),
                levelUp.getLevelUpId()
        );
    }

    @Override
    @Transactional
    public void deleteLevelUp(int id) {

        if (getLevelUp(id) == null) {
            throw new NotFoundException("Level up id not found");
        }
        jdbc.update(DELETE_LEVEL_UP, id);

    }

    @Override
    public List<LevelUp> getAllLevelUps() {

        return jdbc.query(SELECT_ALL_LEVEL_UPS, this::mapRowToLevelUp);
    }

    @Override
    public LevelUp getLevelUpByCustomerId(int customerId) {
        try {
            LevelUp levelUp = jdbc.queryForObject(SELECT_POINTS_BY_CUSTOMER_ID, this::mapRowToLevelUp, customerId);
            return levelUp;
        }

        catch (EmptyResultDataAccessException e) {
            return null;
        }

        catch (IncorrectResultSizeDataAccessException i) {
            throw new AmbiguousResultException("Points could not be determined due to multiple level ups with the provided customer id.");
        }
    }


    private LevelUp mapRowToLevelUp(ResultSet rs, int rowNum) throws SQLException {
        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(rs.getInt("level_up_id"));
        levelUp.setCustomerId(rs.getInt("customer_id"));
        levelUp.setPoints(rs.getInt("points"));
        levelUp.setMemberDate(rs.getDate("member_date").toLocalDate());
        return levelUp;
    }
}
