package com.uch.sisp.client.gcm.http.request;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lucas on 23/06/15.
 */
@Getter @Setter
public class RegisterDeviceRequest implements Serializable
{
    private int id;
    private String email;
    private String registerId;

}
