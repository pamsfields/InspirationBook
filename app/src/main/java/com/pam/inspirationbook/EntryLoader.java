package com.pam.inspirationbook;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Pam on 10/31/2017.
 */

public class EntryLoader {
    public static ArrayList<Entry> mEntries;
    public boolean isBusy;
    public int CurrentPageValue;
    public boolean CanLoadMoreItems;

    private static final String TAG = "GERM.entryloader";

    public EntryLoader()
    {
        mEntries = new ArrayList<Entry>();
    }

    public void loadMoreItems(int itemsPerPage) {

        isBusy = true;
        for (int i = CurrentPageValue; i < CurrentPageValue + itemsPerPage; i++) {

            if (i%2 == 0) {
                ImageEntry newImageEntry = new ImageEntry();
                mEntries.add(newImageEntry);
                Log.d(TAG, newImageEntry.toString());

            } else {
                NoteEntry newNoteEntry = new NoteEntry("’Please enter a phrase that inspires you:");
                EntryManager.sEntryManager.addEntry(newNoteEntry);
                mEntries.add(newNoteEntry);
                Log.d(TAG, newNoteEntry.toString());

            }
        }

        CanLoadMoreItems = true;
        CurrentPageValue = 1;
        isBusy = false;
    }
}
