package com.pam.inspirationbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Pam on 10/31/2017.
 */

public class EntryDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "entryDatabase.db";

    private static final String TAG = "GERM.entryDbHelper";

    public EntryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        Log.d(TAG, "constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EntryDbSchema.EntryTable.DBNAME + "(" +
                " _id integer primary key autoincrement, " +
                EntryDbSchema.EntryTable.Columns.UUID + ", " +
                EntryDbSchema.EntryTable.Columns.DATE + ", " +
                EntryDbSchema.EntryTable.Columns.TEXT + ")"
        );
        Log.d(TAG, "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
