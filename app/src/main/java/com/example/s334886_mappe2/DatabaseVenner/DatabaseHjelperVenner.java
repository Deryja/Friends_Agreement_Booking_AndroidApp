package com.example.s334886_mappe2.DatabaseVenner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHjelperVenner extends SQLiteOpenHelper {
    private static final String DATABASE_NAVN = "venner.db";
    private static final int DATABASE_VERSION = 3;
    public static final String TABELL_VENNER = "venner";
    public static final String KOLONNE_ID = "id";
    public static final String KOLONNE_OPPGAVE_NAVN = "navn";
    public static final String KOLONNE_OPPGAVE_TELEFON = "telefon";



    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABELL_VENNER + "("
            + KOLONNE_ID + " INTEGER PRIMARY KEY, " + KOLONNE_OPPGAVE_TELEFON
            + " INTEGER NOT NULL, " + KOLONNE_OPPGAVE_NAVN + " TEXT NOT NULL)";


    public DatabaseHjelperVenner(Context context) {
        super(context, DATABASE_NAVN, null, DATABASE_VERSION);}



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASKS);}




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)

    {
        db.execSQL("DROP TABLE IF EXISTS " + TABELL_VENNER); //Når tabellen oppgraderes, så blir den gamle tabellen slettet.
        // For å aktivere denne så må vi bare køke database_version med 1 (f.eks skrive 2 osv)
        onCreate(db);}


}
