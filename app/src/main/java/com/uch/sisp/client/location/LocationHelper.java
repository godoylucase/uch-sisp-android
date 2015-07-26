package com.uch.sisp.client.location;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.TextView;

import com.uch.sisp.client.R;
import com.uch.sisp.client.exception.NotLocalizableDeviceException;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lucas on 11/07/15.
 */
@Getter @Setter
public class LocationHelper {

    private LocationManager locManager;
    private Context context;
    private Dialog alertDialog;
    private TextView twLatitud;
    private TextView twLongitud;


    public LocationHelper(LocationManager locManager, Context context, TextView twLatitud, TextView twLongitud) {
        this.locManager = locManager;
        this.context = context;
        this.twLatitud = twLatitud;
        this.twLongitud = twLongitud;
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
        return locationProvidersList.contains(LocationManager.NETWORK_PROVIDER);
    }

    private boolean minimalLocatorEnabled(){
        return locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void closeAlertDialog(){
        if(alertDialog != null) {
            alertDialog.cancel();
        }
    }

    public void refreshScreenInfo(Location location) {
        twLatitud.setText("Latitud: " + String.valueOf(location.getLatitude()));
        twLongitud.setText("Longitud: " + String.valueOf(location.getLongitude()));
    }
}
