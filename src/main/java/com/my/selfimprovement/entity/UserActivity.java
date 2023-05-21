package com.my.selfimprovement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user_activities")
@Data
@NoArgsConstructor
public class UserActivity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "started_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant startedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private Activity activity;

    @OneToMany(mappedBy = "userActivity", cascade = CascadeType.ALL)
    private final Set<UserActivityCompletion> completions = new HashSet<>();

    public UserActivity(User user, Activity activity) {
        this.user = user;
        this.activity = activity;
    }

    @PrePersist
    private void initStaredAt() {
        startedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "UserActivity{" +
                "id=" + id +
                ", startedAt=" + startedAt +
                ", user.id=" + user.getId() +
                ", activity.id=" + activity.getId() +
                ", completions=" + completions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActivity that = (UserActivity) o;
        return id == that.id &&
                Objects.equals(startedAt, that.startedAt) &&
                Objects.equals(user, that.user) &&
                Objects.equals(activity, that.activity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startedAt, user, activity);
    }

    public Set<UserActivityCompletion> getCompletions() {
        return Collections.unmodifiableSet(completions);
    }

    public void addCompletion(UserActivityCompletion completion) {
        completions.add(completion);
    }

}
