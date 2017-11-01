package com.pam.inspirationbook;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Pam on 10/31/2017.
 */

public class EntryCursorWrapper extends CursorWrapper {

    public EntryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public NoteEntry getEntry(){
        String uuidString = getString(getColumnIndex(EntryDbSchema.EntryTable.Columns.UUID));
        long date = getLong(getColumnIndex(EntryDbSchema.EntryTable.Columns.DATE));
        String text = getString(getColumnIndex(EntryDbSchema.EntryTable.Columns.TEXT));

        NoteEntry newNoteEntry = new NoteEntry(UUID.fromString(uuidString));
        newNoteEntry.setDate(new Date(date));
        newNoteEntry.setText(text);

        return newNoteEntry;
    }
}

