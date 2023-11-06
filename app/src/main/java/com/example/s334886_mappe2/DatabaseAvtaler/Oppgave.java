package com.example.s334886_mappe2.DatabaseAvtaler;

public class Oppgave {
    private long id;
    private String telefon;
    private String sted;
    private String klokkeslett;
    private String dato;


    public Oppgave(long id, String telefon, String sted, String klokkeslett, String dato) {
        this.id = id;

        this.telefon = telefon;
        this.sted = sted;
        this.klokkeslett = klokkeslett;
        this.dato = dato;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public String getTelefon() {return telefon;}

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getSted() {
        return sted;
    }

    public void setSted(String sted) {
        this.sted = sted;
    }


    public String getKlokkeslett() {
        return klokkeslett;
    }

    public void setKlokkeslett(String klokkeslett) {
        this.klokkeslett = klokkeslett;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Telefon: " + telefon + ", Sted: "+ sted
                + ", Klokkeslett: "+ klokkeslett
                + ", Dato: "+ dato ;
    }
}



