package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by yxx03 on 1/30/2015.
 */
public interface ProfileBeanIgnoreFieldsMixIn {
    @JsonIgnore
    public String getExtraString();
}
