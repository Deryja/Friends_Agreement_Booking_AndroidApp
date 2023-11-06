package com.example.s334886_mappe2.BroadCast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.example.s334886_mappe2.Instillinger;
import com.example.s334886_mappe2.R;

import java.util.Calendar;

public class MinPeriodisk extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


            Toast.makeText(getApplicationContext(), "I MinPeriodisk", Toast.LENGTH_SHORT).show();
            Log.d("", "Nå er du i MinPeriodisk");



            //Henter fram timepicker koden fra SettingsFragment


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedTime = sharedPreferences.getString("selected_time", "06:00");

        Log.d("MinPeriodisk", "Selected Time: " + selectedTime);

        // Splitter tiden inn i timer og minutter
        String[] timeParts = selectedTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);


            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, 0);

        Log.d("MinPeriodisk", "Hour: " + hour + " Minute: " + minute);


            // Hvis tiden er i fortiden, så skal det legges til en hel dag (24 timer)
            if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }

            Intent i = new Intent(this, MinSendService.class);
            PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);

            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
            return super.onStartCommand(intent, flags, startId);
        }


}