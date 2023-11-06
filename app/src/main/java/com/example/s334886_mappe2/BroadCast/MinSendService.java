package com.example.s334886_mappe2.BroadCast;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.example.s334886_mappe2.DatabaseAvtaler.DatabaseHjelper;
import com.example.s334886_mappe2.DatabaseAvtaler.Oppgave;
import com.example.s334886_mappe2.DatabaseAvtaler.OppgaveDataKilde;
import com.example.s334886_mappe2.MainActivity;
import com.example.s334886_mappe2.R;

import java.util.List;


public class MinSendService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//For å bruke avtaler som er laget med verdier fra databasen (fra annen class - OppgaveDataKilde)
    private OppgaveDataKilde dataKilde;
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;


    EditText editTextSMS;



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Minservice", "Service laget");


        //For å bruke avtaler som er laget med verdier fra databasen (fra annen class - OppgaveDataKilde)
        dataKilde = new OppgaveDataKilde(this);
        dataKilde.open();




    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




//Henter ut avtalene fra arraylisten (som har objekter som ble generert direkte fra verdiene inni databasen)
        List<Oppgave> oppgaver = dataKilde.finnAlleOppgaver();


//Lager en spesiell id for hver notifikasjon, slik at notifikansId kan telles opp i notificationManager
// for å generere flere notifikasjoner

      int NotificationId = 1;


// At det kun kommer notifikasjoner hvis det faktisk er avtaler som er registrert:
        if (!oppgaver.isEmpty()) {



//Skrive notifikasjonsmeldingen med kun dato, klokkeslett og sted (som er generert med cursor fra database i OppgaveDataKilde klassen)
for (Oppgave oppgave: oppgaver){
            String notifikasjonsmelding = oppgave.getDato() + " " +
                    oppgave.getKlokkeslett() + " " + oppgave.getSted();



            //Legger til logikk her for å bruke meldingen i editText: --> for SMS'en




    SharedPreferences sharedPreferencesSMS = PreferenceManager.getDefaultSharedPreferences(this); //Sier til koden at jeg skal nå bruke sharedPreferencesSMS fra SettingsFragment
    String egenDefinertSMS = sharedPreferencesSMS.getString("message_key", ""); //Henter fram verdiene fra metoden i SettingsFragment
    //(som ble lagt til pga metoden i Instillinger med editText og putString. Nå bruker vi getString

    Log.d("MinSendService", "Selektert melding: " + egenDefinertSMS);

    if (!egenDefinertSMS.isEmpty()) {

        sendMessage(oppgave.getTelefon(), egenDefinertSMS);
    }

   else  {
        sendMessage(oppgave.getTelefon(), "Ikke glem avtalen vi har: " + notifikasjonsmelding);
    }



            Toast.makeText(getApplicationContext(), "I MinSendService", Toast.LENGTH_SHORT).show();
            Log.d("", "Nå er du i MinSendService");

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            Intent i = new Intent(this, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
            Notification notifikasjon = new NotificationCompat.Builder(this, "MinKanal")
                    .setContentTitle("Avtaler")
                    .setContentText("Husk at du har avtale: " + notifikasjonsmelding)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pIntent).build();
            notifikasjon.flags |= Notification.FLAG_AUTO_CANCEL;

            //Viktig å telle opp NotifikasjonsIden ettersom man da kan få inn flere notifikasjoner samtidig
    //Ved å øke den fra 1 og oppover (basert på hvor mange avtaler/notifikasjoner som skal sendes)
            notificationManager.notify(NotificationId++, notifikasjon);

        }}
            return super.onStartCommand(intent, flags, startId);
        }





        //For SMS - tjenesten --> Tillattelser
public boolean sjekkTillattelseSMS(String tillattelse){
        int sjekk = ContextCompat.checkSelfPermission(this, tillattelse);
        return (sjekk == PackageManager.PERMISSION_GRANTED);
}


//For SMS tjenesten (selve SMS-meldingen)
    private void sendMessage (String telefonnr, String melding) {


        if (sjekkTillattelseSMS(Manifest.permission.SEND_SMS)) {

            if (!telefonnr.isEmpty() && !melding.isEmpty()) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(telefonnr, null, melding, null, null);
                Toast.makeText(this, "SMS sendt", Toast.LENGTH_SHORT).show();}

            else {
                Toast.makeText(this, "",
                        Toast.LENGTH_SHORT).show();}


        }

      else {
            System.out.println("Ingen tillattelse for å sende SMS er gitt");
        }



    }





        @Override
        public void onDestroy () {
            super.onDestroy();
            Log.d("Minservice", "Service 5ernet");
        }


    }
