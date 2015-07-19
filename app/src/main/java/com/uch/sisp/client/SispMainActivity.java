package com.uch.sisp.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.uch.sisp.client.account.GoogleAccountHelper;
import com.uch.sisp.client.exception.NotLocalizableDeviceException;
import com.uch.sisp.client.gcm.GCMRegistrationIntentService;
import com.uch.sisp.client.location.LocationHelper;
import com.uch.sisp.client.location.LocationListenerImpl;

import static com.uch.sisp.client.config.IntentConstants.*;
import static com.uch.sisp.client.config.SharedPreferencesConstants.*;


public class SispMainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SispMainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Toast toastInfoMessage;
    private LocationHelper locationHelper;
    private LocationListenerImpl locationListener;
    private SharedPreferences sharedPreferences;
    private TextView twLatitud;
    private TextView twLongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sisp_main);
        initializeUIComponents();
        locationHelper = new LocationHelper((LocationManager)getSystemService(LOCATION_SERVICE), this, twLatitud, twLongitud);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // BroadcasReceiver se dispara tras recibir el token de GCM y ser enviado al server SISP
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean sentToken = sharedPreferences.getBoolean(SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    toastInfoMessage = Toast.makeText(getApplicationContext(),
                            getString(R.string.gcm_send_message), Toast.LENGTH_LONG);
                } else {
                    toastInfoMessage = Toast.makeText(getApplicationContext(),
                            getString(R.string.token_error_message), Toast.LENGTH_LONG);
                }
                toastInfoMessage.show();
            }
        };
        initializeApplication();
    }

    private void initializeUIComponents() {
        twLatitud = (TextView) findViewById(R.id.text_view_latitud);
        twLongitud = (TextView) findViewById(R.id.text_view_longitud);
    }

    private void initializeApplication() {
        try {
            // Al iniciar chequea que GPS y Localización por red estén disponibles
            locationHelper.checkIfDeviceIsLocalizable();

            // Inicializa el proveedor de GPS y arranca el proceso de actualizaciones
            String locProvider = sharedPreferences.getString(LOCATOR_PROVIDER, LocationManager.NETWORK_PROVIDER);
            locationListener = new LocationListenerImpl(locProvider, locationHelper);

            if (checkPlayServices()) {
                // Inicia un IntentService para registar en GCM la aplicación.
                Intent intent = new Intent(this, GCMRegistrationIntentService.class);
                intent.putExtra(EMAIL_INTENT_PARAMETER, GoogleAccountHelper.getPrincipalEmailAccount(this));
                startService(intent);
            }
        } catch (NotLocalizableDeviceException e) {
            //TODO: implementar la salida de dispositivo incompatible
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            locationHelper.closeAlertDialog();
            locationHelper.checkIfDeviceIsLocalizable();
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(REGISTRATION_COMPLETE));
//            if(!locationListener.getLocProvider().equals(sharedPreferences.getString(LOCATOR_PROVIDER, null))){
//                locationListener.onProviderPreferenceChange(sharedPreferences.getString(LOCATOR_PROVIDER, null));
//            }
        } catch (NotLocalizableDeviceException e) {
            //TODO: implementar la salida de dispositivo incompatible
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * @return
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "Éste dispositivo no es soportado");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sisp_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ajustes:
                Intent settings = new Intent(SispMainActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
