package org.remedy.db.contract;

import android.provider.BaseColumns;

public final class RemedySymptomContract {
    public RemedySymptomContract() {}

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "remedy_symptom";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_REMEDY_ID = "remedy_id";
        public static final String COLUMN_SYMPTOM_ID = "symptom_id";
    }
}