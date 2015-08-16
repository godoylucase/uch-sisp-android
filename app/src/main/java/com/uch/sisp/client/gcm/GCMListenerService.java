package com.uch.sisp.client.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.uch.sisp.client.R;
import com.uch.sisp.client.SispMainActivity;
import com.uch.sisp.client.gcm.notification.GCMNotification;
import com.uch.sisp.client.gcm.notification.NotificationHelper;
import com.uch.sisp.client.gcm.notification.PanicGCMNotification;

/**
 * Created by lucas on 20/06/15.
 */
public class GCMListenerService extends GcmListenerService {

    private static final String TAG = "GcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        GCMNotification notification = NotificationHelper.buildPanicNotification(data);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + notification.getTitle());

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(notification);
    }
    // [END receive_message]

    private void sendNotification(GCMNotification notification) {
        Intent intent = new Intent(this, SispMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // extrae los datos de la posicion y los pasa como extras en el intent, solo a fines de demo
        PanicGCMNotification panicNotification = (PanicGCMNotification) notification;
        intent.putExtra("latitude", panicNotification.getLatitude());
        intent.putExtra("longitude", panicNotification.getLongitude());
        intent.putExtra("origin", panicNotification.getBody());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}