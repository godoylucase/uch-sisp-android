package com.uch.sisp.client.gcm.http.connection;

import android.os.AsyncTask;

import com.uch.sisp.client.gcm.http.connection.bundle.HttpPanicElementsBundle;

/**
 * Created by lucas on 16/08/15.
 */
public class GCMHttpPanicRequestStandAloneThread extends AsyncTask<HttpPanicElementsBundle, Void, Void> {
    @Override
    protected Void doInBackground(HttpPanicElementsBundle... params) {
        GCMHttpRequestSyncTask gcmConnection = new GCMHttpRequestSyncTask(params[0]);
        gcmConnection.processRequest();
        return null;
    }
}
