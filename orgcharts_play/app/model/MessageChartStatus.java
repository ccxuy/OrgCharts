package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by yxx03 on 1/16/2015.
 */
public class MessageChartStatus extends MessageCommon{
    public final String messageType = MessageChartStatus.class.getName();
    public String editRequest;
    public String status; //success, denied, notfound, error
    public String chartId;
    public String isLocked = IS_LOCKED_UNKNOWN; //true, false, unknown
    public String isOwner = IS_OWNER_UNKNOWN; //true, false, unknown
    public ChartLock lock;
    public String msg = "";
    public String fromUserId;
    public String fromMethod;

    @JsonIgnore
    public static final String STATUS_SUCCESS = "success";
    @JsonIgnore
    public static final String STATUS_DENIED = "denied";
    @JsonIgnore
    public static final String STATUS_NOT_FOUND = "notFound";
    @JsonIgnore
    public static final String STATUS_ERROR = "error";

    @JsonIgnore
    public static final String IS_LOCKED_TRUE = "true";
    @JsonIgnore
    public static final String IS_LOCKED_FALSE = "false";
    @JsonIgnore
    public static final String IS_LOCKED_UNKNOWN = "unknown";

    @JsonIgnore
    public static final String IS_OWNER_TRUE = "true";
    @JsonIgnore
    public static final String IS_OWNER_FALSE = "false";
    @JsonIgnore
    public static final String IS_OWNER_UNKNOWN = "unknown";
}
