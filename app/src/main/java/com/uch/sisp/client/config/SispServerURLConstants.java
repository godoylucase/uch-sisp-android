package com.uch.sisp.client.config;

/**
 * Created by lucas on 05/07/15.
 */
public class SispServerURLConstants {
    public static final String SISP_PROTOCOL = "http://";
    public static final String SISP_HOST = "192.168.0.2";
    public static final String SISP_PORT = ":9090";
    public static final String SISP_CONTEXT = "/uch-server-sisp";
    public static final String SISP_URL = SISP_PROTOCOL + SISP_HOST + SISP_PORT;
    public static final String SISP_ACCESS_URL = SISP_URL + SISP_CONTEXT;

    public static final String SISP_SERVICE_REGISTER_GCM_DEVICE = "/gcm/registerGCMDevice/";
    public static final String SISP_SERVICE_UNREGISTER_GCM_DEVICE = "/gcm/unregisterGCMDevice/";
    public static final String SISP_SERVICE_SEND_GCM_NOTIFICATION = "/gcm/sendGCMNotification/";
}
