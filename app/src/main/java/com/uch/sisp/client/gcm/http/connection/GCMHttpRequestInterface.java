package com.uch.sisp.client.gcm.http.connection;

import org.springframework.http.ResponseEntity;

/**
 * Created by lucas on 15/08/15.
 */
public interface GCMHttpRequestInterface {
    void onPostExecute(ResponseEntity<?> response);
    void processRequest();
}
