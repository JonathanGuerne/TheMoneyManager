package com.example.jonathanguerne.themoneymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jonathan.guerne on 13.08.2016.
 */
public class SecondActivity extends AppCompatActivity {
    private Personne user;
    private ArrayList<Personne> listePersonne;
    private ArrayList<DettePersonne> dettePersonne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView txtNom = (TextView) findViewById(R.id.lsdep_txtTitreListeDepense);
        Button btnListeDepense = (Button) findViewById(R.id.btnListeDepense);
        Button btnNewDepense = (Button) findViewById(R.id.btnNewDepense);

        Intent i = getIntent();

        listePersonne = (ArrayList<Personne>) getIntent().getSerializableExtra("ListePersonne");
        user = (Personne) listePersonne.get(i.getIntExtra("UserID", -1));

        txtNom.setText(user.getName());

        btnListeDepense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListeDepenseActivity.class);

                i.putExtra("ListePersonne", listePersonne);
                i.putExtra("UserID", user.getId());


                startActivity(i);
            }
        });

        btnNewDepense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NewDepenseActivity.class);

                i.putExtra("ListePersonne", listePersonne);
                i.putExtra("UserID", user.getId());


                startActivityForResult(i,2);
            }
        });

        getDPlist();
        populateListView();
        registerClickCallback();
    }

    public void getDPlist(){
        dettePersonne = new ArrayList<>();

        for (Personne p : listePersonne) {
            if (p.getId() != user.getId()) {
                dettePersonne.add(new DettePersonne(p, 0));
            }
        }

        for (DettePersonne dp : dettePersonne) {
            for (Dette d : user.getListeDette()) {
                if (dp.getDebiteur().getId() == d.getDepense().getDebiteur().getId()) {
                    dp.addDette(d.getSommeDette()-d.getSommeDejaPayer());
                }
            }
        }
    }

    private void populateListView() {
        ArrayAdapter<DettePersonne> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.lvPersonnes2);
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


    private void registerClickCallback() {
        ListView liste = (ListView) findViewById(R.id.lvPersonnes2);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DettePersonne dp = dettePersonne.get(position);

                Intent i = new Intent(getApplicationContext(), ThirdActivity.class);

                i.putExtra("ListePersonne", listePersonne);
                i.putExtra("CreancierID", user.getId());
                i.putExtra("DebiteurID", dp.getDebiteur().getId());


                startActivityForResult(i, 1);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            listePersonne = (ArrayList<Personne>) data.getSerializableExtra("ListePersonne");
            user= listePersonne.get(user.getId());
            getDPlist();
            Util.Sauvegarder(listePersonne);
            populateListView();
        }
        else if(requestCode == 2){
            listePersonne = (ArrayList<Personne>) data.getSerializableExtra("ListePersonne");
            user= listePersonne.get(user.getId());
            getDPlist();
            Util.Sauvegarder(listePersonne);
            populateListView();
        }
    }


    private class MyListAdapter extends ArrayAdapter<DettePersonne> {

        public MyListAdapter() {
            super(SecondActivity.this, R.layout.item_element2, dettePersonne);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_element2, parent, false);
            }

            DettePersonne dp = dettePersonne.get(position);

            TextView txtNom = (TextView) itemView.findViewById(R.id.item2_txtNom);
            txtNom.setText(dp.getDebiteur().getName());

            TextView txtDette = (TextView) itemView.findViewById(R.id.item2_txtDette);
            txtDette.setText(String.format("%.2f",dp.getSommeTotdette()));

            return itemView;
        }

    }
}
