package com.tr.ledclock;

import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Class that encapsulates all logic related to current time; hours and minutes.
 */
class TimeTuple {

    // *************************************** CONSTANTS *************************************** //

    private static final int CHAR_0 = 0;
    private static final int CHAR_1 = 1;
    private static final int CHAR_2 = 2;
    private static final int CHAR_3 = 3;


    // ****************************************** VARS ***************************************** //

    private final String mTag;

    private String mHour;
    private String mMinutes;

    // ************************************** CONSTRUCTORS ************************************* //

    public TimeTuple(String tag) {
        mTag = tag;
    }


    // ************************************* PUBLIC METHODS ************************************ //

    public String getHour() {
        return mHour;
    }

    public String getMinutes() {
        return mMinutes;
    }

    public void setHour(String hour) {
        mHour = hour;
    }

    public void setMinutes(String minutes) {
        mMinutes = minutes;
    }

    public Map<Integer, Character> getTimeCharMap(ClockConfig.City mCity) {
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

        // save hour and minutes
        mHour = String.format(Locale.getDefault(), "%02d", c.get(Calendar.HOUR_OF_DAY));
        mMinutes = String.format(Locale.getDefault(), "%02d", c.get(Calendar.MINUTE));

        Log.d(mTag, "Current time: " + mHour + ":" + mMinutes);

        Map<Integer, Character> charMap = new HashMap<>();

        charMap.put(CHAR_0, mHour.charAt(0));
        charMap.put(CHAR_1, mHour.charAt(1));
        charMap.put(CHAR_2, mMinutes.charAt(0));
        charMap.put(CHAR_3, mMinutes.charAt(1));

        return charMap;
    }
}
