package com.pam.inspirationbook;

import com.pam.inspirationbook.EntryDbSchema.EntryTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Pam on 10/31/2017.
 */

public class EntryManager {
    protected static EntryManager sEntryManager;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static final String TAG = "InspirationBoard.entrymanager";

    public static EntryManager get(Context context) {
        Log.d(TAG, "get method");
        if (sEntryManager == null) {
            sEntryManager = new EntryManager(context);
        }

        return sEntryManager;
    }

    private EntryManager(Context context) {
        mContext = context.getApplicationContext();
        Log.d(TAG, "constructor");
        mDatabase = new EntryDatabaseHelper(mContext).getWritableDatabase();
    }

    public void addEntry(Entry e) {
        Log.d(TAG, "Adding entry to database");
        ContentValues values = getContentValues(e);
        mDatabase.insert(EntryDbSchema.EntryTable.DBNAME, null, values);
    }

    public ArrayList<Entry> getEntries() {
        ArrayList<Entry> entries = new ArrayList<>();
        EntryCursorWrapper cursor = queryEntries(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                entries.add(cursor.getEntry());
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }

        return entries;
    }

    public Entry getEntry(UUID id) {
        EntryCursorWrapper cursor = queryEntries(
                EntryDbSchema.EntryTable.Columns.UUID + " = ?", new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getEntry();

        } finally {
            cursor.close();
        }
    }

    public void updateEntry(Entry entry) {
        String uuidString = entry.getId().toString();
        ContentValues values = getContentValues(entry);

        mDatabase.update(EntryTable.DBNAME, values, EntryTable.Columns.UUID + " = ?", new String[] { uuidString});
    }

    private static ContentValues getContentValues(Entry entry) {
        ContentValues values = new ContentValues();
        values.put(EntryTable.Columns.UUID, entry.getId().toString());
        values.put(EntryTable.Columns.DATE, entry.getDate().getTime());
        values.put(EntryTable.Columns.TEXT, entry.getText());

        return values;
    }

    private EntryCursorWrapper queryEntries(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                EntryTable.DBNAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new EntryCursorWrapper(cursor);
    }
}

