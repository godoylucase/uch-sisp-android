package com.uch.sisp.client.gcm.http.connection;

import com.uch.sisp.client.gcm.http.connection.bundle.HttpElementsBundle;

import org.springframework.http.ResponseEntity;

/**
 * Created by lucas on 15/08/15.
 */
public class GCMHttpRequestSyncTask extends GenericGCMHttpRequestSyncTask {

    public GCMHttpRequestSyncTask(HttpElementsBundle bundle) {
        super();
        this.bundle = bundle;
    }

    @Override
    public void processRequest() {
        ResponseEntity<?> response = super.sendRequest(bundle);
        onPostExecute(response);
    }
}
