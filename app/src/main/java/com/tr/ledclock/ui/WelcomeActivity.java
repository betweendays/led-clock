package com.tr.ledclock.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.tr.ledclock.ClockConfig;
import com.tr.ledclock.LedStripDisplayer;
import com.tr.ledclock.MatrixGenerator;
import com.tr.ledclock.R;

import java.io.IOException;
import java.util.Map;

/**
 * Welcome activity for this Android Things project.
 */
public class WelcomeActivity extends Activity {

    // *************************************** CONSTANTS *************************************** //

    private static final String TAG = WelcomeActivity.class.getSimpleName();

    // ****************************************** VARS ***************************************** //

    private ClockConfig mClockConfig;
    private LedStripDisplayer mLedDisplayer;
    private MatrixGenerator mMatrix;

    // ***************************************** VIEWS ***************************************** //

    private Button mMadridBtn;
    private Button mCanberraBtn;
    private Button mWashingtonBtn;

    // *************************************** LIFECYCLE *************************************** //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // init dependencies
        mClockConfig = new ClockConfig(TAG);
        mMatrix = new MatrixGenerator();
        try {
            mLedDisplayer = new LedStripDisplayer(TAG);
        } catch (IOException e) {
            Log.e(TAG, "Error when initialising LED displayer.", e);
            showDialog(getString(R.string.error_init));
            return;
        }

        // set UI references from the view
        mMadridBtn = findViewById(R.id.btn_madrid);
        mCanberraBtn = findViewById(R.id.btn_canberra);
        mWashingtonBtn = findViewById(R.id.btn_washington);

        displayLeds();

        // user clicks on Madrid
        mMadridBtn.setOnClickListener(view -> {
            mClockConfig.setCity(ClockConfig.City.MADRID);
            mMadridBtn.setEnabled(false);
            mCanberraBtn.setEnabled(true);
            mWashingtonBtn.setEnabled(true);
        });

        // user clicks on Canberra
        mCanberraBtn.setOnClickListener(view -> {
            mClockConfig.setCity(ClockConfig.City.CANBERRA);
            mCanberraBtn.setEnabled(false);
            mMadridBtn.setEnabled(true);
            mWashingtonBtn.setEnabled(true);
        });

        // user clicks on Washington
        mWashingtonBtn.setOnClickListener(view -> {
            mClockConfig.setCity(ClockConfig.City.WASHINGTON);
            mWashingtonBtn.setEnabled(false);
            mCanberraBtn.setEnabled(true);
            mMadridBtn.setEnabled(true);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // register to get notified when time changes
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(timeChangedListener, filter);
    }

    @Override
    protected void onStop() {
        // unregister to stop being notified when time changes
        unregisterReceiver(timeChangedListener);
        try {
            mLedDisplayer.stop();
        } catch (IOException e) {
            Log.e(TAG, "Error when stopping LED displayer.", e);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
            mLedDisplayer.stop();
        } catch (IOException e) {
            Log.e(TAG, "Exception closing LED strip.", e);
        }
        super.onDestroy();
    }
    // ************************************ PRIVATE METHODS ************************************ //

    private final BroadcastReceiver timeChangedListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_TIME_TICK)) {
                Log.d(TAG, "Time changed.");
                displayLeds();
            }
        }
    };

    private void displayLeds() {
        // get time in a char map format
        Map<Integer, Character> charMap = mClockConfig.getTimeCharMap();

        boolean[] matrix = mMatrix.generate(charMap);
        try {
            mLedDisplayer.display(matrix, mClockConfig);
        } catch (IOException e) {
            Log.e(TAG, "Error when displaying time.", e);
            showDialog(getString(R.string.error_displaying));
        }
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.title_warning);
        builder.setMessage(message);

        // add buttons
        builder.setPositiveButton(R.string.app_name, (dialog, id) -> dialog.dismiss());

        // create & show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
