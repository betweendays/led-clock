package com.tr.ledclock.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.tr.ledclock.R;
import com.xrigau.driver.ws2801.Ws2801;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class WelcomeActivity extends Activity {

    // *************************************** CONSTANTS *************************************** //

    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private static final String GMT_MADRID = "GMT+1:00";
    private static final String GMT_CANBERRA = "GMT+11:00";
    private static final String GMT_WASHINGTON = "GMT-7:00";
    private static final String TIME_FORMAT = "%02d";

    private static final String SPI_DEVICE_NAME = "SPI0.0";

    private static final int MAX_LED_POS = 30;
    private static final int MAX_LEDS_PER_CELL = 7;

    private static final char CHAR_0 = '0';
    private static final char CHAR_1 = '1';
    private static final char CHAR_2 = '2';
    private static final char CHAR_3 = '3';
    private static final char CHAR_4 = '4';
    private static final char CHAR_5 = '5';
    private static final char CHAR_6 = '6';
    private static final char CHAR_7 = '7';
    private static final char CHAR_8 = '8';
    private static final char CHAR_9 = '9';

    private static final Integer[] CHAR_0_VALUES = {0, 1, 2, 3, 4, 5};
    private static final Integer[] CHAR_1_VALUES = {3, 4};
    private static final Integer[] CHAR_2_VALUES = {0, 2, 3, 5, 6};
    private static final Integer[] CHAR_3_VALUES = {2, 3, 4, 5, 6};
    private static final Integer[] CHAR_4_VALUES = {1, 6, 3, 4};
    private static final Integer[] CHAR_5_VALUES = {2, 1, 6, 4, 5};
    private static final Integer[] CHAR_6_VALUES = {0, 1, 2, 4, 5, 6};
    private static final Integer[] CHAR_7_VALUES = {2, 3, 4};
    private static final Integer[] CHAR_8_VALUES = {0, 1, 2, 3, 4, 5, 6};
    private static final Integer[] CHAR_9_VALUES = {1, 2, 3, 4, 6};

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

        // display LEDs with hour from Madrid by default
        displayLeds(GMT_MADRID);
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

    private void displayLeds(String gmt) {
        List<Integer> finalPositions = new ArrayList<>();
        int[] myLeds = new int[MAX_LED_POS];

        // get list of characters from current time
        List<Character> characters = getCurrentTimeCharacters(gmt);

        // get list of positions to be turned on in case of cell 0 and then, add the corresponding
        // offset according to the cell
        for (int cell = 0; cell < characters.size(); cell++) {
            List<Integer> basePositions = getBasePositions(characters.get(cell));
            finalPositions.addAll(addOffset(cell, basePositions));
        }

        // create a vector where positions that have to be turned on, must be the desired color
        // while the rest must be black.
        for (int i = 0; i < myLeds.length; i++) {
            if (finalPositions.contains(i)) {
                myLeds[i] = Color.BLUE;
            } else {
                myLeds[i] = Color.BLACK;
            }
        }
    }

    private List<Integer> addOffset(int cell, List<Integer> basePositions) {
        // this offset is calculated in order to set the LED strip position given that each cell
        // has 7 LEDs.
        int offset = cell * MAX_LEDS_PER_CELL;

        // add offset for each of the array elements according to the cell position
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < basePositions.size(); i++) {
            int pos;
            if (cell > 1) {
                // add another offset for the minutes due to 2 LEDs are for the `:` separator
                pos = basePositions.get(i) + offset + 2;
                positions.add(pos);
            } else {
                pos = basePositions.get(i) + offset;
                positions.add(pos);
            }
            Log.d(TAG, "Position: [" + pos + "]");
        }

        return positions;
    }

    private List<Integer> getBasePositions(Character character) {
        List<Integer> rawPositions = null;
        switch (character) {
            case CHAR_0:
                rawPositions = new ArrayList<>(Arrays.asList(CHAR_0_VALUES));
                break;
            case CHAR_1:
                rawPositions = new ArrayList<>(Arrays.asList(CHAR_1_VALUES));
                break;
            case CHAR_2:
                rawPositions = new ArrayList<>(Arrays.asList(CHAR_2_VALUES));
                break;
            case CHAR_3:
                rawPositions = new ArrayList<>(Arrays.asList(CHAR_3_VALUES));
                break;
            case CHAR_4:
                rawPositions = new ArrayList<>(Arrays.asList(CHAR_4_VALUES));
                break;
            case CHAR_5:
                rawPositions = new ArrayList<>(Arrays.asList(CHAR_5_VALUES));
                break;
            case CHAR_6:
                rawPositions = new ArrayList<>(Arrays.asList(CHAR_6_VALUES));
                break;
            case CHAR_7:
                rawPositions = new ArrayList<>(Arrays.asList(CHAR_7_VALUES));
                break;
            case CHAR_8:
                rawPositions = new ArrayList<>(Arrays.asList(CHAR_8_VALUES));
                break;
            case CHAR_9:
                rawPositions = new ArrayList<>(Arrays.asList(CHAR_9_VALUES));
                break;
            default:
                // do nothing
        }

        if (rawPositions == null) {
            throw new IllegalStateException("Unknown character: " + character);
        }

        return rawPositions;
    }

    private List<Character> getCurrentTimeCharacters(String gmt) {
        List<Character> charactersList = new ArrayList<>();

        // get timezone & calendar
        TimeZone timeZone = TimeZone.getTimeZone(gmt);
        Calendar calendar = Calendar.getInstance(timeZone);

        // retrieve hour & minutes
        int rawHour = calendar.get(Calendar.HOUR_OF_DAY);
        int rawMinute = calendar.get(Calendar.MINUTE);

        // convert hour & minutes in the specific time format
        String hour = String.format(Locale.getDefault(), TIME_FORMAT, rawHour);
        String minutes = String.format(Locale.getDefault(), TIME_FORMAT, rawMinute);
        Log.d(TAG, "Current time = " + hour + ":" + minutes);

        // get characters from string AB:CD
        Character charA = hour.charAt(0);
        Character charB = hour.charAt(1);
        Character charC = minutes.charAt(0);
        Character charD = minutes.charAt(1);

        // add time characters to list
        charactersList.add(charA);
        charactersList.add(charB);
        charactersList.add(charC);
        charactersList.add(charD);

        return charactersList;
    }

    private void setUiElements() {
        mMadridBtn = findViewById(R.id.button_madrid);
        mCanberraBtn = findViewById(R.id.button_canberra);
        mWashington = findViewById(R.id.button_washington);
    }

    private void setButtonsListeners() {
        mMadridBtn.setOnClickListener(view -> {
            Log.d(TAG, "Madrid");
            displayLeds(GMT_MADRID);
        });

        mCanberraBtn.setOnClickListener(view -> {
            Log.d(TAG, "Canberra");
            displayLeds(GMT_CANBERRA);
        });

        mWashington.setOnClickListener(view -> {
            Log.d(TAG, "Washington");
            displayLeds(GMT_WASHINGTON);
        });
    }
}
