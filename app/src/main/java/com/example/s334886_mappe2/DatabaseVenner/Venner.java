package com.example.s334886_mappe2.DatabaseVenner;

public class Venner {
    private long id;
    private String navn;
    private String telefon;


    public Venner(long id, String navn, String telefon) {
        this.id = id;
        this.navn = navn;
        this.telefon = telefon;

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getTelefon() {return telefon;}

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }



    @Override
    public String toString() {
        return "ID: " + id + ", Navn: " + navn + ", Telefon: " + telefon;
    }
}



