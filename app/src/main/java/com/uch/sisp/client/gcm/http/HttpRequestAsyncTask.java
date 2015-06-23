package com.uch.sisp.client.gcm.http;

import android.os.AsyncTask;

import com.uch.sisp.client.gcm.http.request.GenericRequest;
import com.uch.sisp.client.gcm.http.response.GenericResponse;

/**
 * Created by lucas on 20/06/15.
 */
public class HttpRequestAsyncTask extends AsyncTask<GenericRequest, Void, GenericResponse> {
    @Override
    protected GenericResponse doInBackground(GenericRequest... params) {
        return null;
    }

    @Override
    protected void onPostExecute(GenericResponse genericResponse) {
        super.onPostExecute(genericResponse);
    }
}
