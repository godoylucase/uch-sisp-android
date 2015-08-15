package com.uch.sisp.client.gcm.http.connection;


import com.uch.sisp.client.config.SharedPreferencesConstants;
import com.uch.sisp.client.config.SispServerURLConstants;
import com.uch.sisp.client.gcm.http.connection.bundle.HttpElementsBundle;
import com.uch.sisp.client.gcm.http.connection.bundle.HttpRegisterDeviceElementsBundle;
import com.uch.sisp.client.gcm.http.request.GenericRequest;
import com.uch.sisp.client.gcm.http.request.PanicRequest;
import com.uch.sisp.client.gcm.http.request.RegisterDeviceRequest;

import org.apache.commons.lang3.tuple.Pair;

import static com.uch.sisp.client.config.SharedPreferencesConstants.SISP_DEVICE_ID;
import static com.uch.sisp.client.config.SharedPreferencesConstants.USER_EMAIL;


/**
 * Created by lucas on 10/08/15.
 */
public class HttpRequestHelper {

    public static Pair<GenericRequest, String> prepareSispGcmRequestAndUrl(HttpElementsBundle bundle) {
        GenericRequest request = getGCMRequest(bundle);
        String url = getServiceUrl(bundle);
        return Pair.of(request, url);
    }

    private static String getServiceUrl(HttpElementsBundle bundle) {
        StringBuilder builder = (new StringBuilder(bundle.getSharedPreferences().getString(SharedPreferencesConstants.SISP_ACCESS_URL,
                SispServerURLConstants.SISP_ACCESS_URL))).append((bundle.getServiceTag().getValue()));
        return builder.toString();
    }

    private static GenericRequest getGCMRequest(HttpElementsBundle bundle) {
        GenericRequest request = null;

        switch (bundle.getServiceTag()) {
            case REGISTER:
                request = buildRegisterDeviceRequest(bundle);
                break;
            case PANIC:
                request = buildPanicRequest(bundle);
                break;
            default:
                request = null;
                break;
        }
        return request;
    }

    private static RegisterDeviceRequest buildRegisterDeviceRequest(HttpElementsBundle bundle) {
        HttpRegisterDeviceElementsBundle registerBundle = (HttpRegisterDeviceElementsBundle) bundle;
        RegisterDeviceRequest request = new RegisterDeviceRequest();
        String hardCodedEmail = registerBundle.getSharedPreferences().getString(USER_EMAIL, null);
        int deviceId = registerBundle.getSharedPreferences().getInt(SISP_DEVICE_ID, 0);
        if(deviceId != 0) {
            request.setId(deviceId);
        } else {
            if(hardCodedEmail != null){
                request.setEmail(hardCodedEmail);
            }
        }
        request.setRegisterId(registerBundle.getToken());
        return request;
    }

    private static PanicRequest buildPanicRequest(HttpElementsBundle bundle) {
        // TODO: implementar
        return null;
    }
}
