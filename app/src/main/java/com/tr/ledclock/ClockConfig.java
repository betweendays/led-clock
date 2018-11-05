package com.tr.ledclock;

import android.graphics.Color;
import android.util.Log;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static com.tr.ledclock.ClockConfig.City.Washington;

/**
 * TODO: Please describe the objectives and purposes of this class.
 */
public class ClockConfig {

    // ***************************************** ENUMS ***************************************** //

    enum City {
        Madrid, // BLUE
        Canberra, // RED
        Washington // YELLOW
    }

    // ****************************************** VARS ***************************************** //

    // log tag
    private final String mTag;

    private City mCity = City.Madrid;
    private int mColor = Color.BLACK;

    // ************************************** CONSTRUCTORS ************************************* //

    public ClockConfig(String tag) {
        mTag = tag;
        setCity(City.Madrid);
    }

    // ************************************* PUBLIC METHODS ************************************ //

    public void setCity(City city) {
        mCity = city;

        switch (city) {
            case Madrid:
                mColor = Color.BLUE;
                break;
            case Canberra:
                mColor = Color.RED;
                break;
            case Washington:
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
            case Madrid:
                tz = TimeZone.getTimeZone("GMT+1:00");
                c = Calendar.getInstance(tz);
                break;
            case Canberra:
                tz = TimeZone.getTimeZone("GMT+11:00");
                c = Calendar.getInstance(tz);
                break;
            case Washington:
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
