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

    private static final int MAX_POSITIONS_PER_CELL = 7;

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
        Map<Integer, Character> characters = getCharactersFromTime(time);
        return getLedStripPositions(characters);
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

    private int[] getLedStripPositions(Map<Integer, Character> charMap) {
        List<Integer> ledPositions = new ArrayList<>();

        for (Map.Entry<Integer, Character> entry : charMap.entrySet()) {
            Integer[] positions = getLedPosition(entry.getKey(), entry.getValue());

            Collections.addAll(ledPositions, positions);
        }

        return ledPositions.stream().mapToInt(i -> i).toArray();
    }

    private Integer[] getLedPosition(Integer cellPosition, Character character) {
        int offset = cellPosition * MAX_POSITIONS_PER_CELL;

        Integer[] array = new Integer[MAX_LED_POS];
        switch (character) {
            case CHAR_0:
                array = CHAR_0_VALUES;
                break;
            case CHAR_1:
                array = CHAR_1_VALUES;
                break;
            case CHAR_2:
                array = CHAR_2_VALUES;
                break;
            case CHAR_3:
                array = CHAR_3_VALUES;
                break;
            case CHAR_4:
                array = CHAR_4_VALUES;
                break;
            case CHAR_5:
                array = CHAR_5_VALUES;
                break;
            case CHAR_6:
                array = CHAR_6_VALUES;
                break;
            case CHAR_7:
                array = CHAR_7_VALUES;
                break;
            case CHAR_8:
                array = CHAR_8_VALUES;
                break;
            case CHAR_9:
                array = CHAR_9_VALUES;
                break;
            default:
                Log.w(mTag, "Unknown character: " + character);
                // do nothing
        }

        // TODO: offset + matrix
        return array;
    }

}

