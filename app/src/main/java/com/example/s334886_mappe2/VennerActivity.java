package com.example.s334886_mappe2;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.s334886_mappe2.DatabaseVenner.Venner;
import com.example.s334886_mappe2.DatabaseVenner.VennerDataKilde;

import java.util.List;



public class VennerActivity extends AppCompatActivity {

    //   private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1; //For sms

    private VennerDataKilde dataKilde;
    private ArrayAdapter<Venner> vennerArrayAdapter;
    private EditText vennerEditText;
    private EditText vennerEditText2;

    private List<Venner> venners;

    private long SelektertItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venner);


        dataKilde = new VennerDataKilde(this);
        dataKilde.open();
        vennerEditText = findViewById(R.id.editText);
        vennerEditText2 = findViewById(R.id.editText2);






        ListView vennerListView = findViewById(R.id.listView);

        venners = dataKilde.finnAlleVenner();
        vennerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, venners);
        vennerListView.setAdapter(vennerArrayAdapter);








        //  Hva som skjer ved klikk på leggtil-knapp
        Button leggtilButton = findViewById(R.id.leggtil);
        leggtilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    String vennNavn = vennerEditText.getText().toString();
                    int vennTelefon = Integer.parseInt(vennerEditText2.getText().toString());

                    if (!vennNavn.isEmpty()) {
                        Venner venner = dataKilde.leggInnVenn(vennNavn, String.valueOf(vennTelefon));



                        vennerArrayAdapter.add(venner);


                        vennerEditText.setText("");
                        vennerEditText2.setText("");


                    }
                }
                catch (Exception e){
                    vennerEditText2.setText("Dette feltet må skrives med nummer");
                }
            }

        });


        EditText editText1 = findViewById(R.id.editText); //For å bruke i catch
        EditText editText2 = findViewById(R.id.editText2);//For å bruke i catch






        //For å få posisjonen til en spesifikk item slik at man kan slette den
        vennerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lagre ID'en til den selekterte itemen
                SelektertItemId = venners.get(position).getId();
            }
        });


        // Hva som skjer ved klikk på slett-knapp
        Button slettButton = findViewById(R.id.slett);

        slettButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (!venners.isEmpty()) {
                        if (SelektertItemId !=-1) {

                          //  for (Venner venn: venners){
      for(int i =0; i<venners.size();i++){//Leser gjennom arraylisten
        if (venners.get(i).getId() == SelektertItemId){//Går gjennom arraylisten og finner alle id'ene til alle elementene,
            //Hvis den er lik selektertItemId(Id'en til element jeg har trukket på), så sletter jeg den
                     dataKilde.slettVenn(venners.get(i).getId()); // Sletter den spesifikke itemen i databasen
                      venners.remove(venners.get(i)); //Sletter itemen fra arraylisten også


                                }
                            }

                            SelektertItemId = -1; //Restarter selektert itemID til default (for å kunne brukes igjen)



                            vennerArrayAdapter.notifyDataSetChanged(); //Oppdaterer adapteren om endringen som ble gjort

                        }
                        else {
                            Toast.makeText(VennerActivity.this, "Trykk på en venn først for å slette", Toast.LENGTH_SHORT).show();
                        }

                    }


                } catch (Exception e) {
                    editText1.setText("Fyll inn navn"); // For feilmelding/input validering
                    editText2.setText("Fyll inn telefonnr"); //For feilmelding/input validering


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
        //     dataKilde.close();  --> Kommenterte vekk close koden slik
        //   at databasen ikke lukker seg når jeg jobber med den andre databasen

        super.onPause();
    }


} //ferdig Venner.activity
