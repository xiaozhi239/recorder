package com.huangsz.recorder.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.huangsz.recorder.model.Record;

/**
 * Store or provide data
 */
public class RecordProvider extends ContentProvider {

    private static final String TAG = RecordProvider.class.getSimpleName();

    /**
     * URI ID for route: /records
     */
    public static final int ROUTE_RECORDS = 1;

    /**
     * URI ID for route: /records/{ID}
     */
    public static final int ROUTE_RECORDS_ID = 2;

    /**
     * Path component for "record"-type resources..
     */
    private static final String PATH_RECORDS = "records";

    /**
     * Fully qualified URI for "record" resources.
     */
    public static final Uri CONTENT_URI =
            ProviderConstants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECORDS).build();

    /**
     * UriMatcher, used to decode incoming URIs.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(ProviderConstants.CONTENT_AUTHORITY, RecordProvider.PATH_RECORDS, ROUTE_RECORDS);
        sUriMatcher.addURI(ProviderConstants.CONTENT_AUTHORITY, RecordProvider.PATH_RECORDS + "/*", ROUTE_RECORDS_ID);
    }

    private SQLiteHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new SQLiteHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder().table(Record.Entry.TABLE);
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_RECORDS_ID:
                // Return a single entry, by ID.
                String id = uri.getLastPathSegment();
                builder.where(Record.Entry._ID + "=?", id);
                break;
            case ROUTE_RECORDS:
                // Return all known entries.
                builder.where(selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Cursor cursor = builder.query(db, projection, sortOrder);
        // Note: Notification URI must be manually set here for loaders to correctly
        // register ContentObservers.
        Context ctx = getContext();
        assert ctx != null;
        cursor.setNotificationUri(ctx.getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri result;
        switch (match) {
            case ROUTE_RECORDS:
                long id = db.insertOrThrow(Record.Entry.TABLE, null, values);
                result = Uri.parse(CONTENT_URI + "/" + id);
                Log.i(TAG, String.format("Insert record and returned id: %d", id));
                break;
            case ROUTE_RECORDS_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder().table(Record.Entry.TABLE);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_RECORDS:
                count = builder.where(selection, selectionArgs).delete(db);
                break;
            case ROUTE_RECORDS_ID:
                String id = uri.getLastPathSegment();
                count = builder.where(Record.Entry._ID + "=?", id)
                        .where(selection, selectionArgs)  // just in case user type wrong
                        .delete(db);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder().table(Record.Entry.TABLE);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_RECORDS:
                count = builder.where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_RECORDS_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(Record.Entry.TABLE)
                        .where(Record.Entry._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }
}
