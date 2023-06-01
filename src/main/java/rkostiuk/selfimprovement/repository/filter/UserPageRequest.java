package rkostiuk.selfimprovement.repository.filter;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserPageRequest extends EntityPageRequest {
    public UserPageRequest() {
        setSortBy("name");
    }
}
