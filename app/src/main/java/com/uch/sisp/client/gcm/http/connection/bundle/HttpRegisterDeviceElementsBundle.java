package com.uch.sisp.client.gcm.http.connection.bundle;

import android.content.SharedPreferences;

import com.uch.sisp.client.gcm.http.connection.SispServicesTags;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lucas on 15/08/15.
 */
@Getter @Setter
public class HttpRegisterDeviceElementsBundle extends HttpElementsBundle {
    private String token;

    public HttpRegisterDeviceElementsBundle(SispServicesTags serviceTag, SharedPreferences sharedPreferences, String token) {
        super();
        this.serviceTag = serviceTag;
        this.sharedPreferences = sharedPreferences;
        this.token = token;
    }
}
