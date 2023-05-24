package com.my.selfimprovement.entity.key;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class UserActivityPK implements Serializable {
    private long userId;
    private long activityId;
}
