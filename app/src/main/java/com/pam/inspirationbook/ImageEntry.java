package com.pam.inspirationbook;

import android.graphics.Bitmap;

import java.util.UUID;

/**
 * Created by Pam on 10/31/2017.
 */

public class ImageEntry extends Entry{
    protected Bitmap mImage;
    public int mResourceId;
    private static int COUNTER = 0;

    private static final String TAG = "InspirationBoard.imageentry";

    public ImageEntry () {
        super();

        Integer[] thumbIds = {};
        mResourceId = thumbIds[COUNTER];
        COUNTER++;

        EntryManager.sEntryManager.addEntry(this);
    }

    public ImageEntry (UUID id) {
        super(id);
    }
}
