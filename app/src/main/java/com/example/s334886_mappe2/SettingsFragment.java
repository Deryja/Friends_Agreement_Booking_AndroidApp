package com.example.s334886_mappe2;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.s334886_mappe2.R;


//For preferences
public class SettingsFragment extends PreferenceFragmentCompat {




    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref, rootKey);





        //Legge til preferanser for switch-knappen
        SwitchPreference switchPreference = findPreference("switch_preference"); //Referer til key i pref.xml


        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {

                boolean enabled = (boolean) newValue;


//For å save tilstanden (slik at når switch kanppen skrus på og bruker går inn i en annen activity, så er den fortsatt på
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch_preference", enabled);
                editor.apply();


                return true;
            }
        });




        //For timePicker

        // Saver tiden når preferanse av tid er endret
        findPreference("selected_time").setOnPreferenceChangeListener((preference, newValue) -> {

            // Save the selected time in SharedPreferences.
            String selectedTime = (String) newValue;
            android.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
                    .edit()
                    .putString("selected_time", selectedTime)
                    .apply();
            return true;
        });



        findPreference("message_key").setOnPreferenceChangeListener(((preference, newValue) -> {

            String egenDefinertSMS = (String) newValue;
            android.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
                    .edit()
                    .putString("message_key", egenDefinertSMS)
                    .apply();
return true;
        }));

    }
    }


