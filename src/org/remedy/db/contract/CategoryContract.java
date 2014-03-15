package org.remedy.db.contract;

import android.provider.BaseColumns;

public final class CategoryContract {
    public CategoryContract() {}

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CATEGORY = "category";
    }
}