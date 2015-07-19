package com.uch.sisp.client.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lucas on 18/07/15.
 */
@Getter @Setter
public class LocationListenerImpl implements LocationListener, Serializable {

    // TODO: implementar cambio de proveedor en settings para refrescar referencia al locProvider
    private static long REFRESH_TIME = 15000;
    private static float MIN_DISTANCE = 0;
    private String locProvider = null;
    private LocationManager locationManager = null;
    private Location currentLocation = null;
    private LocationHelper locationHelper;

    public LocationListenerImpl(String locProvider, LocationHelper locationHelper) {
        this.locationHelper = locationHelper;
        this.locationManager = locationHelper.getLocManager();

        this.locProvider = locProvider != null ? locProvider : LocationManager.NETWORK_PROVIDER;

        currentLocation = locationManager.getLastKnownLocation(locProvider);
        init();
    }

    public void init() {
        locationManager.requestLocationUpdates(this.locProvider, REFRESH_TIME, MIN_DISTANCE, this);
    }

    public void finish() {
        locationManager.removeUpdates(this);
    }

    public void onProviderPreferenceChange(String newProvider) {
        finish();
        locProvider = newProvider.equals(locProvider) ? locProvider : newProvider;
        init();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        locationHelper.refreshScreenInfo(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
