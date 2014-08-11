package com.example.WebSiteGuardian.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import com.example.WebSiteGuardian.R;
import com.example.WebSiteGuardian.activity.MainActivity;
import com.example.WebSiteGuardian.activity.SettingsActivity;
import com.example.WebSiteGuardian.db.DBHelper;
import com.example.WebSiteGuardian.db.ResultsDataSource;
import com.example.WebSiteGuardian.net.HandleHTTP;
import com.example.WebSiteGuardian.notification.SiteCheckNotification;


public class SiteCheckService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String siteUrl = preferences.getString(SettingsActivity.SITE_URL_KEY, getString(R.string.defaultSiteUrl));
        Log.d("mytime", "Async[1]: ".concat(siteUrl));
        new HttpRequest().execute(siteUrl);
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("mytime", "Service onBind() start");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class HttpRequest extends AsyncTask<String, Void, Void>{

        private int serverStatus;

        @Override
        protected Void doInBackground(String... params) {
            String siteUrl = params[0];
            Log.d("mytime", "Async[2]: ".concat(siteUrl));
            HandleHTTP handleHTTP = new HandleHTTP(siteUrl);
            serverStatus = handleHTTP.getServerStatus();

            SiteCheckResult checkResult = new SiteCheckResult(siteUrl, System.currentTimeMillis(), serverStatus);
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            ResultsDataSource resultsDataSource = new ResultsDataSource();
            resultsDataSource.addResult(checkResult, dbHelper.getWritableDatabase());

            SiteCheckNotification notification = new SiteCheckNotification(getApplicationContext());

            if (MainActivity.isActive){
                notifyDataChanged();
            } else {
                if (serverStatus/100 != 2)
                    notification.sentErrorNotification(serverStatus, siteUrl);
                else
                    notification.removeErrorNotification();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("mytime", "Status " + serverStatus);
        }
    }

    private void notifyDataChanged(){
        Intent intent=new Intent();
        intent.setAction(DBHelper.DB_UPDATE);
        sendBroadcast(intent);
    }

    public static class SiteCheckResult {

        private String siteUrl;
        private long checkedTime;
        private int result;

        public SiteCheckResult(String siteUrl, long checkedTime, int result) {
            this.siteUrl = siteUrl;
            this.checkedTime = checkedTime;
            this.result = result;
        }

        public String getSiteUrl() {
            return siteUrl;
        }

        public long getCheckedTime() {
            return checkedTime;
        }

        public int getResult() {
            return result;
        }
    }
}
