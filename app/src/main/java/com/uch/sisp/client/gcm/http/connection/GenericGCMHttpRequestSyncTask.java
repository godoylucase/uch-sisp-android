package com.uch.sisp.client.gcm.http.connection;

import android.util.Log;

import com.uch.sisp.client.gcm.http.connection.bundle.HttpElementsBundle;
import com.uch.sisp.client.gcm.http.request.GenericRequest;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


/**
 * Created by lucas on 20/06/15.
 */
public abstract class GenericGCMHttpRequestSyncTask implements GCMHttpRequestInterface {

    protected RestTemplate restTemplate;
    protected HttpElementsBundle bundle;

    public ResponseEntity<?> sendRequest(HttpElementsBundle bundle) {
        ResponseEntity<?> response = null;
        HttpEntity<?> httpEntity;
        try {
            Pair<GenericRequest, String> requestAndUrl = HttpRequestHelper.prepareSispGcmRequestAndUrl(bundle);
            httpEntity = setRequestToRestTemplate(requestAndUrl.getKey());
            response = SispRestComunicationExecutor.execute(restTemplate, requestAndUrl.getValue(),
                    bundle.getServiceTag().getHttpMethod(), httpEntity);
            Log.d("SISP Response: ", response.getStatusCode().toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void onPostExecute(ResponseEntity<?> response) {
        HttpResponseHelper.processResponse(response, bundle);
    }

    private HttpEntity<?> setRequestToRestTemplate(GenericRequest request) {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpEntity<?> httpEntity = new HttpEntity<GenericRequest>(request);
        return httpEntity;
    }
}
