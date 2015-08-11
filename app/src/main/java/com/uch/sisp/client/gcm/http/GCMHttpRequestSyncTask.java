package com.uch.sisp.client.gcm.http;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.uch.sisp.client.config.SharedPreferencesConstants;
import com.uch.sisp.client.gcm.http.request.GenericRequest;
import com.uch.sisp.client.gcm.http.request.SispServicesTags;
import com.uch.sisp.client.gcm.http.response.GenericResponse;
import com.uch.sisp.client.gcm.http.response.RegisterDeviceResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static com.uch.sisp.client.config.SharedPreferencesConstants.SISP_DEVICE_ID;
import static com.uch.sisp.client.config.SharedPreferencesConstants.USER_EMAIL;


/**
 * Created by lucas on 20/06/15.
 */
public class GCMHttpRequestSyncTask {

    private RestTemplate restTemplate;

    public void processRequest(SharedPreferences sharedPreferences, String token) {
        GenericRequest request = null;
        ResponseEntity<RegisterDeviceResponse> response = null;
        HttpEntity<?> httpEntity = null;
        String url = null;
        try {
            request = HttpHelper.prepareRegisterGCMDeviceRequest(token, sharedPreferences);
            url = HttpHelper.buildURL(sharedPreferences, SispServicesTags.REGISTER);
            httpEntity = setRestTemplateAndHttpEntity(request);
            response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, RegisterDeviceResponse.class);
            Log.d("SISP Response: ", response.getStatusCode().toString());
        } catch (Throwable e) {
            e.printStackTrace();
            Log.e("SISP Exception: ", e.getMessage());
        }
        onPostExecute(sharedPreferences, response);
    }


    private void onPostExecute(SharedPreferences sharedPreferences, ResponseEntity<RegisterDeviceResponse> response) {
        GenericResponse responseBody = (GenericResponse) response.getBody();
        if(responseBody instanceof RegisterDeviceResponse) {
            RegisterDeviceResponse registerDeviceResponse = (RegisterDeviceResponse) responseBody;
            if (response.getStatusCode() == HttpStatus.CREATED) {
                sharedPreferences.edit().putInt(SISP_DEVICE_ID, registerDeviceResponse.getId()).apply();
                sharedPreferences.edit().putString(USER_EMAIL, registerDeviceResponse.getEmail()).apply();
                sharedPreferences.edit().putBoolean(SharedPreferencesConstants.SENT_TOKEN_TO_SERVER, true).apply();
            }
        }
    }

    private HttpEntity<?> setRestTemplateAndHttpEntity(GenericRequest request){
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpEntity<?> httpEntity = new HttpEntity<GenericRequest>(request);
        return httpEntity;
    }
}
