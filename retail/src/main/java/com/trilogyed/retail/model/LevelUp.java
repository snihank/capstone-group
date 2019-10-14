package com.trilogyed.retail.model;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Objects;

public class LevelUp {
    private Integer levelUpId;
    @NotNull(message = "Custoemr id cannot be empty")
    @Positive
    private Integer customerId;
    @NotNull(message = "Points cannot be empty")
    private Integer points;
    @NotNull(message = "Date cannot be empty")
    private LocalDate memberDate;

    public LevelUp() {
    }

    public LevelUp(int customerId, int points, LocalDate memberDate) {
        this.customerId = customerId;
        this.points = points;
        this.memberDate = memberDate;
    }

    public LevelUp(Integer levelUpId, Integer customerId, Integer points, LocalDate memberDate) {
        this.levelUpId = levelUpId;
        this.customerId = customerId;
        this.points = points;
        this.memberDate = memberDate;
    }

    public Integer getLevelUpId() {
        return levelUpId;
    }

    public void setLevelUpId(Integer levelUpId) {
        this.levelUpId = levelUpId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDate getMemberDate() {
        return memberDate;
    }

    public void setMemberDate(LocalDate memberDate) {
        this.memberDate = memberDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LevelUp)) return false;
        LevelUp levelUp = (LevelUp) o;
        return getLevelUpId().equals(levelUp.getLevelUpId()) &&
                getCustomerId().equals(levelUp.getCustomerId()) &&
                getPoints().equals(levelUp.getPoints()) &&
                getMemberDate().equals(levelUp.getMemberDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLevelUpId(), getCustomerId(), getPoints(), getMemberDate());
    }
}
