package com.uch.sisp.client.gcm.http.connection;

import com.uch.sisp.client.config.SharedPreferencesConstants;
import com.uch.sisp.client.gcm.http.connection.bundle.HttpElementsBundle;
import com.uch.sisp.client.gcm.http.response.RegisterDeviceResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.uch.sisp.client.config.SharedPreferencesConstants.SISP_DEVICE_ID;

/**
 * Created by lucas on 15/08/15.
 */
public class HttpResponseHelper {

    public static void processResponse(ResponseEntity<?> response, HttpElementsBundle bundle) {
        if (response.getBody() instanceof RegisterDeviceResponse){
            RegisterDeviceResponse registerDeviceResponse = (RegisterDeviceResponse) response.getBody();
            if (response.getStatusCode() == HttpStatus.CREATED) {
                bundle.getSharedPreferences().edit().putInt(SISP_DEVICE_ID, registerDeviceResponse.getId()).apply();
                bundle.getSharedPreferences().edit().putBoolean(SharedPreferencesConstants.SENT_TOKEN_TO_SERVER, true).apply();
            }
        }
    }
}
