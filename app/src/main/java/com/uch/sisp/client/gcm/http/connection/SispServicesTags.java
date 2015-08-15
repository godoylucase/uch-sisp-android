package com.uch.sisp.client.gcm.http.connection;

/**
 * Created by lucas on 10/08/15.
 */
public enum SispServicesTags {

    REGISTER("/gcm/registerGCMDevice/"),
    UNREGISTER("/gcm/unregisterGCMDevice/"),
    PANIC("/gcm/sendGCMNotification/");

    private String value;

    private SispServicesTags(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
