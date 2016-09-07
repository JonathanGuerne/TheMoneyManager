package com.example.jonathanguerne.themoneymanager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jonathan.guerne on 13.08.2016.
 */
public class Personne implements Serializable {
    private String name;
    private double detteTotale;
    private ArrayList<Depense> listeDepense;
    private ArrayList<Dette> listeDette;
    private int id;

    public Personne(int id,String name) {
        this.id=id;
        this.name = name;
        this.detteTotale=0;
        listeDepense= new ArrayList<>();
        listeDette = new ArrayList<>();
    }

    public int getId(){

        return id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public double getDetteTotale() {
        return detteTotale;
    }

    public void setDetteTotale(double detteTotale) {
        this.detteTotale = detteTotale;
    }

    public ArrayList<Depense> getListeDepense() {
        return listeDepense;
    }

    public void setListeDepense(ArrayList<Depense> listeDepense) {
        this.listeDepense = listeDepense;
    }

    public ArrayList<Dette> getListeDette() {
        return listeDette;
    }

    public void setListeDette(ArrayList<Dette> listeDette) {
        this.listeDette = listeDette;
    }
}
