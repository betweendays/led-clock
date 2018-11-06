package com.tr.ledclock;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for parsing time characters and generate a matrix to indicate which LEDs must
 * be turned on according to a given time.
 */
public class MatrixGenerator {

    // *************************************** CONSTANTS *************************************** //

    private static final int MAX_LED_POS = 28;

    private static final int MAX_LEDS_PER_CELL = 7;

    private static final int CHAR_0 = 0;
    private static final int CHAR_1 = 1;
    private static final int CHAR_2 = 2;
    private static final int CHAR_3 = 3;
    private static final int CHAR_4 = 4;
    private static final int CHAR_5 = 5;
    private static final int CHAR_6 = 6;
    private static final int CHAR_7 = 7;
    private static final int CHAR_8 = 8;
    private static final int CHAR_9 = 9;

    private static final Integer[] CHAR_0_VALUES = {0, 1, 2};
    private static final Integer[] CHAR_1_VALUES = {0, 1, 2};
    private static final Integer[] CHAR_2_VALUES = {0, 1, 2};
    private static final Integer[] CHAR_3_VALUES = {0, 1, 2};
    private static final Integer[] CHAR_4_VALUES = {0, 1, 2};
    private static final Integer[] CHAR_5_VALUES = {0, 1, 2};
    private static final Integer[] CHAR_6_VALUES = {0, 1, 2};
    private static final Integer[] CHAR_7_VALUES = {0, 1, 2};
    private static final Integer[] CHAR_8_VALUES = {0, 1, 2};
    private static final Integer[] CHAR_9_VALUES = {0, 1, 2};

    // ****************************************** VARS ***************************************** //

    private final String mTag;

    // ************************************** CONSTRUCTORS ************************************* //

    public MatrixGenerator(String logTag) {
        mTag = logTag;
    }

    // ************************************* PUBLIC METHODS ************************************ //

    /**
     * Method that generates a matrix of LED positions to be turned on from the LED strip.
     *
     * @param time Time to be treated.
     * @return Array of integers with the LED strips position.
     */
    public int[] generate(String time) {
        Map<Integer, Character> charMap = getCharactersFromTime(time);
        List<Integer> ledPositions = new ArrayList<>();

        for (Map.Entry<Integer, Character> entry : charMap.entrySet()) {
            Integer[] positions = getLedPosition(entry.getKey(), entry.getValue());
            Collections.addAll(ledPositions, positions);
        }
        return ledPositions.stream().mapToInt(i -> i).toArray();
    }

    // ************************************ PRIVATE METHODS ************************************ //

    private Map<Integer, Character> getCharactersFromTime(String time) {
        Map<Integer, Character> charMap = new HashMap<>();

        String[] separated = time.split(":");
        String hour = separated[0];
        String minutes = separated[1];

        Log.d(mTag, "Hour: " + hour);
        Log.d(mTag, "Minutes: " + minutes);

        charMap.put(CHAR_0, hour.charAt(0));
        charMap.put(CHAR_1, hour.charAt(1));
        charMap.put(CHAR_2, minutes.charAt(0));
        charMap.put(CHAR_3, minutes.charAt(1));

        return charMap;
    }

    private Integer[] getLedPosition(Integer cellPosition, Character character) {
        // this offset is calculated in order to set the LED strip position given that each cell
        // has 7 LEDs.
        int offset = cellPosition * MAX_LEDS_PER_CELL;

        Integer[] rawPositions = new Integer[MAX_LED_POS];
        switch (character) {
            case CHAR_0:
                rawPositions = CHAR_0_VALUES;
                break;
            case CHAR_1:
                rawPositions = CHAR_1_VALUES;
                break;
            case CHAR_2:
                rawPositions = CHAR_2_VALUES;
                break;
            case CHAR_3:
                rawPositions = CHAR_3_VALUES;
                break;
            case CHAR_4:
                rawPositions = CHAR_4_VALUES;
                break;
            case CHAR_5:
                rawPositions = CHAR_5_VALUES;
                break;
            case CHAR_6:
                rawPositions = CHAR_6_VALUES;
                break;
            case CHAR_7:
                rawPositions = CHAR_7_VALUES;
                break;
            case CHAR_8:
                rawPositions = CHAR_8_VALUES;
                break;
            case CHAR_9:
                rawPositions = CHAR_9_VALUES;
                break;
            default:
                Log.w(mTag, "Unknown character: " + character);
                // do nothing
        }

        // add offset for each of the array elements according to the cell position
        Integer[] positions = new Integer[MAX_LED_POS];
        for (int i = 0; i < rawPositions.length; i++) {
            Log.d(mTag, "[RAW] Position value in (" + i + "): " + rawPositions[i]);
            if (cellPosition > 1) {
                // add another offset for the minutes due to 2 LEDs are for the `:` separator
                positions[i] = rawPositions[i] + offset + 2;
            } else {
                positions[i] = rawPositions[i] + offset;
            }
            Log.d(mTag, "Position value in (" + i + "): " + positions[i]);
        }
        return positions;
    }

}

