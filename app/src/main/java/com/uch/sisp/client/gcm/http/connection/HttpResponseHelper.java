package com.uch.sisp.client.gcm.http.connection;

import android.widget.Toast;

import com.uch.sisp.client.R;
import com.uch.sisp.client.config.SharedPreferencesConstants;
import com.uch.sisp.client.gcm.http.connection.bundle.HttpElementsBundle;
import com.uch.sisp.client.gcm.http.connection.bundle.HttpPanicElementsBundle;
import com.uch.sisp.client.gcm.http.response.RegisterDeviceResponse;
import com.uch.sisp.client.gcm.http.response.SendPanicNotificationResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.uch.sisp.client.config.SharedPreferencesConstants.SISP_DEVICE_ID;

/**
 * Created by lucas on 15/08/15.
 */
public class HttpResponseHelper {

    public static void processResponse(ResponseEntity<?> response, HttpElementsBundle bundle) {
        if (response.getBody() instanceof RegisterDeviceResponse) {
            RegisterDeviceResponse registerDeviceResponse = (RegisterDeviceResponse) response.getBody();
            if (response.getStatusCode() == HttpStatus.CREATED) {
                bundle.getSharedPreferences().edit().putInt(SISP_DEVICE_ID, registerDeviceResponse.getId()).apply();
                bundle.getSharedPreferences().edit().putBoolean(SharedPreferencesConstants.SENT_TOKEN_TO_SERVER, true).apply();
            }
        } else if (response.getBody() instanceof SendPanicNotificationResponse) {
            // TODO: eliminar esto, es solo para la demo
            Toast toastInfoMessage = null;
            HttpPanicElementsBundle panicBundle = (HttpPanicElementsBundle) bundle;
            if(response.getStatusCode().equals(HttpStatus.CREATED)) {
                toastInfoMessage = Toast.makeText(panicBundle.getContext(), R.string.sent_panic, Toast.LENGTH_LONG);
            } else {
                toastInfoMessage = Toast.makeText(panicBundle.getContext(), R.string.no_sent_panic, Toast.LENGTH_LONG);
            }
            toastInfoMessage.show();
        }
    }
}
