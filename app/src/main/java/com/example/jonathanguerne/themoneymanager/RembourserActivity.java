package com.example.jonathanguerne.themoneymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jonathan.guerne on 18.08.2016.
 */
public class RembourserActivity extends AppCompatActivity {

    private ArrayList<Personne> listePersonne;
    private Personne creancier;
    private Personne debiteur;
    private double sommeDette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rembourser);

        TextView textRemboursement = (TextView) findViewById(R.id.remb_txtRemboursement);
        TextView texteSommeDette = (TextView) findViewById(R.id.remb_txtDetteTot);
        final EditText etSommeRembourser = (EditText) findViewById(R.id.remb_etSommeRembourser);
        Button boutonRembourser = (Button) findViewById(R.id.remb_btnRembourser);


        Intent i = getIntent();

        listePersonne = (ArrayList<Personne>) getIntent().getSerializableExtra("ListePersonne");
        creancier = (Personne) listePersonne.get(i.getIntExtra("CreancierID", -1));
        debiteur = (Personne) listePersonne.get(i.getIntExtra("DebiteurID", -1));

        sommeDette = getSommeDette();

        textRemboursement.setText(creancier.getName() + " rembourse " + debiteur.getName());
        texteSommeDette.setText(String.format("%.2f",sommeDette));


        boutonRembourser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valeurTexte = etSommeRembourser.getText().toString();
                double sommeRemboursee = Double.parseDouble(valeurTexte);

                for (Dette d : creancier.getListeDette()) {
                    if (d.getDepense().getDebiteur().getId() == debiteur.getId()) {
                        if (sommeRemboursee > d.getSommeDette() - d.getSommeDejaPayer()) {
                            creancier.setDetteTotale(creancier.getDetteTotale() - (d.getSommeDette() - d.getSommeDejaPayer()));
                            sommeRemboursee -= d.getSommeDette() - d.getSommeDejaPayer();
                            d.setSommeDejaPayer(d.getSommeDette());
                        } else {
                            d.setSommeDejaPayer(d.getSommeDejaPayer() + sommeRemboursee);
                            creancier.setDetteTotale(creancier.getDetteTotale() - sommeRemboursee);
                            sommeRemboursee = 0;
                        }
                    }
                }

                if (sommeRemboursee > 0) {


                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String currentDate = sdf.format(new Date());

                    Depense d = new Depense(Util.nbDepense, sommeRemboursee, creancier, "Reste remboursement (" + creancier.getName() + " -> " + debiteur.getName() + ")", currentDate);
                    Util.nbDepense++;
                    creancier.getListeDepense().add(d);
                    debiteur.getListeDette().add(new Dette(Util.nbDette, sommeRemboursee, debiteur, d));
                    debiteur.setDetteTotale(debiteur.getDetteTotale()+sommeRemboursee);
                    Util.nbDette++;
                }

                Intent intent = new Intent();
                intent.putExtra("ListePersonne", listePersonne);
                intent.putExtra("CreancierID", creancier.getId());
                intent.putExtra("DebiteurID", debiteur.getId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    double getSommeDette() {
        double sommeTotDette = 0;
        ArrayList<Dette> listeDettePersonne = new ArrayList<>();

        for (Dette d : creancier.getListeDette()) {
            if (d.getDepense().getDebiteur().getId() == debiteur.getId()) {
                listeDettePersonne.add(d);
                sommeTotDette += (d.getSommeDette() - d.getSommeDejaPayer());
            }
        }
        return sommeTotDette;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("ListePersonne", listePersonne);
        intent.putExtra("CreancierID", creancier.getId());
        intent.putExtra("DebiteurID", debiteur.getId());
        setResult(RESULT_OK, intent);
        finish();
    }
}


