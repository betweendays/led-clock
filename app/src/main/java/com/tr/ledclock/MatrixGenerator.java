package com.tr.ledclock;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class responsible for parsing time characters and generate a matrix to indicate which LEDs must
 * be turned on according to a given time.
 */
public class MatrixGenerator {

    // *************************************** CONSTANTS *************************************** //

    private static final int MAX_LED_POS = 30;

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
        Character[] characters = getCharactersFromTime(time);
        return getLedStripPositions(characters);
    }

    // ************************************ PRIVATE METHODS ************************************ //

    private Character[] getCharactersFromTime(String time) {
        String[] separated = time.split(":");
        String hour = separated[0];
        String minutes = separated[1];

        Log.d(mTag, "Hour: " + hour);
        Log.d(mTag, "Minutes: " + minutes);

        Character[] characters = new Character[4];
        characters[0] = hour.charAt(0);
        characters[1] = hour.charAt(1);
        characters[2] = minutes.charAt(0);
        characters[3] = minutes.charAt(1);

        return characters;
    }

    private int[] getLedStripPositions(Character[] timeSplit) {
        List<Integer> ledPositions = new ArrayList<>();
        for (Character character : timeSplit) {
            Log.d(mTag, "Char: " + character);
            Integer[] positions = getLedPosition(character);
            Collections.addAll(ledPositions, positions);
        }

        return ledPositions.stream().mapToInt(i -> i).toArray();
    }

    private Integer[] getLedPosition(Character character) {
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
        return array;
    }

}

