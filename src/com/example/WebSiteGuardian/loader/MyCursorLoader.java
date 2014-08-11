package com.example.WebSiteGuardian.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.WebSiteGuardian.db.DBHelper;
import com.example.WebSiteGuardian.db.ResultsDataSource;

public class MyCursorLoader extends AsyncTaskLoader<Cursor> {

    private Context context;
    private Cursor cursor = null;

    public MyCursorLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Cursor loadInBackground() {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ResultsDataSource dataSource = new ResultsDataSource();
        return dataSource.getLastResults(-1, db);
    }

    @Override
    public void deliverResult(Cursor data) {
        if (isStarted()){
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (cursor != null){
            deliverResult(cursor);
        } else {
            forceLoad();
        }

    }
}
