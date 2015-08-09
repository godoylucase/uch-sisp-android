package com.uch.sisp.client.gcm.notification;

import android.os.Bundle;

/**
 * Created by lucas on 09/08/15.
 */
public class NotificationHelper {

    public static GCMNotification buildPanicNotification(Bundle data) {
        PanicGCMNotification notification = new PanicGCMNotification();
        notification.setTitle(data.getString("title"));
        notification.setBody(data.getString("body"));
        notification.setLatitude(data.getString("latitude"));
        notification.setAltitude(data.getString("altitude"));
        return notification;
    }
}
