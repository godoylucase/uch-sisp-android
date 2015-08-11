package com.uch.sisp.client.gcm.http.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lucas on 23/06/15.
 */
@Getter @Setter
public class RegisterDeviceResponse extends GenericResponse implements Serializable {

    private int id;
    private String email;
    private String registerId;


}
