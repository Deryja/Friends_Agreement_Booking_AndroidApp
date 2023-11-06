package com.example.s334886_mappe2;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.s334886_mappe2.BroadCast.MinBroadcastReceiver;
import com.example.s334886_mappe2.BroadCast.MinSendService;
import com.example.s334886_mappe2.BroadCast.MinPeriodisk;

import java.util.Calendar;

public class Instillinger extends AppCompatActivity {




    private SharedPreferences preferanser;

    private Button velgTid;
    private TextView selektertTid;


    private Switch switchKnapp;
    String CHANNEL_ID = "MinKanal";

    EditText editTextSMS;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instillinger);




    //FOR SMS

        editTextSMS = findViewById(R.id.DefaultSMSMelding);
        SharedPreferences sharedPreferencesSMS = PreferenceManager.getDefaultSharedPreferences(this);

//Henter fram den lagde meldingen fra SharedPreferences og setter det inn i editext
        String egenDefinertSMS = sharedPreferencesSMS.getString("message_key", "");
        editTextSMS.setText(egenDefinertSMS);

//Legger til tekst changeListener sånn at meldingen oppdateres når enn bruker skriver
        editTextSMS.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Automatisk generert
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//Automatisk generert
            }


            @Override
            public void afterTextChanged(Editable melding) {
              //Oppdaterer selektertMelding når enn teksten endres
                String newMelding = melding.toString();
                SharedPreferences.Editor editor = sharedPreferencesSMS.edit();
                editor.putString("message_key", newMelding);
                editor.apply(); //Legger til disse nye endringene inn til metoden i SettingsFragment
            }
        });




//For timePicker:
        velgTid = findViewById(R.id.velgtid);
        selektertTid = findViewById(R.id.idSelektertTid);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedTime = sharedPreferences.getString("selected_time", "06:00");
        selektertTid.setText(selectedTime);



//Knapp for tid
        velgTid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int Minutt = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Instillinger.this,
                        (timePickerView, hourOfDay, selectedMinute) -> {
                            selektertTid.setText(hourOfDay + ":" + selectedMinute);

                            //Lagrer tiden bruker trykker inn til sharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("selected_time", hourOfDay + ":" + selectedMinute);
                            editor.apply();
                        }, hour, Minutt, true);

                timePickerDialog.show();
            }
        });










        //For notification
        createNotificationChannel();








        //For SwtichKnapp i preferanser
        preferanser = getSharedPreferences("MinePreferanser", MODE_PRIVATE);

        switchKnapp = findViewById(R.id.switchKnapp); // Replace with the actual ID of your switch in the layout

        // Set the switch state based on the saved preference
        boolean switchState = preferanser.getBoolean("switch_preference", false);
        switchKnapp.setChecked(switchState);



// Setter en onCheckedChanged for å tilskrive SharedPreferences koden til switch knappen,
// og lagre valget (av å aktivere eller skru av switch kanppen)


        switchKnapp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the switch state in SharedPreferences
                SharedPreferences.Editor editor = preferanser.edit();
                editor.putBoolean("switch_preference", isChecked);
                editor.apply();

                if (isChecked) {
                    // Handle when the switch is turned on
                    Log.e("", "Switch er aktivert");
                    Intent intent = new Intent();
                    intent.setAction("com.example.service.MITTSIGNAL");
                    sendBroadcast(intent);


                } else {
                    // Handle when the switch is turned off
                    Log.d("", "Switch er ikke aktivert");
                }
            }
        });







        //For Broadcast
        BroadcastReceiver myBroadcastReceiver = new MinBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.example.service.MITTSIGNAL");
        filter.addAction("com.example.service.MITTSIGNAL");
        this.registerReceiver(myBroadcastReceiver, filter);


    }


    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }





    //For å få switch knappen til å fungere
    public void onSwitchClick(View view) {


        Switch switchKnapp = (Switch) view;


            if (switchKnapp.isChecked()) {


                Log.e("", "Switch er trukket på");
    Intent intent = new Intent();
    intent.setAction("com.example.service.MITTSIGNAL");
    sendBroadcast(intent);


            }
            else {
                // Når switch ikke aktiveres
              Log.d("", "Switch er ikke trukket på");
            }
        }
    }



