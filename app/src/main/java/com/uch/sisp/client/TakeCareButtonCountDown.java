package com.uch.sisp.client;

import android.os.CountDownTimer;
import android.widget.Button;

import com.uch.sisp.client.gcm.http.connection.GCMHttpPanicRequestStandAloneThread;
import com.uch.sisp.client.gcm.http.connection.bundle.HttpPanicElementsBundle;

/**
 * Created by lucas on 16/08/15.
 */
public class TakeCareButtonCountDown extends CountDownTimer {

    private Button btTakeCareOfMe;
    private HttpPanicElementsBundle bundle;

    public TakeCareButtonCountDown(long millisInFuture, long countDownIntervalButton,
                                   Button btTakeCareOfMe, HttpPanicElementsBundle bundle) {
        super(millisInFuture, countDownIntervalButton);
        this.btTakeCareOfMe = btTakeCareOfMe;
        this.bundle = bundle;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btTakeCareOfMe.setText("Cuidame en: " + millisUntilFinished / 1000);
    }

    @Override
    public void onFinish() {
        btTakeCareOfMe.setText(R.string.button_take_care_of_me_text);
        GCMHttpPanicRequestStandAloneThread thread = new GCMHttpPanicRequestStandAloneThread();
        thread.execute(bundle);
    }
}
