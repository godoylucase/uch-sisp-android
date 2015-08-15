package com.uch.sisp.client.config;

/**
 * Created by lucas on 05/07/15.
 */
public class SispServerURLConstants {
    public static final String SISP_PROTOCOL = "http://";
    public static final String SISP_HOST = "192.168.0.11";
    public static final String SISP_PORT = ":9090";
    public static final String SISP_CONTEXT = "/uch-server-sisp";
    public static final String SISP_URL = SISP_PROTOCOL + SISP_HOST + SISP_PORT;
    public static final String SISP_ACCESS_URL = SISP_URL + SISP_CONTEXT;
}
