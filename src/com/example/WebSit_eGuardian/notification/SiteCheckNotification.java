package com.example.WebSit_eGuardian.notification;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import com.example.WebSit_eGuardian.activity.MainActivity;
import com.example.WebSit_eGuardian.R;

public class SiteCheckNotification {

    private final int NOTIFICATION_ID = 0;
    private Context c;
    private NotificationManager manager;
    private Notification.Builder mBuilder;
    private Notification notification;

    public SiteCheckNotification(Context context){
        this.c = context;
        manager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new Notification.Builder(c);
    }

    public void sentErrorNotification(int errorNum, String siteUrl){
        PendingIntent pendingIntent = createOpenActivityIntent(MainActivity.class);
        notification = mBuilder.setSmallIcon(android.R.drawable.stat_notify_error)
                .setContentTitle(String.format(getString(R.string.siteTitleError), errorNum))
                .setContentText(String.format(getString(R.string.siteError), siteUrl))
                .setContentIntent(pendingIntent)
                .setLights(getColor(android.R.color.holo_red_light), 800, 1500)
                .setAutoCancel(true).build();
        vibrate();
        manager.notify(NOTIFICATION_ID, notification);
    }

    public void removeErrorNotification(){
        PendingIntent pendingIntent = createOpenActivityIntent(MainActivity.class);
        if (pendingIntent != null){
            manager.cancel(NOTIFICATION_ID);
        }
    }

    private String getString(int resId){
        return c.getString(resId);
    }
    private int getColor(int resId){
        return c.getResources().getColor(resId);
    }

    private void vibrate(){
        Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
        if (v.hasVibrator()){
            long[] vibratePattern = new long[]{0, 300, 300, 300};
            v.vibrate(vibratePattern, -1);
        }
    }

    private PendingIntent createOpenActivityIntent(Class<? extends Activity> activityClass){
        Intent intent = new Intent(c, activityClass);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
        stackBuilder.addParentStack(activityClass);
        stackBuilder.addNextIntent(intent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
