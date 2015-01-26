package model;

import org.joda.time.DateTime;

/**
 * Created by yxx03 on 1/16/2015.
 */
public class ChartLock {
    DateTime lockTime;
    String userId;

    public ChartLock(final String userId) {
        this.lockTime = new DateTime();
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public DateTime getLockTime() {
        return lockTime;
    }

    @Override
    public String toString() {
        return "ChartLock{" +
                "lockTime=" + lockTime +
                ", userId='" + userId + '\'' +
                '}';
    }
}