package com.uch.sisp.client.location;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.uch.sisp.client.R;
import com.uch.sisp.client.exception.NotLocalizableDeviceException;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lucas on 11/07/15.
 */
@Getter @Setter
public class LocationHelper {

    LocationManager locManager;
    Context context;
    Dialog alertDialog;

    public LocationHelper(LocationManager locManager, Context context) {
        this.locManager = locManager;
        this.context = context;
    }

    public void checkIfDeviceIsLocalizable() throws NotLocalizableDeviceException{
        if(!hasMinimalLocators()){
            throw new NotLocalizableDeviceException();
        }

        if(!minimalLocatorEnabled()){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getString(R.string.alert_not_active_location_message));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });

            alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private boolean hasMinimalLocators() {
        List<String> locationProvidersList = locManager.getAllProviders();
        return locationProvidersList.contains(LocationManager.GPS_PROVIDER)
                || locationProvidersList.contains(LocationManager.NETWORK_PROVIDER);
    }

    private boolean minimalLocatorEnabled(){
        return locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void closeAlertDialog(){
        if(alertDialog != null) {
            alertDialog.cancel();
        }
    }
}
