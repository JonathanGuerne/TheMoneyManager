package com.example.jonathanguerne.themoneymanager;

import java.io.Serializable;

/**
 * Created by jonathan.guerne on 14.08.2016.
 */
public class Dette implements Serializable{
    private int ID;
    private double sommeDette;
    private double sommeDejaPayer;
    private Personne creancier;
    private Depense depense;

    public Dette(int ID,double sommeDette, Personne creancier, Depense depense) {
        this.ID=ID;
        this.sommeDette = sommeDette;
        this.sommeDejaPayer=0;
        this.creancier = creancier;
        this.depense = depense;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getSommeDette() {
        return sommeDette;
    }

    public void setSommeDette(double sommeDette) {
        this.sommeDette = sommeDette;
    }

    public Personne getCreancier() {
        return creancier;
    }

    public void setCreancier(Personne creancier) {
        this.creancier = creancier;
    }

    public Depense getDepense() {
        return depense;
    }

    public void setDepense(Depense depense) {
        this.depense = depense;
    }

    public double getSommeDejaPayer() {
        return sommeDejaPayer;
    }

    public void setSommeDejaPayer(double sommeDejaPayer) {
        this.sommeDejaPayer = sommeDejaPayer;
    }
}
