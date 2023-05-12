package com.my.selfimprovement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Objects;

/**
 * Represents activity that may be completed more than once.<br>
 * If {@code periodType} is set to null, then field {@code timesPerPeriod} is ignored
 */
@Entity
@Table(name = "repetitive_activities")
@PrimaryKeyJoinColumn(name = "activity_id")
@Data
public class RepetitiveActivity extends Activity {

    public enum PeriodType {
        DAILY,
        WEEKLY,
        MONTHLY,
        NO_PERIOD
    }

    @Id
    @Column(name = "activity_id")
    private long id;

    @Column(name = "period_type")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{valid.repetitiveActivity.periodType.notNull}")
    private PeriodType periodType = PeriodType.NO_PERIOD;

    @Column(name = "times_per_period")
    @Min(value = 0, message = "{valid.repetitiveActivity.timesPerPeriod}")
    @Max(value = 10000, message = "{valid.repetitiveActivity.timesPerPeriod}")
    private int timesPerPeriod;

    @Override
    public boolean isRepetitive() {
        return true;
    }

    @Override
    public String toString() {
        return "RepetitiveActivity{" +
                "id=" + id +
                ", periodType=" + periodType +
                ", timesPerPeriod=" + timesPerPeriod +
                ", super = " + super.toString() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RepetitiveActivity that = (RepetitiveActivity) o;
        return id == that.id && timesPerPeriod == that.timesPerPeriod && periodType == that.periodType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, periodType, timesPerPeriod);
    }

}
