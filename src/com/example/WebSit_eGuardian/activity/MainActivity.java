package com.example.WebSit_eGuardian.activity;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import com.example.WebSit_eGuardian.R;
import com.example.WebSit_eGuardian.adapter.MyCursorAdapter;
import com.example.WebSit_eGuardian.db.DBHelper;
import com.example.WebSit_eGuardian.loader.MyCursorLoader;
import com.example.WebSit_eGuardian.service.MyAlarm;

public class MainActivity extends Activity implements OnCheckedChangeListener, LoaderCallbacks<Cursor>{

    public static boolean isActive = false;
    private static final int LOADER_ID = 0;

    private MainActivity me;

    private Switch swServiceSwitch;
    private MyAlarm myAlarm;
    private ListView listView;
    private MyCursorAdapter cursorAdapter;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        me = this;

        listView = (ListView) findViewById(R.id.lvHistory);
        swServiceSwitch = (Switch) findViewById(R.id.swServiceSwitch);


        myAlarm = new MyAlarm(this);
        swServiceSwitch.setChecked(myAlarm.isActive());
        Log.d("mytime", "Is active[2]: ".concat("" + myAlarm.isActive()));

        swServiceSwitch.setOnCheckedChangeListener(this);

        cursorAdapter = new MyCursorAdapter(this, null, 0);
        listView.setAdapter(cursorAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("mytime", "Received");
                getLoaderManager().restartLoader(LOADER_ID, null, me);
            }
        };
        registerReceiver(receiver, new IntentFilter(DBHelper.DB_UPDATE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.abSettings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        boolean isAlarmActive = myAlarm.isActive();
        Log.d("mytime", "Is active[1]: ".concat("" + isAlarmActive));
        if (isChecked && !isAlarmActive){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String intervalStr = preferences.getString(SettingsActivity.CHECK_INTERVAL_KEY, "");
            long interval = (long) (Double.valueOf(intervalStr) * 60 * 1000);
            myAlarm.setAlarm(interval);
            Log.d("mytime", "Start alarm. Interval " + interval/1000 + " sec");
        } else if (!isChecked && isAlarmActive){
            myAlarm.cancelAlarm();
            Log.d("mytime", "Stop alarm");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case LOADER_ID:
                return new MyCursorLoader(this);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listView.setVisibility(View.VISIBLE);
        Cursor oldCursor = cursorAdapter.swapCursor(data);
        if (oldCursor != null && !oldCursor.isClosed())
            oldCursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Cursor oldCursor = cursorAdapter.swapCursor(null);
        if (oldCursor != null && !oldCursor.isClosed())
            oldCursor.close();
    }
}
