package com.aitekteam.developer.playmovie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.aitekteam.developer.playmovie.receiver.AlarmReceiver;

import java.util.Calendar;
import java.util.Locale;

public class ChangeLanguageActivity extends AppCompatActivity {

    public static final String SHR_PRF = "com.aitekteam.developer.playmovie";
    public static final String SHR_PRF_DAILY = "com.aitekteam.developer.playmovie.DAILY";
    public static final String SHR_PRF_RELEASE = "com.aitekteam.developer.playmovie.RELEASE";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);

        RadioButton btnLanguageIdFull, btnLanguageEnFull;
        final Switch daily, release;
        btnLanguageEnFull = findViewById(R.id.btn_language_en_full);
        btnLanguageIdFull = findViewById(R.id.btn_language_id_full);
        daily = findViewById(R.id.daily);
        release = findViewById(R.id.release);
        sharedPreferences = this.getSharedPreferences(SHR_PRF, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!sharedPreferences.getString(SHR_PRF, "en-US").equals("en-US"))
            btnLanguageIdFull.setChecked(true);
        else btnLanguageEnFull.setChecked(true);

        if (sharedPreferences.getBoolean(SHR_PRF_DAILY, false))
            daily.setChecked(true);
        else daily.setChecked(false);

        if (sharedPreferences.getBoolean(SHR_PRF_RELEASE, false))
            release.setChecked(true);
        else release.setChecked(false);

        btnLanguageEnFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage("en");
                editor.putString(SHR_PRF, "en-US");
                editor.apply();
            }
        });
        btnLanguageIdFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage("in");
                editor.putString(SHR_PRF, "id-ID");
                editor.apply();
            }
        });
        daily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    daily.setChecked(true);
                    saveReminder(SHR_PRF, true,"07:00", true);
                    editor.putBoolean(SHR_PRF_DAILY, true);
                    editor.apply();
                }
                else {
                    daily.setChecked(false);
                    saveReminder(SHR_PRF, true,"07:00", false);
                    editor.putBoolean(SHR_PRF_DAILY, false);
                    editor.apply();
                }
            }
        });
        release.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    release.setChecked(true);
                    saveReminder(SHR_PRF, false,"08:00", true);
                    editor.putBoolean(SHR_PRF_RELEASE, true);
                    editor.apply();
                }
                else {
                    release.setChecked(false);
                    saveReminder(SHR_PRF, false,"08:00", false);
                    editor.putBoolean(SHR_PRF_RELEASE, false);
                    editor.apply();
                }
            }
        });
    }

    private void changeLanguage(String lang) {
        Locale locale = new Locale(lang);
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, metrics);

        Toast.makeText(this, R.string.msg_language_dummy, Toast.LENGTH_SHORT).show();
    }

    private void saveReminder(String ref, boolean type, String time, boolean status) {

        Log.d("HAHAHA", "TYPE: " + String.valueOf(type) + " STATUS: " + String.valueOf(status));

        Calendar alarm = Calendar.getInstance();
        alarm.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
        alarm.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
        alarm.set(Calendar.SECOND, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra(ref, type);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        if (status) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        else am.cancel(pendingIntent);
    }
}
