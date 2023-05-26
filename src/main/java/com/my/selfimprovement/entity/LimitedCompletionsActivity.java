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
@Table(name = "limited_completions_activities")
@PrimaryKeyJoinColumn(name = "activity_id")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class LimitedCompletionsActivity extends Activity {

    @Id
    @Column(name = "activity_id")
    private long id;

    @Column(name = "completions_limit")
    @NotNull(message = "{valid.limitedCompletionsActivity.completionsLimit.notNull}")
    @Min(value = 1, message = "{valid.limitedCompletionsActivity.completionsLimit}")
    @Max(value = 9999, message = "{valid.limitedCompletionsActivity.completionsLimit}")
    private int completionsLimit;

    @Override
    public String toString() {
        return "RepetitiveActivity{" +
                "id=" + id +
                ", completionsLimit=" + completionsLimit +
                ", super = " + super.toString() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LimitedCompletionsActivity that = (LimitedCompletionsActivity) o;
        return id == that.id && completionsLimit == that.completionsLimit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, completionsLimit);
    }

}
