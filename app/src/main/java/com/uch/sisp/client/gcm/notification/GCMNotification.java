package com.uch.sisp.client.gcm.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lucas on 09/08/15.
 */
@Setter @Getter
public abstract class GCMNotification {
    private String title;
    private String body;
}
