package org.remedy.db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class RemedyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "remedy";
    private static final int DATABASE_VERSION = 1;

    public RemedyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgradeVersion(DATABASE_VERSION);
    }
}