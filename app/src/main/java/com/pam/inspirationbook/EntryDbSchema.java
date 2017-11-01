package com.pam.inspirationbook;

/**
 * Created by Pam on 10/31/2017.
 */

public class EntryDbSchema {
    public static final class EntryTable {
        public static final String DBNAME = "entries";

        public static final class Columns {
            public static final String UUID = "uuid";
            public static final String DATE = "date";
            public static final String TEXT = "text";
        }
    }
}
