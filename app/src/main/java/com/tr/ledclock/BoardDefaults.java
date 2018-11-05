package com.tr.ledclock;

import android.os.Build;

public class BoardDefaults {

    private BoardDefaults() {
        // hide
    }

    private static final String DEVICE_EDISON = "edison";
    private static final String DEVICE_RPI3 = "rpi3";
    private static final String DEVICE_NXP = "imx6ul";
    /**
     * Return the preferred I2C port for each board.
     */
    public static String getSPIPort() {
        switch (Build.DEVICE) {
            // same for Edison Arduino breakout and Edison SOM
            case DEVICE_EDISON:
                return "SPI2";
            case DEVICE_RPI3:
                return "SPI0.0";
            case DEVICE_NXP:
                return "SPI3_0";
            default:
                throw new IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE);
        }
    }
}

