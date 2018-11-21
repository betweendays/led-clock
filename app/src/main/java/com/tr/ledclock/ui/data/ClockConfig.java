package com.tr.ledclock.ui.data;

import android.graphics.Color;

/**
 * This class represents the configuration for the clock.
 */
public class ClockConfig {

    public enum City {
        MADRID,
        CANBERRA,
        WASHINGTON
    }

    // *************************************** CONSTANTS *************************************** //

    // GMT for each city
    private static final String GMT_MADRID = "GMT+1:00";
    private static final String GMT_CANBERRA = "GMT+11:00";
    private static final String GMT_WASHINGTON = "GMT-7:00";

    // ****************************************** VARS ***************************************** //

    private String mCityGmt;
    private int mColor;

    // ************************************** CONSTRUCTORS ************************************* //

    public ClockConfig() {
        setCityGmt(City.MADRID);
    }

    // ************************************* PUBLIC METHODS ************************************ //

    public void setCityGmt(City city) {
        switch (city) {
            case CANBERRA:
                mCityGmt = GMT_CANBERRA;
                mColor = Color.RED;
                break;
            case WASHINGTON:
                mCityGmt = GMT_WASHINGTON;
                mColor = Color.YELLOW;
                break;
            case MADRID:
            default:
                // if unknown city value, set madrid
                mCityGmt = GMT_MADRID;
                mColor = Color.BLUE;
        }
    }

    public String getCityGmt() {
        return mCityGmt;
    }

    public int getColor() {
        return mColor;
    }
}
