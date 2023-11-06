package com.example.s334886_mappe2.DatabaseAvtaler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/*
Redigering -->
1. lag layout for liste av venner, med ny database og tabell for venner til den (navn og tlf)

2. Når lager avtale, ta kun dato, klokkeslett og treffsted, men også tlf nr for å kunne sende sms til riktig venn


 */



public class OppgaveDataKilde {
    private SQLiteDatabase database;
    private DatabaseHjelper dbHelper;

    public OppgaveDataKilde(Context context) {
        dbHelper = new DatabaseHjelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

    }

    public void close() {
        dbHelper.close();
    }




    /*
    Her lager vi egne metoder for kommunikasjon
med databasen via database-objektet
     */
    public Oppgave leggInnOppgave(String oppgaveTelefon, String oppgaveSted, String oppgaveKlokkeslett, String oppgaveDato) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHjelper.KOLONNE_OPPGAVE_TELEFON, oppgaveTelefon);
        values.put(DatabaseHjelper.KOLONNE_OPPGAVE_STED, oppgaveSted);
        values.put(DatabaseHjelper.KOLONNE_OPPGAVE_KLOKKESLETT, oppgaveKlokkeslett);
        values.put(DatabaseHjelper.KOLONNE_OPPGAVE_DATO, oppgaveDato);

        long insertId = database.insert(DatabaseHjelper.TABELL_AVTALER, null, values);
        Log.d("Insert", "New object inserted with ID: " + insertId);

        Cursor cursor = database.query(DatabaseHjelper.TABELL_AVTALER, null,
                DatabaseHjelper.KOLONNE_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Oppgave nyOppgave = cursorTilOppgave(cursor);
        cursor.close();
        return nyOppgave;
    }


   /* Har en cursor som peker på raden som er satt
    inn. Lager et Oppgave-objekt med verdier fra databasen */
    private Oppgave cursorTilOppgave(Cursor cursor) {
        Oppgave oppgave = new Oppgave(0, "telefon", "sted", "klokkeslett","dato"); //Objektet som skal fylles ut med verdier fra databasen
        oppgave.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_ID)));
        oppgave.setTelefon(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_OPPGAVE_TELEFON)));
        oppgave.setSted(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_OPPGAVE_STED)));
        oppgave.setKlokkeslett(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_OPPGAVE_KLOKKESLETT)));
        oppgave.setDato(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_OPPGAVE_DATO)));

        return oppgave;
    }


   // Metode for å legge inn objektene inni arrayliste --> objekter som ble laget med verdier fra databasen
    public List<Oppgave> finnAlleOppgaver() {
        List<Oppgave> oppgaver = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHjelper.TABELL_AVTALER, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {


            Oppgave oppgave = cursorTilOppgave(cursor);

            oppgaver.add(oppgave);
            cursor.moveToNext();}

        cursor.close();

        return oppgaver;
    }




   // Metode for å slette
    public void slettOppgave(long oppgaveId) {
        database.delete(DatabaseHjelper.TABELL_AVTALER,
                DatabaseHjelper.KOLONNE_ID + " =? ", new String[]{Long.toString(oppgaveId)});
    }
}



