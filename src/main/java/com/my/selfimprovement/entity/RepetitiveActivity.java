package com.my.selfimprovement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

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
    private PeriodType periodType = PeriodType.NO_PERIOD;

    @Column(name = "times_per_period")
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
