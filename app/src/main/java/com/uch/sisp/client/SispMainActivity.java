package com.uch.sisp.client;

import static com.uch.sisp.client.config.IntentConstants.*;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.uch.sisp.client.account.GoogleAccountHelper;
import com.uch.sisp.client.config.SharedPreferencesConstants;
import com.uch.sisp.client.exception.NotLocalizableDeviceException;
import com.uch.sisp.client.gcm.GCMRegistrationIntentService;
import com.uch.sisp.client.location.LocationHelper;


public class SispMainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SispMainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Toast toastInfoMessage;
    private LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sisp_main);
        locationHelper = new LocationHelper((LocationManager)getSystemService(LOCATION_SERVICE), this);

        try {
            // Al iniciar chequea que GPS y Localización por red estén disponibles
            locationHelper.checkIfDeviceIsLocalizable();

            // BroadcasReceiver se dispara tras recibir el token de GCM y ser enviado al server SISP
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(SharedPreferencesConstants.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        toastInfoMessage.makeText(getApplicationContext(), getString(R.string.gcm_send_message), Toast.LENGTH_LONG);
                    } else {
                        toastInfoMessage.makeText(getApplicationContext(), getString(R.string.token_error_message), Toast.LENGTH_LONG);
                    }
                }
            };

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
                    new IntentFilter(SharedPreferencesConstants.REGISTRATION_COMPLETE));
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
                startActivity(new Intent(SispMainActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
