package com.example.jonathanguerne.themoneymanager;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by jonathan.guerne on 20.08.2016.
 */
public class Util {

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/theMoneyManger";
    public static String pathPref = "";
    public static String workingDirectory = "";


    public static int nbDette = 0;
    public static int nbDepense = 0;


    public static void Sauvegarder(ArrayList<Personne> listePersonne) {
        if (listePersonne != null) {
            String outPersonne = "", outDepense = "", outDette = "";

            outDepense += Util.nbDepense + "\n";
            outDette += Util.nbDette + "\n";

            for (Personne p : listePersonne) {
                outPersonne += p.getId() + ";" + p.getName() + ";" + p.getDetteTotale() + "\n";
                for (Depense d : p.getListeDepense()) {
                    outDepense += d.getID() + ";" + d.getSommeDepense() + ";" + d.getDescription() + ";" + d.getDate() + ";" + p.getId() + "\n";
                }

                for (Dette d : p.getListeDette()) {
                    outDette += d.getID() + ";" + d.getSommeDette() + ";" + d.getSommeDejaPayer() + ";" + p.getId() + ";" + d.getDepense().getID() + "\n";
                }
            }

            String[] tab_outPersonne = outPersonne.split(System.getProperty("line.separator"));
            String[] tab_outDepense = outDepense.split(System.getProperty("line.separator"));
            String[] tab_outDette = outDette.split(System.getProperty("line.separator"));

            File fileP = new File(Util.workingDirectory + "/personne.txt");
            File fileDet = new File(Util.workingDirectory + "/dette.txt");
            File fileDep = new File(Util.workingDirectory + "/depense.txt");

            Util.SaveToFile(fileP, tab_outPersonne);
            Util.SaveToFile(fileDet, tab_outDette);
            Util.SaveToFile(fileDep, tab_outDepense);

        }
    }

    public static ArrayList<Personne> Charger() {

        ArrayList<Personne> listePersonne = new ArrayList<>();

        File fileP = new File(Util.workingDirectory + "/personne.txt");
        File fileDet = new File(Util.workingDirectory + "/dette.txt");
        File fileDep = new File(Util.workingDirectory + "/depense.txt");

        String[] txtListePersonne = Util.LoadFromFile(fileP);

        for (String s : txtListePersonne) {
            String[] sub_s = s.split(";");
            Personne p = new Personne(Integer.parseInt(sub_s[0]), sub_s[1]);
            p.setDetteTotale(Double.parseDouble(sub_s[2]));

            listePersonne.add(p);
        }

        String[] txtListDepense = Util.LoadFromFile(fileDep);

        Util.nbDepense = Integer.parseInt(txtListDepense[0]);

        for (int i = 1; i < txtListDepense.length; i++) {
            String[] sub_s = txtListDepense[i].split(";");
            for (Personne p : listePersonne) {
                if (p.getId() == Integer.parseInt(sub_s[4])) {

                    Depense d = new Depense(Integer.parseInt(sub_s[0]), Double.parseDouble(sub_s[1]), p, sub_s[2], sub_s[3]);
                    p.getListeDepense().add(d);
                }
            }
        }

        String[] txtListeDette = Util.LoadFromFile(fileDet);

        Util.nbDette = Integer.parseInt(txtListeDette[0]);

        for (int i = 1; i < txtListeDette.length; i++) {
            String sub_s[] = txtListeDette[i].split(";");
            int ID = Integer.parseInt(sub_s[0]);
            double sommeDette = Double.parseDouble(sub_s[1]);
            double sommeDejaPaye = Double.parseDouble(sub_s[2]);
            int idCreancier = Integer.parseInt(sub_s[3]);
            int idDepense = Integer.parseInt(sub_s[4]);
            Personne creancier = null;
            Depense depense = null;

            for (Personne p : listePersonne) {
                if (idCreancier == p.getId()) {
                    creancier = p;
                } else {
                    for (Depense d : p.getListeDepense()) {
                        if (d.getID() == idDepense) {
                            depense = d;
                        }
                    }
                }
            }

            Dette d = new Dette(ID, sommeDette, creancier, depense);
            d.setSommeDejaPayer(sommeDejaPaye);

            creancier.getListeDette().add(d);
        }
        return listePersonne;
    }


    public static void SaveToFile(File file, String[] data) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                for (int i = 0; i < data.length; i++) {
                    fos.write(data[i].getBytes());
                    if (i < data.length - 1) {
                        fos.write("\n".getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String[] LoadFromFile(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String test;
        int anzahl = 0;
        try {
            while ((test = br.readLine()) != null) {
                anzahl++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fis.getChannel().position(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try {
            while ((line = br.readLine()) != null) {
                array[i] = line;
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    public static int Aleatoir(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
