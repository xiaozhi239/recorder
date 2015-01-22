package com.huangsz.recorder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.huangsz.recorder.model.Record;

import java.util.HashSet;
import java.util.Set;

/**
 * database helper
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "tracker.db";

    private static final int DATABASE_VERSION = 1;

    private static final String TYPE_TEXT = " TEXT";

    private static final String COMMA_SEP = ",";

    private Set<String> tables;

    private static final String SQL_CREATE_RECORD =
            "CREATE TABLE " + Record.Entry.TABLE + " (" +
                    Record.Entry._ID + " INTEGER PRIMARY KEY," +
                    Record.Entry.COLUMN_NAME + TYPE_TEXT + COMMA_SEP +
                    Record.Entry.COLUMN_DESC + TYPE_TEXT + COMMA_SEP +
                    Record.Entry.COLUMN_DATA + TYPE_TEXT + COMMA_SEP +
                    Record.Entry.COLUMN_CREATE_TIME + TYPE_TEXT + ")";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        tables = new HashSet<>();
        tables.add(Record.Entry.TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        for (String table : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        onCreate(db);
    }

}
