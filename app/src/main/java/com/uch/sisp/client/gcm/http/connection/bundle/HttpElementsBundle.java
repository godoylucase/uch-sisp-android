package com.uch.sisp.client.gcm.http.connection.bundle;

import android.content.SharedPreferences;

import com.uch.sisp.client.gcm.http.connection.SispServicesTags;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lucas on 15/08/15.
 */
@Getter @Setter
public abstract class HttpElementsBundle {
    protected SispServicesTags serviceTag;
    protected SharedPreferences sharedPreferences;
}
