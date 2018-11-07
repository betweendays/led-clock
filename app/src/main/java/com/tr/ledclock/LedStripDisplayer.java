package com.tr.ledclock;

import android.util.Log;

import com.tr.ledclock.utils.BoardDefaults;
import com.xrigau.driver.ws2801.Ws2801;

import java.io.IOException;

/**
 * Class responsible for displaying LEDs from the WS2801 strip.
 */
public class LedStripDisplayer {

    // ****************************************** VARS ***************************************** //

    private final String mTag;
    private Ws2801 mLedStrip;

    // ************************************** CONSTRUCTORS ************************************* //

    public LedStripDisplayer(String logTag) throws IOException {
        mTag = logTag;
        mLedStrip = Ws2801.create(BoardDefaults.getSPIPort(), Ws2801.Mode.RBG);
    }

    // ************************************* PUBLIC METHODS ************************************ //

    /**
     * Method that writes the position(s) of the LED strip according to a given matrix and
     * configuration.
     *
     * @param matrix Position(s) of the LED strip to be written.
     * @param config {@link ClockConfig} instance with the required colour.
     * @throws IOException Thrown if an error occurs while writing on LED strip.
     */
    public void display(int[] matrix, ClockConfig config) throws IOException {
        Log.d(mTag, "Display leds with colour: " + config.getColor());

        int[] leds = new int[matrix.length];
        for (int value : matrix) {
            leds[value] = config.getColor();

            Log.d(mTag, "LEDs position: " + value);
        }

        mLedStrip.write(leds);
        Log.d(mTag, "Done.");
    }

    /**
     * Method that closes the stream against LED strip.
     *
     * @throws IOException Thrown if an error occurs while closing the LED strip.
     */
    public void stop() throws IOException {
        Log.d(mTag, "Close LED strip.");
        mLedStrip.close();
    }
}
