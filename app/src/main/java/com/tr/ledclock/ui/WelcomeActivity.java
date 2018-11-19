package com.tr.ledclock.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.tr.ledclock.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class WelcomeActivity extends Activity {

    // *************************************** CONSTANTS *************************************** //

    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private static final String GMT_MADRID = "GMT+1:00";
    private static final String GMT_CANBERRA = "GMT+11:00";
    private static final String GMT_WASHINGTON = "GMT-7:00";
    private static final String TIME_FORMAT = "%02d";

    // ****************************************** VARS ***************************************** //

    private Button mMadridBtn;
    private Button mCanberraBtn;
    private Button mWashington;

    // *************************************** LIFECYCLE *************************************** //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setUiElements();
        setButtonsListeners();

        // set time zone by default
        showCurrentTime(GMT_MADRID);

        List<Character> characters = getCurrentTimeCharacters(GMT_MADRID);
        for (Character character : characters) {
            Log.d(TAG, "Character: " + character);
        }

        /*Ws2801 ledstrip = Ws2801.create(SPI_DEVICE_NAME, Ws2801.Mode.RGB);
        ledstrip.write();
        ledstrip.close();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        // quan l'app es mostri (no per primer cop)
    }

    @Override
    protected void onStop() {
        super.onStop();
        // quan l'app es queda en background
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // quan l'app estigui a punt de morir
    }

    // ************************************ PRIVATE METHODS ************************************ //

    private void setUiElements() {
        mMadridBtn = findViewById(R.id.button_madrid);
        mCanberraBtn = findViewById(R.id.button_canberra);
        mWashington = findViewById(R.id.button_washington);
    }

    private void setButtonsListeners() {
        mMadridBtn.setOnClickListener(view -> {
            Log.d(TAG, "Madrid");
            showCurrentTime(GMT_MADRID);
        });

        mCanberraBtn.setOnClickListener(view -> {
            Log.d(TAG, "Canberra");
            showCurrentTime(GMT_CANBERRA);
        });

        mWashington.setOnClickListener(view -> {
            Log.d(TAG, "Washington");
            showCurrentTime(GMT_WASHINGTON);
        });
    }

    private void showCurrentTime(String gmt) {
        TimeZone timeZone = TimeZone.getTimeZone(gmt);
        Calendar calendar = Calendar.getInstance(timeZone);

        String hour = String.format(Locale.getDefault(), TIME_FORMAT, calendar.get(Calendar.HOUR_OF_DAY));
        String minutes = String.format(Locale.getDefault(), TIME_FORMAT, calendar.get(Calendar.MINUTE));

        Log.d(TAG, "Current time = " + hour + ":" + minutes);
    }

    private List<Character> getCurrentTimeCharacters(String gmt) {
        List<Character> charactersList = new ArrayList<>();

        TimeZone timeZone = TimeZone.getTimeZone(gmt);
        Calendar calendar = Calendar.getInstance(timeZone);

        String hour = String.format(Locale.getDefault(), TIME_FORMAT, calendar.get(Calendar.HOUR_OF_DAY));
        String minutes = String.format(Locale.getDefault(), TIME_FORMAT, calendar.get(Calendar.MINUTE));

        Log.d(TAG, "Current time = " + hour + ":" + minutes);

        charactersList.add(hour.charAt(0));
        charactersList.add(hour.charAt(1));
        charactersList.add(minutes.charAt(0));
        charactersList.add(minutes.charAt(1));

        return charactersList;
    }
}
