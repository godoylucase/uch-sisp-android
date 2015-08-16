package com.uch.sisp.client.gcm.http.connection.bundle;

import android.content.Context;
import android.content.SharedPreferences;

import com.uch.sisp.client.gcm.http.connection.SispServicesTags;
import com.uch.sisp.client.location.LocationHelper;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lucas on 15/08/15.
 */
@Getter @Setter
public class HttpPanicElementsBundle extends HttpElementsBundle{
    private LocationHelper locationHelper;
    private Context context;

    public HttpPanicElementsBundle(SispServicesTags serviceTag, SharedPreferences sharedPreferences, LocationHelper locationHelper, Context context) {
        super();
        this.serviceTag = serviceTag;
        this.sharedPreferences = sharedPreferences;
        this.locationHelper = locationHelper;
        this.context = context;
    }
}
