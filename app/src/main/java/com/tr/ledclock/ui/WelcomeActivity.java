package com.tr.ledclock.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.tr.ledclock.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Welcome activity which allows user to configure its clock by selecting the desired city.
 */
public class WelcomeActivity extends Activity {

    // *************************************** CONSTANTS *************************************** //

    // Log tag
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    // GMT for each city
    private static final String GMT_MADRID = "GMT+1:00";
    private static final String GMT_CANBERRA = "GMT+11:00";
    private static final String GMT_WASHINGTON = "GMT-7:00";
    private static final String TIME_FORMAT = "%02d";

    // SPI port to be used for connecting to LEDs
    private static final String SPI_DEVICE_NAME = "SPI0.0";

    // Total number of LEDs & max number per cell AB:CD
    private static final int MAX_LED_POS = 30;
    private static final int MAX_LEDS_PER_CELL = 7;

    // Possible characters
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

    // Positions of two dots AB:CD
    private static final Integer[] TWO_POINTS_VALUES = {15, 16};
    // Positions for each of the possible characters in cell A
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

        // register to a service that notifies when time has changed
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeChangedReceiver, filter);
    }

    @Override
    protected void onStop() {
        // unregister to time changed service
        unregisterReceiver(mTimeChangedReceiver);

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // ************************************ PRIVATE METHODS ************************************ //

    private BroadcastReceiver mTimeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_TIME_TICK)) {
                Log.d(TAG, "Time has changed.");
                // TODO: this must be re-think
                // displayLeds();
            }
        }
    };

    /**
     * Method that displays LEDs (not yet) according to the current time.
     *
     * @param gmt GMT time zone to be used to calculate current time.
     */
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

    /**
     * Method that adds an offset corresponding to the cell which character must be written. For
     * instance, 14:30:
     *  Cell1(A): "1" - Offset: 0
     *  Cell2(B): "4" - Offset: 7
     *  Cell3(C): "3" - Offset: 14 + 2 = 16
     *  Cell4(D): "0" - Offset: 21 + 2 = 23
     *
     * @param cell Cell where character must be printed.
     * @param basePositions List of integers mapping character in Cell1(A).
     * @return List of integers mapping character with the offset according to its cell.
     */
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

    /**
     * Method that provides a list of positions according to the character received corresponding
     * to Cell1(A) - AB:CD.
     *
     * @param character Character to be treated.
     * @return List of positions to be used.
     */
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

        // add two dots positions into array ":"
        rawPositions.addAll(Arrays.asList(TWO_POINTS_VALUES));

        // set positions in ascending order (optional)
        Collections.sort(rawPositions);

        return rawPositions;
    }

    /**
     * Method that retrieves current time in AB:CD format and creates its corresponding list of
     * characters. For instance: 14:30 -> Characters list: "1", "4", "3, "0".
     *
     * @param gmt GMT time zone to be used to calculate current time.
     * @return List of characters according to calculated time.
     */
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

    /**
     * Method that maps UI elements with its corresponding variable to be used later.
     */
    private void setUiElements() {
        mMadridBtn = findViewById(R.id.button_madrid);
        mCanberraBtn = findViewById(R.id.button_canberra);
        mWashington = findViewById(R.id.button_washington);
    }

    /**
     * Method that sets a listener when a user clicks on any of the defined buttons in the UI.
     */
    private void setButtonsListeners() {
        mMadridBtn.setOnClickListener(view -> displayLeds(GMT_MADRID));
        mCanberraBtn.setOnClickListener(view -> displayLeds(GMT_CANBERRA));
        mWashington.setOnClickListener(view -> displayLeds(GMT_WASHINGTON));
    }
}
