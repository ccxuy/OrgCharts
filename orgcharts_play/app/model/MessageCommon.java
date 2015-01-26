package model;

/**
 * Created by yxx03 on 1/23/2015.
 */
public class MessageCommon {
    public final String messageType = MessageCommon.class.getName();
    public String status; //success, denied, notfound, error
    public String msg = "";
    public String fromUserId;
    public String fromMethod;
}
