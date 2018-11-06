package com.tr.ledclock;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;

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

    // *************************************** LIFECYCLE *************************************** //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // init dependencies
        mClockConfig = new ClockConfig(TAG);
        mMatrix = new MatrixGenerator(TAG);
        try {
            mLedDisplayer = new LedStripDisplayer(TAG);
        } catch (IOException e) {
            Log.e(TAG, "Error when initialising LED displayer.", e);
            showDialog(getString(R.string.error_init));
            return;
        }

        displayLeds();

        // set UI references from the view
        mMadridBtn = findViewById(R.id.btn_madrid);

        // set buttons listeners to handle actions
        mMadridBtn.setOnClickListener(view -> {
            mClockConfig.setCity(ClockConfig.City.MADRID);
            mMadridBtn.setEnabled(false);
        });
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

    private void displayLeds() {
        int[] matrix = mMatrix.generate(mClockConfig.getTime());
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
        builder.setPositiveButton(R.string.app_name, (dialog, id) -> {
            dialog.dismiss();
        });

        // create & show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
