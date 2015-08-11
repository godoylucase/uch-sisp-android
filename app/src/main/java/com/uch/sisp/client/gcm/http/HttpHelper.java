package com.uch.sisp.client.gcm.http;


import android.content.SharedPreferences;

import com.uch.sisp.client.config.SharedPreferencesConstants;
import com.uch.sisp.client.config.SispServerURLConstants;
import com.uch.sisp.client.gcm.http.request.RegisterDeviceRequest;
import com.uch.sisp.client.gcm.http.request.SispServicesTags;

import static com.uch.sisp.client.config.SharedPreferencesConstants.SISP_DEVICE_ID;
import static com.uch.sisp.client.config.SharedPreferencesConstants.USER_EMAIL;


/**
 * Created by lucas on 10/08/15.
 */
public class HttpHelper {

    public static String buildURL(SharedPreferences sharedPreferences, SispServicesTags service) {
        StringBuilder builder = (new StringBuilder(sharedPreferences.getString(SharedPreferencesConstants.SISP_ACCESS_URL,
                SispServerURLConstants.SISP_ACCESS_URL))).append((service.getValue()));
        return builder.toString();
    }

    public static RegisterDeviceRequest prepareRegisterGCMDeviceRequest(String token, SharedPreferences sharedPreferences){
        RegisterDeviceRequest request = new RegisterDeviceRequest();
        String hardCodedEmail = sharedPreferences.getString(USER_EMAIL, null);
        int deviceId = sharedPreferences.getInt(SISP_DEVICE_ID, 0);
        if(deviceId != 0) {
            request.setId(deviceId);
        } else {
            if(hardCodedEmail != null){
                request.setEmail(hardCodedEmail);
            }
        }
        request.setRegisterId(token);
        return request;
    }

    public static RegisterDeviceRequest preparePanicGCMRequest(){
        return null;
    }
}
