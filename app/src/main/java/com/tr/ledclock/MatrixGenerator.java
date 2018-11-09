package com.tr.ledclock;

import java.util.ArrayList;
import java.util.Arrays;
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

    // ************************************* PUBLIC METHODS ************************************ //

    /**
     * Method that generates a matrix of LED positions to be turned on from the LED strip.
     *
     * @param charMap Map containing the current time split in 4 characters.
     * @return Array of integers with the LED strips position.
     */
    public boolean[] generate(Map<Integer, Character> charMap) {
        boolean[] allPositions = new boolean[MAX_LED_POS];
        List<Integer> concatenatedPos = new ArrayList<>();

        for (Map.Entry<Integer, Character> entry : charMap.entrySet()) {
            List<Integer> rawPositions = getArrayLedsPerCharacter(entry.getValue());
            rawPositions = addOffset(entry.getKey(), rawPositions);
            concatenatedPos.addAll(rawPositions);
        }

        for (int i = 0; i < allPositions.length; i++) {
            if (concatenatedPos.contains(i)) {
                allPositions[i] = true;
            }
        }

        return allPositions;
    }

    private List<Integer> addOffset(Integer cellPosition, List<Integer> rawPositions) {
        // this offset is calculated in order to set the LED strip position given that each cell
        // has 7 LEDs.
        int offset = cellPosition * MAX_LEDS_PER_CELL;

        // add offset for each of the array elements according to the cell position
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < rawPositions.size(); i++) {
            if (cellPosition > 1) {
                // add another offset for the minutes due to 2 LEDs are for the `:` separator
                positions.add(rawPositions.get(i) + offset + 2);
            } else {
                positions.add(rawPositions.get(i) + offset);
            }
        }
        return positions;
    }

    // ************************************ PRIVATE METHODS ************************************ //

    private List<Integer> getArrayLedsPerCharacter(Character character) {
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
}

