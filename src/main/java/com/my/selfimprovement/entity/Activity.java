package com.my.selfimprovement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "activities")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Activity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "minutes_required")
    private int minutesRequired;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToMany(mappedBy = "activities", cascade = CascadeType.PERSIST)
    private final Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "activity")
    private final Set<UserActivity> userActivities = new HashSet<>();

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", minutesRequired=" + minutesRequired +
                ", author.id=" + author.getId() +
                ", categories.id=" + categories.stream().map(Category::getId).collect(Collectors.toSet()) +
                ", userActivities=" + userActivities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id == activity.id &&
                minutesRequired == activity.minutesRequired &&
                Objects.equals(name, activity.name) &&
                Objects.equals(description, activity.description) &&
                Objects.equals(author, activity.author) &&
                Objects.equals(categories, activity.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, minutesRequired, author, categories);
    }

    public Set<Category> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    public Set<UserActivity> getUserActivities() {
        return Collections.unmodifiableSet(userActivities);
    }

    public void addCategories(Collection<Category> categories) {
        this.categories.addAll(categories);
    }

    public boolean isRepetitive() {
        return false;
    }

}
