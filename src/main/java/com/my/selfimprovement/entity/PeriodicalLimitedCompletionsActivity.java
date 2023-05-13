package com.my.selfimprovement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "periodical_limited_completions_activities")
@PrimaryKeyJoinColumn(name = "activity_id")
@Data
public class PeriodicalLimitedCompletionsActivity {

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
    @NotNull(message = "{valid.repetitiveActivity.periodType.notNull}")
    private PeriodType periodType;

    @Override
    public String toString() {
        return "PeriodicalLimitedCompletionsActivity{" +
                "(super=" + super.toString() +
                ")id=" + id +
                ", periodType=" + periodType +
                '}';
    }

}
