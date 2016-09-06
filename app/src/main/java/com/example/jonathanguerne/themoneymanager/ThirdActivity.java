package com.example.jonathanguerne.themoneymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jonathan.guerne on 14.08.2016.
 */
public class ThirdActivity extends AppCompatActivity {
    private ArrayList<Personne> listePersonne;
    private Personne creancier;
    private Personne debiteur;

    private ArrayList<Dette> listeDettePersonne;

    private TextView txtDetteTot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);


        TextView txtDettePersonne = (TextView) findViewById(R.id.lsdep_txtTitreListeDepense);
        txtDetteTot = (TextView) findViewById(R.id.txtDettePersonneTotale);
        Button btnRembourser = (Button) findViewById(R.id.btnRembourser);

        Intent i = getIntent();

        listePersonne = (ArrayList<Personne>) getIntent().getSerializableExtra("ListePersonne");
        creancier = (Personne) listePersonne.get(i.getIntExtra("CreancierID", -1));
        debiteur = (Personne) listePersonne.get(i.getIntExtra("DebiteurID", -1));

        txtDettePersonne.setText("Dette de " + creancier.getName() + " envers " + debiteur.getName());
        btnRembourser.setText("Rembourser "+debiteur.getName());



        btnRembourser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RembourserActivity.class);
                i.putExtra("ListePersonne", listePersonne);
                i.putExtra("CreancierID",creancier.getId());
                i.putExtra("DebiteurID", debiteur.getId());
                startActivityForResult(i,1);
            }
        });


        txtDetteTot.setText(String.format("%.2f",getSommeDette()));
        populateListView();
    }

    double getSommeDette(){
        double sommeTotDette = 0;
        listeDettePersonne = new ArrayList<>();

        for (Dette d : creancier.getListeDette()) {
            if (d.getDepense().getDebiteur().getId() == debiteur.getId()) {
                listeDettePersonne.add(d);
                sommeTotDette += (d.getSommeDette() - d.getSommeDejaPayer());
            }
        }
        return sommeTotDette;
    }

    private void populateListView() {
        ArrayAdapter<Dette> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.lvDettePersonne);
        list.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        //ENVOYER LES INFOS VERS L'ACTIVITY PRINCIPALE UNE FOIS LE BOUTON CLIQUé (http://stackoverflow.com/questions/14292398/how-to-pass-data-from-2nd-activity-to-1st-activity-when-pressed-back-android)
        Intent intent = new Intent();
        intent.putExtra("ListePersonne", listePersonne);
        setResult(RESULT_OK, intent);
        finish();
        // Toast.makeText(getApplicationContext(),"NO !",Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            listePersonne = (ArrayList<Personne>) data.getSerializableExtra("ListePersonne");
            creancier = (Personne) listePersonne.get(data.getIntExtra("CreancierID", -1));
            debiteur = (Personne) listePersonne.get(data.getIntExtra("DebiteurID", -1));
            txtDetteTot.setText(String.format("%.2f",getSommeDette()));
            Util.Sauvegarder(listePersonne);
            populateListView();
        }
    }

    private class MyListAdapter extends ArrayAdapter<Dette> {

        public MyListAdapter() {
            super(ThirdActivity.this, R.layout.item_element3, listeDettePersonne);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_element3, parent, false);
            }

            Dette d = listeDettePersonne.get(position);

            TextView txtDesc = (TextView) itemView.findViewById(R.id.ild_txtDescription);
            txtDesc.setText(d.getDepense().getDescription());

            TextView txtDate = (TextView) itemView.findViewById(R.id.ild_txtDate);
            txtDate.setText(d.getDepense().getDate());

            TextView txtDetteTot = (TextView) itemView.findViewById(R.id.item3_txtDetteTot);
            txtDetteTot.setText(String.format("%.2f",d.getSommeDette()));

            TextView txtDetteDejaPaye = (TextView) itemView.findViewById(R.id.ild_txtSommeDepense);
            txtDetteDejaPaye.setText(String.format("%.2f",d.getSommeDejaPayer()));

            return itemView;
        }

    }
}
