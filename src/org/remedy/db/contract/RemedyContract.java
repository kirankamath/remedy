package org.remedy.db.contract;

import android.provider.BaseColumns;

public final class RemedyContract {
    public RemedyContract() {}

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "remedy";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DETAILS = "details";
        public static final String COLUMN_DOSAGE = "dosage";
    }
}