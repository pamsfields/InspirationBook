package com.pam.inspirationbook;

import java.util.UUID;

/**
 * Created by Pam on 10/31/2017.
 */

public class NoteEntry extends Entry{
    private static final String TAG = "GERM.textentryclass";

    public NoteEntry(String text) {
        super();
        mText = text;
    }

    public NoteEntry(UUID id) {
        super(id);
    }
}
