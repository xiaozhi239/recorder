package com.huangsz.recorder.model;

import android.provider.BaseColumns;
import android.util.Log;
import android.util.Pair;

import com.google.common.collect.Collections2;
import com.huangsz.recorder.data.utils.TimeDataHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Record {

    private static final String TAG = Record.class.getSimpleName();

    private long id;

    private String name;

    private String desc;

    /**
     * the format of the data is {"date": "time"} or other json,
     * but note that the key (such as the 'date' should always be
     * the horizontal ordinate. Thus the data is a json, but not a
     * json array.
     * like text
     */
    private JSONObject data;

    private String createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getData() {
        return data.toString();
    }

    public void setData(String data) {
        try {
            this.data = new JSONObject(data == null ? "{}" : data);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * if the key already exists, then the value will be updated to the new one
     * @param key
     * @param value
     */
    public void putData(String key, String value) {
        try {
            this.data.put(key, value);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    public List<Pair<String, String>> getDataInList() {
        List<Pair<String, String>> entries = new ArrayList<>();
        Iterator<String> iterator = data.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Pair<String, String> entry = new Pair<>(key, data.optString(key));
            entries.add(entry);
        }
        Collections.sort(entries, new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> lhs, Pair<String, String> rhs) {
                return lhs.first.compareTo(rhs.second);
            }
        });
        return entries;
    }

    public Date getCreateTime() {
        try {
            return TimeDataHelper.parseTime(createTime);
        } catch (Exception e) {
            return null;
        }
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static class Entry implements BaseColumns {

        public static final String TABLE = "Record";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_DESC = "desc";

        public static final String COLUMN_DATA = "data";

        public static final String COLUMN_CREATE_TIME = "createTime";

        public static String[] projection() {
            return new String[] {_ID, COLUMN_NAME, COLUMN_DESC, COLUMN_DATA,
                    COLUMN_CREATE_TIME};
        }
    }
}
