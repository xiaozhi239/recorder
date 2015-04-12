package com.huangsz.recorder.data.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.huangsz.recorder.data.RecordProvider;
import com.huangsz.recorder.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Database related operation utils for Record.
 */
public class RecordDataHelper {

    public static Record getRecord(Context context, long recordId) {
        Uri uri = getRecordUri(recordId);
        Cursor cursor = context.getContentResolver().query(uri,
                Record.Entry.projection(), null, null, null);
        cursor.moveToFirst();
        return cursor2Record(cursor);
    }

    public static void updateRecord(Context context, ContentValues newData, long recordId) {
        context.getContentResolver().update(getRecordUri(recordId), newData, null, null);
    }

    public static void deleteRecord(Context context, long recordId) {
        context.getContentResolver().delete(getRecordUri(recordId), null, null);
    }

    private static Uri getRecordUri(long recordId) {
        return RecordProvider.CONTENT_URI.buildUpon()
                .appendPath(String.format("%d", recordId)).build();
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
