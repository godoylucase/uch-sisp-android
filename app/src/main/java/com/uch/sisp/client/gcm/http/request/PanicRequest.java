package com.uch.sisp.client.gcm.http.request;

import com.uch.sisp.client.gcm.http.request.vo.GPSPosition;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lucas on 15/08/15.
 */
@Getter @Setter
public class PanicRequest implements GenericRequest, Serializable {
    GPSPosition position;
}
