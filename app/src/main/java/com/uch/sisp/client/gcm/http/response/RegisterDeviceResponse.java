package com.uch.sisp.client.gcm.http.response;

/**
 * Created by lucas on 23/06/15.
 */
public class RegisterDeviceResponse extends GenericResponse {

    private int id;
    private String registerId;

    public int getId()
    {
        return id;
    }

    public String getRegisterId()
    {
        return registerId;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setRegisterId(String registerId)
    {
        this.registerId = registerId;
    }
}
