package com.example.s334886_mappe2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.s334886_mappe2.BroadCast.MinSendService;

//For preferences
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }


   @Override
   public void onResume(){
        super.onResume();


        //Henter default verdien av det jeg har valgt for sharedPreferences
       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

       //Refererer til switch knappen (for å få dens default)
       boolean serviceEnabled = preferences.getBoolean("switch_preference", false);


       //For å starte notifikasjoner/sms tjenestene
       if (serviceEnabled){
          startService(new Intent(this, MinSendService.class));}


       //For å stoppe notifikasjoner/sms tjenestene
       else {
         stopService(new Intent(this, MinSendService.class));}


   }

}

