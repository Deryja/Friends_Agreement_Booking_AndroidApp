package com.example.s334886_mappe2.DatabaseAvtaler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHjelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAVN = "avtaler.db";
    private static final int DATABASE_VERSION = 5;
    public static final String TABELL_AVTALER = "avtaler";
    public static final String KOLONNE_ID = "id";

    public static final String KOLONNE_OPPGAVE_TELEFON = "telefon";
    public static final String KOLONNE_OPPGAVE_STED = "sted";
    public static final String KOLONNE_OPPGAVE_KLOKKESLETT = "klokkeslett";
    public static final String KOLONNE_OPPGAVE_DATO = "dato";

    private static final String CREATE_TABLE_TASKS = "CREATE TABLE "
            + TABELL_AVTALER + "(" + KOLONNE_ID + " INTEGER PRIMARY KEY, " + KOLONNE_OPPGAVE_TELEFON
            + " INTEGER NOT NULL, " +
            KOLONNE_OPPGAVE_STED + " TEXT NOT NULL, "
            + KOLONNE_OPPGAVE_KLOKKESLETT + " TEXT NOT NULL, " + KOLONNE_OPPGAVE_DATO + " TEXT NOT NULL)";



    public DatabaseHjelper(Context context) {
        super(context, DATABASE_NAVN, null, DATABASE_VERSION);}



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASKS);}


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)

    {
        db.execSQL("DROP TABLE IF EXISTS " + TABELL_AVTALER); //Når tabellen oppgraderes, så blir den gamle tabellen slettet.
        // For å aktivere denne så må vi bare køke database_version med 1 (f.eks skrive 2 osv)
        onCreate(db);}



}



