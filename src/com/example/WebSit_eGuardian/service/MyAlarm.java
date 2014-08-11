package com.example.WebSit_eGuardian.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class MyAlarm {

    private AlarmManager am;
    private Intent intent;
    private PendingIntent pendingIntent;
    private Context context;

    private final int ALARM_ID = 0;

    public MyAlarm(Context context){
        this.context = context;
        am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, SiteCheckService.class);
    }

    public void setAlarm(long interval){
        pendingIntent = PendingIntent.getService(context, ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000, interval, pendingIntent);
    }

    public void cancelAlarm(){
        pendingIntent = PendingIntent.getService(context, ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public boolean isActive(){
        PendingIntent pi = PendingIntent.getService(context, ALARM_ID, intent, PendingIntent.FLAG_NO_CREATE);
        return (pi != null);
    }
}
