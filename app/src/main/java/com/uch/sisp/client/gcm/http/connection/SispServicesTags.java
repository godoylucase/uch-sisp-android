package com.uch.sisp.client.gcm.http.connection;

import org.springframework.http.HttpMethod;

/**
 * Created by lucas on 10/08/15.
 */
public enum SispServicesTags {

    REGISTER("/gcm/registerGCMDevice/", HttpMethod.POST),
    UNREGISTER("/gcm/unregisterGCMDevice/", HttpMethod.POST),
    PANIC("/gcm/sendPanicNotification/", HttpMethod.POST);

    private String value;
    private HttpMethod httpMethod;

    private SispServicesTags(String value, HttpMethod httpMethod) {
        this.value = value;
        this.httpMethod = httpMethod;
    }

    public String getValue() {
        return value;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
}
