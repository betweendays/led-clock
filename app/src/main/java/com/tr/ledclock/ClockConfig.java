package com.tr.ledclock;

import android.graphics.Color;
import android.util.Log;

import java.util.Map;

/**
 * Class responsible for handling clock configuration.
 */
public class ClockConfig {

    // ***************************************** ENUMS ***************************************** //

    public enum City {
        MADRID, // BLUE
        CANBERRA, // RED
        WASHINGTON // YELLOW
    }

    // ****************************************** VARS ***************************************** //

    // log tag
    private final String mTag;

    private City mCity;
    private int mColor;
    private TimeTuple mTime;

    // ************************************** CONSTRUCTORS ************************************* //

    public ClockConfig(String tag) {
        mTag = tag;
        setCity(City.MADRID);
        mTime = new TimeTuple(tag);
    }

    // ************************************* PUBLIC METHODS ************************************ //

    /**
     * Method that sets the clock city and depending on this, its colour.
     *
     * @param city {@link City} value to be set.
     */
    public void setCity(City city) {
        mCity = city;

        switch (city) {
            case MADRID:
                mColor = Color.BLUE;
                break;
            case CANBERRA:
                mColor = Color.RED;
                break;
            case WASHINGTON:
                mColor = Color.YELLOW;
                break;
            default:
                mColor = Color.BLACK;
        }

        Log.d(mTag, "City and colour defined.");
    }

    public City getCity() {
        return mCity;
    }

    public int getColor() {
        return mColor;
    }

    public int getDefaultColor() {
        return Color.BLACK;
    }

    /**
     * Method that calculates current time according to the previously configured city.
     *
     * @return Current time on the configured city. Madrid by default.
     */
    public Map<Integer, Character> getTimeCharMap() {
        return mTime.getTimeCharMap(mCity);
    }
}
