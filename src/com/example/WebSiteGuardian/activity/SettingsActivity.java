package com.example.WebSiteGuardian.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.util.Log;
import com.example.WebSiteGuardian.service.MyAlarm;
import com.example.WebSiteGuardian.R;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    public static final String SITE_URL_KEY = "site";
    public static final String CHECK_INTERVAL_KEY = "interval";

    private EditTextPreference siteUrlPreference;
    private EditTextPreference checkIntervalPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        siteUrlPreference = (EditTextPreference)findPreference(SITE_URL_KEY);
        checkIntervalPreference = (EditTextPreference)findPreference(CHECK_INTERVAL_KEY);

        siteUrlPreference.setSummary(siteUrlPreference.getText());
        checkIntervalPreference.setSummary(checkIntervalPreference.getText() + " minutes");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case SITE_URL_KEY:
                siteUrlPreference.setSummary(siteUrlPreference.getText());
                Log.d("mytime", "Change site url");
                break;
            case CHECK_INTERVAL_KEY:
                checkIntervalPreference.setSummary(checkIntervalPreference.getText() + " minutes");
                MyAlarm myAlarm = new MyAlarm(this);
                if (myAlarm.isActive()){
                    Log.d("mytime", "Is active: " + myAlarm.isActive());
                    long interval = (long) (Double.parseDouble(checkIntervalPreference.getText()) * 60 * 1000);
                    myAlarm.setAlarm(interval);
                }
                Log.d("mytime", "Change interval");
                break;
        }
    }
}
