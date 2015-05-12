package com.huangsz.recorder.data.utils;

import android.util.Log;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public static int getHourOfDay(Date time) {
        return getTimeField(time, Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date time) {
        return getTimeField(time, Calendar.MINUTE);
    }

    private static int getTimeField(Date time, int field) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(time);
        return calendar.get(field);
    }
}
