package com.my.selfimprovement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "user_activities_completions")
@Data
public class UserActivityCompletion implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_activity_id", referencedColumnName = "id")
    private UserActivity userActivity;

    @Column(name = "completed_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant completedAt;

    @PrePersist
    private void initCompletedAt() {
        completedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "UserActivityCompletion{" +
                "id=" + id +
                ", userActivity.id=" + userActivity.getId() +
                ", completedAt=" + completedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActivityCompletion that = (UserActivityCompletion) o;
        return id == that.id &&
                Objects.equals(userActivity, that.userActivity) &&
                Objects.equals(completedAt, that.completedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userActivity, completedAt);
    }

}
