package org.remedy.db;

import org.remedy.Remedy;
import org.remedy.Symptom;
import org.remedy.db.contract.CategoryContract;
import org.remedy.db.contract.RemedyContract;
import org.remedy.db.contract.RemedySymptomContract;
import org.remedy.db.contract.SymptomListContract;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RemedyDAO {

    public Remedy getRemedyDetails(Context context, String remedyName) {
        MyDatabase database = new MyDatabase(context);
        SQLiteDatabase db = database.getReadableDatabase();
        String[] selectionArgs = new String[1];
        selectionArgs[0] = remedyName;

        String details = "";
        String dosage = "";
        {
            Cursor cursor = db.rawQuery("select details, dosage from remedy where name = ? ",
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
                    " r.id = rs.remedyid and " + // Join from Remedy to RemedySymptom.
                    " rs.symptomid = s.id and " + // Join from RemedySymptom to Symptom.
                    " s.catid = c.id", // Join from Symptom to Category.
                    selectionArgs);
            boolean notEmpty = cursor.moveToFirst();
            do {
                String category = cursor.getString(0);
                String symptom = cursor.getString(1);
                remedy.addSymptom(new Symptom(category, symptom));
                notEmpty = cursor.moveToNext();
            } while (notEmpty);
            cursor.close();
        }

        db.close();
        return remedy;
    }
}
