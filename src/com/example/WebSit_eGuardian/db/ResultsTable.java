package com.example.WebSit_eGuardian.db;

import android.database.sqlite.SQLiteDatabase;

public class ResultsTable {

    public static final String RESULTS_TABLE = "results";
    public static final String ID = "_id";
    public static final String SITE_URL = "url";
    public static final String CHECK_TIME = "time";
    public static final String CHECK_RESULT = "result";

    public static final String[] COLUMNS = new String[]{ID, SITE_URL, CHECK_TIME, CHECK_RESULT};

    public static void onCreate(SQLiteDatabase db){
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("create table ").append(RESULTS_TABLE).append(" (").append(ID).append(" integer primary key autoincrement, ")
                .append(SITE_URL).append(" text not null, ").append(CHECK_TIME).append(" integer not null, ").append(CHECK_RESULT)
                .append(" integer not null);");
        db.execSQL(queryBuilder.toString());
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
