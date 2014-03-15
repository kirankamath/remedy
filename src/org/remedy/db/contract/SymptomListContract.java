package org.remedy.db.contract;

import android.provider.BaseColumns;

public final class SymptomListContract {
    public SymptomListContract() {}

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "symptom_list";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CAT_ID = "cat_id";
        public static final String COLUMN_SYMPTOM = "symptom";
    }
}