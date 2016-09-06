package com.example.jonathanguerne.themoneymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jonathan.guerne on 19.08.2016.
 */
public class ListeDepenseActivity extends AppCompatActivity {

    ArrayList<Personne> listePersonne;
    Personne user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listedepense);

        TextView txtTitrelisteDepense = (TextView) findViewById(R.id.lsdep_txtTitreListeDepense);
        TextView txtDepenseTot = (TextView) findViewById(R.id.lsdep_txtDepenseTot);
        ListView lvListeDepense = (ListView) findViewById(R.id.lsdep_lvlisteDepense);

        Intent i = getIntent();

        listePersonne = (ArrayList<Personne>) getIntent().getSerializableExtra("ListePersonne");
        user = (Personne) listePersonne.get(i.getIntExtra("UserID", -1));

        txtTitrelisteDepense.setText("Dépenses de "+user.getName());

        double sommeTot=0;
        for(Depense d:user.getListeDepense()){
            sommeTot+=d.getSommeDepense();
        }

        txtDepenseTot.setText(String.format("%.2f",sommeTot));

        populateListView();

    }

    private void populateListView() {
        ArrayAdapter<Depense> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.lsdep_lvlisteDepense);
        list.setAdapter(adapter);
    }


    private class MyListAdapter extends ArrayAdapter<Depense> {
        public MyListAdapter() {
            super(ListeDepenseActivity.this, R.layout.item_listedepense, user.getListeDepense());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_listedepense, parent, false);
            }

            Depense d = user.getListeDepense().get(position);

            TextView txtDescription = (TextView) itemView.findViewById(R.id.ild_txtDescription);
            txtDescription.setText(d.getDescription());

            TextView txtDate = (TextView) itemView.findViewById(R.id.ild_txtDate);
            txtDate.setText(d.getDate());

            TextView txtSommeDepense = (TextView) itemView.findViewById(R.id.ild_txtSommeDepense);
            txtSommeDepense.setText(String.format("%.2f",d.getSommeDepense()));

            return itemView;
        }

    }
}
