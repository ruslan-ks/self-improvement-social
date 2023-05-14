package com.my.selfimprovement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Objects;

@Entity
@Table(name = "periodical_limited_completions_activities")
@PrimaryKeyJoinColumn(name = "activity_id")
@Data
public class PeriodicalLimitedCompletionsActivity extends LimitedCompletionsActivity {

    public enum PeriodType {
        DAILY,
        WEEKLY,
        MONTHLY
    }

    @Id
    @Column(name = "activity_id")
    private long id;

    @Column(name = "period_type")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{valid.limitedCompletionsActivity.periodType.notEmpty}")
    private PeriodType periodType;

    @Override
    public String toString() {
        return "PeriodicalLimitedCompletionsActivity{" +
                "(super=" + super.toString() +
                ")id=" + id +
                ", periodType=" + periodType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var that = (PeriodicalLimitedCompletionsActivity) o;
        return id == that.id && periodType == that.periodType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, periodType);
    }

}
