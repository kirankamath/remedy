package org.remedy.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.remedy.Remedy;
import org.remedy.db.contract.CategoryContract;
import org.remedy.db.contract.RemedyContract;
import org.remedy.db.contract.RemedySymptomContract;
import org.remedy.db.contract.SymptomListContract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RemedyDAO {

    private final SQLiteDatabase dbInstance;

    public RemedyDAO(Context context) {
        RemedyDatabase database = new RemedyDatabase(context);
        dbInstance = database.getReadableDatabase();
    }

    public void close() {
        dbInstance.close();
    }

    public static Remedy getRemedyDetails(Context context, String remedyName) {
        RemedyDatabase database = new RemedyDatabase(context);
        SQLiteDatabase db = database.getReadableDatabase();
        String[] selectionArgs = new String[1];
        selectionArgs[0] = remedyName;

        String details = "";
        String dosage = "";
        {
            Cursor cursor = db.rawQuery("select " + RemedyContract.Entry.COLUMN_DETAILS +
                    " , " + RemedyContract.Entry.COLUMN_DOSAGE +
                    " from " + RemedyContract.Entry.TABLE_NAME + " where " +
                    RemedyContract.Entry.COLUMN_NAME + " = ? ",
                    selectionArgs);

            boolean notEmpty = cursor.moveToFirst();
            if (notEmpty) {
                details = cursor.getString(0);
                dosage = cursor.getString(1);
            }
            cursor.close();
        }

        Remedy remedy = new Remedy(remedyName);
        remedy.setDetails(details);
        remedy.setDosage(dosage);

        // Get the list of symptoms for this remedy.
        {
            Cursor cursor = db.rawQuery(" select c.category, s.symptom " +
                    " from " + RemedyContract.Entry.TABLE_NAME + " r, " +
                    CategoryContract.Entry.TABLE_NAME + " c, " +
                    SymptomListContract.Entry.TABLE_NAME + " s, " +
                    RemedySymptomContract.Entry.TABLE_NAME + " rs " +
                    " where r.name = ? and " +
                    " r.rowid = rs.remedy_id and " + // Join from Remedy to RemedySymptom.
                    " rs.symptom_id = s.rowid and " + // Join from RemedySymptom to Symptom.
                    " s.cat_id = c.rowid", // Join from Symptom to Category.
                    selectionArgs);
            boolean notEmpty = cursor.moveToFirst();
            do {
                String category = cursor.getString(0);
                String symptom = cursor.getString(1);
                remedy.addSymptom(category, symptom);
                notEmpty = cursor.moveToNext();
            } while (notEmpty);
            cursor.close();
        }

        db.close();
        return remedy;
    }

    public static List<String> getRemedyNames(Context context) {
        List<String> remedyNames = new ArrayList<String>();
        RemedyDatabase database = new RemedyDatabase(context);
        SQLiteDatabase db = database.getReadableDatabase();
        {
            Cursor cursor = db.rawQuery(" select name from remedy", null);
            boolean notEmpty = cursor.moveToFirst();
            do {
                String name = cursor.getString(0);
                remedyNames.add(name);
                notEmpty = cursor.moveToNext();
            } while (notEmpty);
            cursor.close();
        }
        db.close();
        return remedyNames;
    }

    private static boolean isPresent(SQLiteDatabase db, String remedyName) {
        boolean found = false;
        {
            String sql = " select rowid from remedy where name = ?";
            String[] selectionArgs = new String[1];
            selectionArgs[0] = remedyName;

            Cursor cursor = db.rawQuery(sql, selectionArgs);
            found = cursor.moveToFirst();
            cursor.close();
        }
        return found;
    }

    public static void saveRemedy(Context context, Remedy remedy) {
        RemedyDatabase database = new RemedyDatabase(context);
        SQLiteDatabase db = database.getReadableDatabase();
        if (isPresent(db, remedy.getName())) {
            return;
        }

        long remedyId;
        {
            ContentValues values = new ContentValues(3);
            values.put("name", remedy.getName());
            values.put("details", remedy.getDetails());
            values.put("dosage", remedy.getDosage());
            remedyId = db.insert("remedy", null, values);
        }

        Map<String, Set<String>> symptomsWithCategory = remedy.getSymptoms();
        for (String category: symptomsWithCategory.keySet()) {

            // See if the category already exists. If so, pick up its id.
            long categoryId;
            {
                String sql = "select rowid from category where category = ?";
                Cursor cursor = db.rawQuery(sql, new String[] {category});

                boolean found = cursor.moveToFirst();
                if (found) {
                    categoryId = cursor.getLong(0);
                } else {
                    ContentValues values = new ContentValues(1);
                    values.put("category", category);
                    categoryId = db.insert("category", null, values);
                }
                cursor.close();
            }

            Set<String> symptoms = symptomsWithCategory.get(category);
            for (String symptom: symptoms) {
                long symptomId;
                {
                    String sql = "select rowid from symptom_list where symptom = ? and cat_id = " + categoryId;
                    Cursor cursor = db.rawQuery(sql, new String[] {symptom});
                    boolean found = cursor.moveToFirst();
                    if (found) {
                        symptomId = cursor.getLong(0);
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("symptom", symptom);
                        values.put("cat_id", categoryId);
                        symptomId = db.insert("symptom_list", null, values);
                    }
                    cursor.close();
                }
                {
                    String sql = "select rowid from remedy_symptom where remedy_id = " + remedyId + " and symptom_id = " + symptomId;
                    Cursor cursor = db.rawQuery(sql, null);
                    boolean found = cursor.moveToFirst();
                    cursor.close();
                    if (found) {
                        continue;
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("remedy_id", remedyId);
                        values.put("symptom_id", symptomId);
                        db.insert("remedy_symptom", null, values);
                    }
                }
            }
        }

        db.close();
    }

    public static List<String> getAllCategories(Context context) {
        RemedyDatabase database = new RemedyDatabase(context);
        SQLiteDatabase db = database.getReadableDatabase();

        List<String> categoryNames = new ArrayList<String>();
        {
            Cursor cursor = db.rawQuery(" select category from category ", null);
            boolean notEmpty = cursor.moveToFirst();
            do {
                String name = cursor.getString(0);
                categoryNames.add(name);
                notEmpty = cursor.moveToNext();
            } while (notEmpty);
            cursor.close();
        }
        db.close();
        return categoryNames;
    }

    public static List<String> getAllSymptomsForCategory(Context context, String category) {
        List<String> symptoms = new ArrayList<String>();
        RemedyDatabase database = new RemedyDatabase(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String[] selectionArgs = new String[1];
        selectionArgs[0] = category;
        {
            String sql = " select symptom from " + SymptomListContract.Entry.TABLE_NAME + " s, " +
                    CategoryContract.Entry.TABLE_NAME + " c " +
                    " where s.cat_id = c.rowid and c.category = ?";
            Cursor cursor = db.rawQuery(sql, selectionArgs);
            boolean notEmpty = cursor.moveToFirst();
            do {
                String name = cursor.getString(0);
                symptoms.add(name);
                notEmpty = cursor.moveToNext();
            } while (notEmpty);
            cursor.close();
        }
        db.close();
        return symptoms;
    }

    public List<String> getRemediesForSymptom(String inCategory, String inSymptom) {
        List<String> result = new ArrayList<String>();

        String[] selectionArgs = new String[2];
        selectionArgs[0] = inCategory;
        selectionArgs[1] = inSymptom;
        {
            Cursor cursor = dbInstance.rawQuery(" select r.name " +
                    " from " + RemedyContract.Entry.TABLE_NAME + " r, " +
                    CategoryContract.Entry.TABLE_NAME + " c, " +
                    SymptomListContract.Entry.TABLE_NAME + " s, " +
                    RemedySymptomContract.Entry.TABLE_NAME + " rs " +
                    " where c.category = ? and s.symptom = ? and " +
                    " r.rowid = rs.remedy_id and " + // Join from Remedy to RemedySymptom.
                    " rs.symptom_id = s.rowid and " + // Join from RemedySymptom to Symptom.
                    " s.cat_id = c.rowid", // Join from Symptom to Category.
                    selectionArgs);
            boolean notEmpty = cursor.moveToFirst();
            do {
                String remedyName = cursor.getString(0);
                result.add(remedyName);
                notEmpty = cursor.moveToNext();
            } while (notEmpty);
            cursor.close();
        }
        return result;
    }
}
