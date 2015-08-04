package com.uch.sisp.client.gcm;

import static com.uch.sisp.client.config.SispServerURLConstants.*;
import static com.uch.sisp.client.config.IntentConstants.*;
import static com.uch.sisp.client.config.SharedPreferencesConstants.*;


import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.uch.sisp.client.R;
import com.uch.sisp.client.config.SharedPreferencesConstants;
import com.uch.sisp.client.config.SispServerURLConstants;
import com.uch.sisp.client.gcm.http.request.RegisterDeviceRequest;
import com.uch.sisp.client.gcm.http.response.RegisterDeviceResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by lucas on 20/06/15.
 */
public class GCMRegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public GCMRegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // [START get_token]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                sendRegistrationToServer(token, sharedPreferences, intent);

                // Subscribe to topic channels
                subscribeTopics(token);

                // [END get_token]
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(SharedPreferencesConstants.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(SharedPreferencesConstants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     *Registra en el servidor SISP el token provisto por GCM para
     * el envío de notificaciones push, y almacena el id del dispositivo en
     * las SharedPreferences
     *
     * @param token
     * @param sharedPreferences
     * @param intent
     */
    private void sendRegistrationToServer(String token, SharedPreferences sharedPreferences, Intent intent) {
        RegisterDeviceRequest request = prepareRegisterGCMDeviceRequest(token, sharedPreferences, intent);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpEntity<?> httpEntity = new HttpEntity<RegisterDeviceRequest>(request);

        StringBuilder url = (new StringBuilder(
                sharedPreferences.getString(SharedPreferencesConstants.SISP_ACCESS_URL, SispServerURLConstants.SISP_ACCESS_URL)))
                .append((SISP_SERVICE_REGISTER_GCM_DEVICE));
        ResponseEntity<RegisterDeviceResponse> response = null;
        try {
            response = restTemplate.exchange(url.toString(), HttpMethod.POST, httpEntity, RegisterDeviceResponse.class);
            Log.d("SISP Response: ", response.getStatusCode().toString());
            RegisterDeviceResponse responseBody = (RegisterDeviceResponse) response.getBody();
            if (response.getStatusCode() == HttpStatus.CREATED) {
                sharedPreferences.edit().putInt(SISP_DEVICE_ID, responseBody.getId()).apply();
                sharedPreferences.edit().putString(USER_EMAIL, responseBody.getEmail()).apply();
                sharedPreferences.edit().putBoolean(SharedPreferencesConstants.SENT_TOKEN_TO_SERVER, true).apply();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Log.e("SISP Exception: ", e.getMessage());
        }
    }

    private RegisterDeviceRequest prepareRegisterGCMDeviceRequest(String token, SharedPreferences sharedPreferences, Intent intent){
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

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}
