package com.huangsz.recorder.data.utils;

import android.database.Cursor;

import com.huangsz.recorder.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Database related operation utils for Record.
 */
public class RecordDataHelper {

    /**
     * Note that cursor should use the projection of
     * {@link com.huangsz.recorder.model.Record.Entry#projection()}
     * @param cursor
     * @return
     */
    public static List<Record> getAllRecords(Cursor cursor) {
        List<Record> records = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            records.add(cursor2Record(cursor));
            cursor.moveToNext();
        }
        return records;
    }

    private static Record cursor2Record(Cursor cursor) {
        Record record = new Record();
        record.setId(cursor.getLong(0));
        record.setName(cursor.getString(1));
        record.setDesc(cursor.getString(2));
        record.setData(cursor.getString(3));
        record.setCreateTime(cursor.getString(4));
        return record;
    }
}
