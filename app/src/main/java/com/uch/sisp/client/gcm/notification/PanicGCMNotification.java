package com.uch.sisp.client.gcm.notification;

/**
 * Created by lucas on 09/08/15.
 */

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class PanicGCMNotification extends GCMNotification {
    private String latitude;
    private String altitude;

    public PanicGCMNotification() {
        super();
    }
}
