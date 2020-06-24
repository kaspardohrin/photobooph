package com.example.photobooph;

import android.content.Context;
import android.content.res.Configuration;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;

public class SettingsActivity extends PreferenceActivity {
    public final static String LOG_TAG = "Settings: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

    public static boolean nightMode(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("check_box_night_mode", false);
    }

    public static boolean language(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("check_box_language", false);
    }

    @Override
    protected void onResume() {
        if (SettingsActivity.nightMode(this)) {
            Log.d(LOG_TAG, "nightmode enabled");

            setTheme(R.style.DarkTheme);
        }
        super.onResume();

        if (!SettingsActivity.nightMode(this)) {
            Log.d(LOG_TAG, "daymode enabled");

            setTheme(R.style.AppTheme);
        }
        super.onResume();

        if (SettingsActivity.language(this)) {
            Log.d(LOG_TAG, "dutchmode enabled");

            Locale locale = new Locale("nl");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        super.onResume();

        if (!SettingsActivity.language(this)) {
            Log.d(LOG_TAG, "englishmode enabled");

            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        super.onResume();
    }
}