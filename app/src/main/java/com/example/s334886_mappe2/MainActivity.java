package com.example.s334886_mappe2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.s334886_mappe2.DatabaseAvtaler.Oppgave;
import com.example.s334886_mappe2.DatabaseAvtaler.OppgaveDataKilde;

import java.util.List;

public class MainActivity extends AppCompatActivity {



    private OppgaveDataKilde dataKilde;
    private ArrayAdapter<Oppgave> oppgaveArrayAdapter;

    private EditText oppgaveEditText2;
    private EditText oppgaveEditText3;
    private EditText oppgaveEditText4;
    private EditText oppgaveEditText5;
    private List<Oppgave> oppgaver;

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1; //For sms

    private long selektertItemId = -1; // Variable to store the selected item's ID


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//Tilattelse for sending av SMS
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSION_REQUEST_CODE);
        }


        dataKilde = new OppgaveDataKilde(this);
        dataKilde.open();

        oppgaveEditText2 = findViewById(R.id.editText2);
        oppgaveEditText3 = findViewById(R.id.editText3);
        oppgaveEditText4 = findViewById(R.id.editText4);
        oppgaveEditText5 = findViewById(R.id.editText5);


//For å gå til liste over venner
        Button VennerKnapp = (Button) findViewById(R.id.MineVenner);


        Intent GåTilVennerLayout = new Intent(this, VennerActivity.class);

        VennerKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GåTilVennerLayout);
            }
        });


        //For å gå til preferanser/instillinger layout
        ImageButton PreferanserKnapp = (ImageButton) findViewById(R.id.instillinger);


        Intent GåTilPreferanserLayout = new Intent(this, Instillinger.class);

        PreferanserKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GåTilPreferanserLayout);
            }
        });


//Hvordan slette spesifikke items ved å trykke på de inne i listen:
        ListView oppgaveListView = findViewById(R.id.listView);

        oppgaver = dataKilde.finnAlleOppgaver();
        oppgaveArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, oppgaver);
        oppgaveListView.setAdapter(oppgaveArrayAdapter);





        //For å få posisjonen til en spesifikk item slik at man kan slette den
        oppgaveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lagre ID'en til den selekterte itemen
                selektertItemId = oppgaver.get(position).getId();
            }
        });


        //  Hva som skjer ved klikk på leggtil-knapp
        Button leggtilButton = findViewById(R.id.leggtil);
        leggtilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    int oppgaveTelefon = Integer.parseInt(oppgaveEditText2.getText().toString());
                    String oppgaveSted = oppgaveEditText3.getText().toString();
                    String oppgaveKlokkeslett = oppgaveEditText4.getText().toString();
                    String oppgaveDato = oppgaveEditText5.getText().toString();


                    //For å opprette et objekt av avtale og samtidig sette det inn i databasen
                    if (!oppgaveSted.isEmpty() && !oppgaveKlokkeslett.isEmpty() && !oppgaveDato.isEmpty()) {
                        Oppgave oppgave = dataKilde.leggInnOppgave(String.valueOf(oppgaveTelefon),
                                oppgaveSted, oppgaveKlokkeslett, oppgaveDato);


                        //Legge til alle objekter av avtaler inni array for å liste det i layouten senere
                        oppgaveArrayAdapter.add(oppgave);


                        oppgaveEditText2.setText("");
                        oppgaveEditText3.setText("");
                        oppgaveEditText4.setText("");
                        oppgaveEditText5.setText("");

                    }
                } catch (Exception e) {
                    oppgaveEditText2.setText("Dette feltet må skrives med nummer");
                }


            }

        });


        EditText editText2 = findViewById(R.id.editText2);//For å bruke i catch
        EditText editText3 = findViewById(R.id.editText3);//For å bruke i catch
        EditText editText4 = findViewById(R.id.editText4);//For å bruke i catch
        EditText editText5 = findViewById(R.id.editText5);//For å bruke i catch


        // Hva som skjer ved klikk på slett-knapp
        Button slettButton = findViewById(R.id.slett);
        slettButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (!oppgaver.isEmpty()) {
                        if (selektertItemId != -1) {



  //Lager et objekt av klasse Oppgave, og legger til logikken: hvis en oppgave i lista har samme id som itemen
  //Vi trykker på, så skal det objektet slettes
                     //       for (Oppgave oppgave : oppgaver) {

   for(int i =0; i<oppgaver.size();i++){//Går gjennom arraylisten og finner alle id'ene til alle elementene,
       //Hvis den er lik selektertItemId(Id'en til element jeg har trukket på), så sletter jeg den
           if (oppgaver.get(i).getId() == selektertItemId) { // Sletter den spesifikke itemen i databasen
                                    dataKilde.slettOppgave(oppgaver.get(i).getId()); // Sletter den spesifikke itemen
                                    oppgaver.remove(oppgaver.get(i)); //Sletter itemen fra arraylisten også

                                }
                            }
                            selektertItemId = -1; //Restarter selektertItemId til default



                            oppgaveArrayAdapter.notifyDataSetChanged(); //Gir adapter beskjed om det
                        }

                        else {
                            Toast.makeText(MainActivity.this, "Trykk på en avtale først for å slette", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    editText2.setText("Fyll inn telefonnr"); //For feilmelding/input validering
                    editText3.setText("Fyll inn sted"); //For feilmelding/input validering
                    editText4.setText("Fyll inn klokkeslett"); //For feilmelding/input validering
                    editText5.setText("Fyll inn dato"); //For feilmelding/input validering
                }
            }
        });
    }



        // Må huske å stenge db-oppkobling
    @Override
    protected void onResume() {
        dataKilde.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
     //   dataKilde.close(); //Kommenterte vekk close koden slik
        //   at databasen ikke lukker seg når jeg jobber med den andre databasen
        super.onPause();
    }


}
