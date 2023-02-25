package com.my.selfimprovement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

    public enum Role {
        USER,
        ADMIN,
        ROOT
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "registered_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant registeredAt;

    @ManyToMany
    @JoinTable(name = "user_followings",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    private final Set<User> followings = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_followings",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private final Set<User> followers = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private final Set<Activity> createdActivities = new HashSet<>();

    // Activities user is doing
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final Set<UserActivity> activities = new HashSet<>();

    @PrePersist
    private void initRegisteredAt() {
        registeredAt = Instant.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", role=" + role +
                ", registeredAt=" + registeredAt +
                ", followings.id=" + followings.stream().map(User::getId).collect(Collectors.toSet()) +
                ", followers.id=" + followers.stream().map(User::getId).collect(Collectors.toSet()) +
                ", createdActivities.id=" +
                createdActivities.stream().map(Activity::getId).collect(Collectors.toSet()) +
                ", activities=" + activities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(birthday, user.birthday) &&
                role == user.role &&
                Objects.equals(registeredAt, user.registeredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, password, birthday, role, registeredAt);
    }

    public Set<User> getFollowings() {
        return Collections.unmodifiableSet(followings);
    }

    public Set<User> getFollowers() {
        return Collections.unmodifiableSet(followers);
    }

    public Set<Activity> getCreatedActivities() {
        return Collections.unmodifiableSet(createdActivities);
    }

    public Set<UserActivity> getActivities() {
        return Collections.unmodifiableSet(activities);
    }

    public void addFollowing(User other) {
        followings.add(other);
    }

    public void addFollower(User other) {
        followers.add(other);
    }

    public void addActivity(UserActivity userActivity) {
        activities.add(userActivity);
    }

}
