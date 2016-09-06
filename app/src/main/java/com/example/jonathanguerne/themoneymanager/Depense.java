package com.example.jonathanguerne.themoneymanager;

import java.io.Serializable;

/**
 * Created by jonathan.guerne on 14.08.2016.
 */
public class Depense implements Serializable {
    private int ID;
    private double sommeDepense;
    private Personne debiteur;
    private String description;
    private String date;

    public Depense(int ID,double sommeDepense, Personne debiteur, String description, String date) {
        this.ID=ID;
        this.sommeDepense = sommeDepense;
        this.debiteur = debiteur;
        this.description = description;
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getSommeDepense() {
        return sommeDepense;
    }

    public void setSommeDepense(double sommeDepense) {
        this.sommeDepense = sommeDepense;
    }

    public Personne getDebiteur() {
        return debiteur;
    }

    public void setDebiteur(Personne debiteur) {
        this.debiteur = debiteur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
