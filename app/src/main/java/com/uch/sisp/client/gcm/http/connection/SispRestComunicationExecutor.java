package com.uch.sisp.client.gcm.http.connection;

import com.uch.sisp.client.gcm.http.request.SendPanicNotificationRequest;
import com.uch.sisp.client.gcm.http.request.RegisterDeviceRequest;
import com.uch.sisp.client.gcm.http.response.SendPanicNotificationResponse;
import com.uch.sisp.client.gcm.http.response.RegisterDeviceResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by lucas on 15/08/15.
 */
public class SispRestComunicationExecutor {

    public static ResponseEntity<?> execute(RestTemplate restTemplate, String url,
                                                   HttpMethod httpMethod, HttpEntity<?> httpEntity) {
        ResponseEntity<?> response = null;

        if(httpEntity.getBody() instanceof RegisterDeviceRequest) {
            response = restTemplate.exchange(url, httpMethod, httpEntity, RegisterDeviceResponse.class);
        } else if(httpEntity.getBody() instanceof SendPanicNotificationRequest) {
            response = restTemplate.exchange(url, httpMethod, httpEntity, SendPanicNotificationResponse.class);
        }
        return response;
    }

}
