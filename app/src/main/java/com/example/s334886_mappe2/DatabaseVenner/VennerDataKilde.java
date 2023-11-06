package com.example.s334886_mappe2.DatabaseVenner;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;




public class VennerDataKilde {
    private SQLiteDatabase database;
    private DatabaseHjelperVenner dbHelperVenner;

    public VennerDataKilde(Context context) {
        dbHelperVenner = new DatabaseHjelperVenner(context);
    }

    public void open() throws SQLException {
        database = dbHelperVenner.getWritableDatabase();

    }

    public void close() {
        dbHelperVenner.close();
    }




    /*
    Her lager vi egne metoder for kommunikasjon
med databasen via database-objektet
     */
    public Venner leggInnVenn(String vennerNavn, String vennerTelefon) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHjelperVenner.KOLONNE_OPPGAVE_NAVN, vennerNavn);
        values.put(DatabaseHjelperVenner.KOLONNE_OPPGAVE_TELEFON, vennerTelefon);


        long insertId = database.insert(DatabaseHjelperVenner.TABELL_VENNER, null, values);
        Log.d("Insert", "New object inserted with ID: " + insertId);

        Cursor cursor = database.query(DatabaseHjelperVenner.TABELL_VENNER, null,

                DatabaseHjelperVenner.KOLONNE_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Venner nyVenn = cursorTilVenner(cursor);
        cursor.close();
        return nyVenn;
    }


    /* Har en cursor som peker på raden som er satt
     inn. Lager et Oppgave-objekt som returneres */
    private Venner cursorTilVenner(Cursor cursor) {
        Venner venner = new Venner(0, "navn", "telefon");
        venner.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHjelperVenner.KOLONNE_ID)));
        venner.setNavn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelperVenner.KOLONNE_OPPGAVE_NAVN)));
        venner.setTelefon(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelperVenner.KOLONNE_OPPGAVE_TELEFON)));

        return venner;
    }


    // Metode for å finne alle objekter
    public List<Venner> finnAlleVenner() {
        List<Venner> venners = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHjelperVenner.TABELL_VENNER, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Venner venner = cursorTilVenner(cursor);



           venners.add(venner);
            cursor.moveToNext();}
        cursor.close();

        return venners;
    }




    // Metode for å slette
    public void slettVenn(long vennerId) {
        database.delete(DatabaseHjelperVenner.TABELL_VENNER,
                DatabaseHjelperVenner.KOLONNE_ID + " =? ", new String[]{Long.toString(vennerId)});
    }
}


