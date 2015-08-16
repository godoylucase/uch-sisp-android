package com.uch.sisp.client.maps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by lucas on 16/08/15.
 */
public class MapHelper {

    public static void moveToPanicNotification(Context context, GoogleMap map, String origin, String latitude, String longitude) {
        LatLng panicPoint = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        addMarkerToMap(context, map, panicPoint, origin);
        moveCamera(map, panicPoint);
    }

    private static void moveCamera(GoogleMap mMap, LatLng position) {
        CameraPosition camPos = new CameraPosition.Builder()
                .target(position)   //Centramos el mapa en el punto
                .zoom(19)           //Establecemos el zoom en 19
                .tilt(70)           //Bajamos el punto de vista de la cámara 70 grados
                .build();
        CameraUpdate cameraPosition = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(cameraPosition);
    }

    private static String getAddressForLocation(Context context, LatLng position) {
        String streetName = null;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(position.latitude, position.longitude, 1);
            if (addresses.size() > 0) {
                streetName = addresses.get(0).getThoroughfare();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return streetName;
    }

    private static void addMarkerToMap(Context context, GoogleMap map, LatLng position, String origin) {
        String streetName = getAddressForLocation(context, position);
        map.addMarker(new MarkerOptions()
                .position(position)
                .title("Notificación de Panico de: " + origin)
                .snippet("Ocurrió en: " + streetName));
    }
}
