package com.tr.ledclock;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.widget.Button;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static com.tr.ledclock.ClockConfig.City.Washington;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github
 * .com/androidthings/contrib-drivers#readme</a>
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
        try {
            mLedDisplayer = new LedStripDisplayer(TAG);
        } catch (IOException e) {
            Log.e(TAG, "Error when initialising LED displayer.", e);
            showDialog("Ha habido un error iniciando los componentes.");
            return;
        }

        displayLeds();

        // set UI references from the view
        mMadridBtn = findViewById(R.id.btn_madrid);

        // set buttons listeners to handle actions
        mMadridBtn.setOnClickListener(view -> mClockConfig.setCity(ClockConfig.City.Madrid));
    }

    // ************************************ PRIVATE METHODS ************************************ //

    private void displayLeds() {
        int[] matrix = mMatrix.generate(mClockConfig.getTime());
        try {
            mLedDisplayer.display(matrix, mClockConfig);
        } catch (IOException e) {
            Log.e(TAG, "Error when displaying time.", e);
            showDialog("Ha habido un error mostrando la hora.");
        }
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Atención");
        builder.setMessage(message);

        // add buttons
        builder.setPositiveButton(R.string.app_name, (dialog, id) -> {
            // aquí s'executa el codi quan l'usuari apreta el botó "OK"
            dialog.dismiss();
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
