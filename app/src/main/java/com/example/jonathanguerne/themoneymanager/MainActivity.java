package com.example.jonathanguerne.themoneymanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Personne> listePersonne;
    Button btnSauvegarder;
    Button btnReset;
    //SAUVGARDER LES INFOS https://www.youtube.com/watch?v=x3pyyQbwLko

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnReset = (Button) findViewById(R.id.btnReset);

        File dir = new File(Util.path);
        dir.mkdirs();

        loadPreferance();
        registerClickCallback();
    }

    private void loadPreferance() {

        Util.pathPref=Util.path+"/preferance.txt";
        File filePref = new File(Util.pathPref);
        if(filePref.exists()){
            String[] pref=Util.LoadFromFile(filePref);
            Util.workingDirectory=Util.path+"/"+pref[0];
            listePersonne=Util.Charger();
            populateListView();

        }
        else{
            try {
                filePref.createNewFile();
                Intent i = new Intent(getApplicationContext(), GroupeActivity.class);
                startActivityForResult(i, 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeGroupeAction(View view){
        Intent i = new Intent(getApplicationContext(), GroupeActivity.class);
        startActivityForResult(i,2);
    }


    public void actionReset(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Attention");
        builder.setMessage("Voulez-vous vraiment remettre à zéro toutes les données de ce groupe ?");

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                for(Personne p:listePersonne){
                    p.setDetteTotale(0);
                    p.setListeDepense(new ArrayList<Depense>());
                    p.setListeDette(new ArrayList<Dette>());
                }

                Util.Sauvegarder(listePersonne);
                populateListView();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void populateListView() {
        ArrayAdapter<Personne> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.lvPersonnes);
        list.setAdapter(adapter);
    }


    private void registerClickCallback() {
        ListView liste = (ListView) findViewById(R.id.lvPersonnes);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Personne clickedPersonne = listePersonne.get(position);

                Intent i = new Intent(getApplicationContext(), SecondActivity.class);
                i.putExtra("ListePersonne", listePersonne);
                i.putExtra("UserID", clickedPersonne.getId());

                Log.e("n", clickedPersonne.getName());

                startActivityForResult(i, 1);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            listePersonne = (ArrayList<Personne>) data.getSerializableExtra("ListePersonne");
            Util.Sauvegarder(listePersonne);
            populateListView();
        }
        else if(requestCode==2){
            listePersonne=Util.Charger();
            Util.Sauvegarder(listePersonne);
            populateListView();
        }
    }

    private class MyListAdapter extends ArrayAdapter<Personne> {
        public MyListAdapter() {
            super(MainActivity.this, R.layout.item_element, listePersonne);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_element, parent, false);
            }

            Personne actPeronne = listePersonne.get(position);

            TextView txtNom = (TextView) itemView.findViewById(R.id.inc_txtName);
            txtNom.setText(actPeronne.getName());

            TextView txtDette = (TextView) itemView.findViewById(R.id.item1_txtDette);
            //String.format("%.2f",actPeronne.getDetteTotale());
            txtDette.setText(String.format("%.2f", actPeronne.getDetteTotale()));

            return itemView;
        }
    }
}
