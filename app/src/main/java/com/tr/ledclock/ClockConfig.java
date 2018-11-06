package com.tr.ledclock;

import android.graphics.Color;
import android.util.Log;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * TODO: Please describe the objectives and purposes of this class.
 */
public class ClockConfig {

    // ***************************************** ENUMS ***************************************** //

    enum City {
        MADRID, // BLUE
        CANBERRA, // RED
        WASHINGTON // YELLOW
    }

    // ****************************************** VARS ***************************************** //

    // log tag
    private final String mTag;

    private City mCity = City.MADRID;
    private int mColor = Color.BLACK;

    // ************************************** CONSTRUCTORS ************************************* //

    public ClockConfig(String tag) {
        mTag = tag;
        setCity(City.MADRID);
    }

    // ************************************* PUBLIC METHODS ************************************ //

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

    public String getTime() {
        TimeZone tz;
        Calendar c = null;
        switch (mCity) {
            case MADRID:
                tz = TimeZone.getTimeZone("GMT+1:00");
                c = Calendar.getInstance(tz);
                break;
            case CANBERRA:
                tz = TimeZone.getTimeZone("GMT+11:00");
                c = Calendar.getInstance(tz);
                break;
            case WASHINGTON:
                tz = TimeZone.getTimeZone("GMT-7:00");
                c = Calendar.getInstance(tz);
                break;
            default:
                // Unknown city, do nothing
        }

        if (c == null) {
            throw new IllegalStateException("Couldn't get time.");
        }

        // només agafa l'hora i els minuts..
        String time = String.format(Locale.getDefault(), "%02d", c.get(Calendar.HOUR_OF_DAY)) +
                String.format(Locale.getDefault(), "%02d", c.get(Calendar.MINUTE));

        Log.d(mTag, "Current time: " + time);
        return time;
    }
}
