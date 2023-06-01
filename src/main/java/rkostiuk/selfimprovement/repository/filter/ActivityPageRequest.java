package rkostiuk.selfimprovement.repository.filter;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityPageRequest extends EntityPageRequest {
    public ActivityPageRequest() {
        setSortBy("name");
    }
}
