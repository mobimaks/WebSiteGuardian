package com.example.WebSiteGuardian.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.WebSiteGuardian.R;
import com.example.WebSiteGuardian.db.ResultsTable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final int SITE_URL_INDEX = cursor.getColumnIndex(ResultsTable.SITE_URL);
        final int CHECKED_TIME_INDEX = cursor.getColumnIndex(ResultsTable.CHECK_TIME);
        final int CHECK_RESULT_INDEX = cursor.getColumnIndex(ResultsTable.CHECK_RESULT);

        TextView tvCheckTime = (TextView)view.findViewById(R.id.tvCheckTime);
        TextView tvSiteUrl = (TextView)view.findViewById(R.id.tvSiteUrl);
        TextView tvCheckResult = (TextView)view.findViewById(R.id.tvCheckResult);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM HH:mm:ss");
        Date date = new Date(cursor.getLong(CHECKED_TIME_INDEX));
        tvCheckTime.setText(dateFormat.format(date));

        tvSiteUrl.setText(cursor.getString(SITE_URL_INDEX));

        int statusCode = cursor.getInt(CHECK_RESULT_INDEX);
        tvCheckResult.setText("" + statusCode);
        if (statusCode/100 == 2){
            tvCheckResult.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
            tvCheckResult.setTextColor(context.getResources().getColor(android.R.color.black));
        } else {
            tvCheckResult.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
            tvCheckResult.setTextColor(context.getResources().getColor(android.R.color.white));
        }
    }
}
