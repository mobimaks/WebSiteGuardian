package com.example.WebSiteGuardian.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static String DB_UPDATE = "com.example.WebSiteGuardian.db.DB_UPDATE";

    //DB main
    public static final String DB_NAME = "history.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ResultsTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ResultsTable.onUpgrade(db, oldVersion, newVersion);
    }
}
