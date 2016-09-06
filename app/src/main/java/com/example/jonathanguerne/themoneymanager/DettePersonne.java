package com.example.jonathanguerne.themoneymanager;

/**
 * Created by jonathan.guerne on 14.08.2016.
 */
public class DettePersonne {
    private Personne debiteur;
    private double sommeTotdette;

    public DettePersonne(Personne debiteur, double sommeTotdette) {
        this.debiteur = debiteur;
        this.sommeTotdette = sommeTotdette;
    }

    public void addDette(double dette){
        sommeTotdette+=dette;
    }

    public Personne getDebiteur() {
        return debiteur;
    }

    public void setDebiteur(Personne debiteur) {
        this.debiteur = debiteur;
    }

    public double getSommeTotdette() {
        return sommeTotdette;
    }

    public void setSommeTotdette(double sommeTotdette) {
        this.sommeTotdette = sommeTotdette;
    }
}
