package com.my.selfimprovement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Objects;

@Entity
@Table(name = "periodical_limited_completions_activities")
@PrimaryKeyJoinColumn(name = "activity_id")
@Data
public class PeriodicalLimitedCompletionsActivity extends LimitedCompletionsActivity {

    @Id
    @Column(name = "activity_id")
    private long id;

    @Column(name = "period_duration_minutes")
    @NotNull(message = "{valid.limitedCompletionsActivity.periodDurationMinutes.notNull}")
    @Min(value = 1, message = "{valid.limitedCompletionsActivity.periodDurationMinutes}")
    @Max(value = 99_999, message = "{valid.limitedCompletionsActivity.periodDurationMinutes}")
    private long periodDurationMinutes;

    @Override
    public String toString() {
        return "PeriodicalLimitedCompletionsActivity{" +
                "(super=" + super.toString() +
                ") id=" + id +
                ", periodDurationMinutes=" + periodDurationMinutes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var that = (PeriodicalLimitedCompletionsActivity) o;
        return id == that.id && periodDurationMinutes == that.periodDurationMinutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, periodDurationMinutes);
    }

}
