package com.example.WebSit_eGuardian.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.WebSit_eGuardian.service.SiteCheckService.SiteCheckResult;

public class ResultsDataSource {

    public void addResult(SiteCheckResult siteCheckResult, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(ResultsTable.SITE_URL, siteCheckResult.getSiteUrl());
        cv.put(ResultsTable.CHECK_RESULT, siteCheckResult.getResult());
        cv.put(ResultsTable.CHECK_TIME, siteCheckResult.getCheckedTime());
        db.insert(ResultsTable.RESULTS_TABLE, null, cv);
    }

    public Cursor getLastResults(Integer number, SQLiteDatabase db){
        Cursor c;
        if (number == -1){
            c = db.query(ResultsTable.RESULTS_TABLE, ResultsTable.COLUMNS, null, null, null, null,
                    ResultsTable.CHECK_TIME.concat(" DESC"));
        } else {
            c = db.query(ResultsTable.RESULTS_TABLE, ResultsTable.COLUMNS, null, null, null, null,
                    ResultsTable.CHECK_TIME.concat(" DESC"), String.valueOf(number));
        }
        Log.d("mytime", "Number: ".concat(number.toString()).concat(". Size:").concat("" + c.getCount()));
        return c;
    }
}
