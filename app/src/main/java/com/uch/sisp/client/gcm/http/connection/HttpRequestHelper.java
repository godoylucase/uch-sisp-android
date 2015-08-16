package com.uch.sisp.client.gcm.http.connection;


import com.uch.sisp.client.config.SharedPreferencesConstants;
import com.uch.sisp.client.config.SispServerURLConstants;
import com.uch.sisp.client.gcm.http.connection.bundle.HttpElementsBundle;
import com.uch.sisp.client.gcm.http.connection.bundle.HttpPanicElementsBundle;
import com.uch.sisp.client.gcm.http.connection.bundle.HttpRegisterDeviceElementsBundle;
import com.uch.sisp.client.gcm.http.request.GenericRequest;
import com.uch.sisp.client.gcm.http.request.SendPanicNotificationRequest;
import com.uch.sisp.client.gcm.http.request.RegisterDeviceRequest;
import com.uch.sisp.client.gcm.http.request.vo.GPSPosition;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;

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
        int userId = registerBundle.getSharedPreferences().getInt(SISP_DEVICE_ID, 0);
        if (userId != 0) {
            request.setId(userId);
        } else {
            if (hardCodedEmail != null) {
                request.setEmail(hardCodedEmail);
            }
        }
        request.setRegisterId(registerBundle.getToken());
        return request;
    }

    private static SendPanicNotificationRequest buildPanicRequest(HttpElementsBundle bundle) {
        HttpPanicElementsBundle panicBundle = (HttpPanicElementsBundle) bundle;
        SendPanicNotificationRequest request = new SendPanicNotificationRequest();
        int userId = panicBundle.getSharedPreferences().getInt(SISP_DEVICE_ID, 0);
        GPSPosition position = GPSPosition.builder()
                .latitude(BigDecimal.valueOf(panicBundle.getLocationHelper().getLatitude()))
                .longitude(BigDecimal.valueOf(panicBundle.getLocationHelper().getLongitude()))
                .build();
        request.setId(userId);
        request.setPosition(position);
        return request;
    }
}
