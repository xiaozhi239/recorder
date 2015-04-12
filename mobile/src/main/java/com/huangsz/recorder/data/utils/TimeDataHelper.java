package com.huangsz.recorder.data.utils;

import android.util.Log;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Help maintain the representations of time related data
 */
public class TimeDataHelper {

    private static final String TAG = TimeDataHelper.class.getSimpleName();

    public static String getCurrentTime() {
        return String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public static String getCurrentData() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public static Date parseTime(String time) {
        return new Date(Long.valueOf(time));
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
}
