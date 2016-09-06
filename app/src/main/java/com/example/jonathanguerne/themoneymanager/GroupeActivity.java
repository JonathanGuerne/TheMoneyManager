package com.example.jonathanguerne.themoneymanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jonathan.guerne on 23.08.2016.
 */
public class GroupeActivity extends AppCompatActivity {

    ArrayList<String> listeGroupe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe);

        Button btnNewGroupe = (Button) findViewById(R.id.ga_btnNewGroupe);

        listeGroupe = new ArrayList<>();
        File f = new File(Util.path);
        File[] files = f.listFiles();
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                listeGroupe.add(inFile.getName());

            }
        }

        btnNewGroupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NewGroupeActivity.class);
                i.putExtra("ListeGroupe", listeGroupe);
                startActivityForResult(i, 1);
            }
        });

        populateListView();
        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView liste = (ListView) findViewById(R.id.ga_lvGroupes);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedGroupe = listeGroupe.get(position);

                Util.workingDirectory = Util.path + "/" + clickedGroupe;

                String[] data = {clickedGroupe};
                Util.SaveToFile(new File(Util.pathPref), data);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        liste.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final String clickedGroupe = listeGroupe.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(GroupeActivity.this);

                builder.setTitle("Attention");
                builder.setMessage("Voulez-vous supprimer ce groupe ?");

                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        File dirSupp = new File(Util.path + "/" + clickedGroupe);
                        Util.deleteRecursive(dirSupp);

                        for (int i = 0; i <= listeGroupe.size(); i++) {
                            if (listeGroupe.get(i) == clickedGroupe) {
                                listeGroupe.remove(i);
                                break;
                            }
                        }

                        if (!new File(Util.workingDirectory).exists()) {
                            Util.workingDirectory = "";
                        }

                        String pref[]=Util.LoadFromFile(new File(Util.pathPref));
                        if(pref[0].equals(clickedGroupe)){
                            new File(Util.pathPref).delete();
                        }

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

                return true;
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            listeGroupe.add((String) data.getSerializableExtra("Groupe"));
            populateListView();
        }
    }

    @Override
    public void onBackPressed() {
        //ENVOYER LES INFOS VERS L'ACTIVITY PRINCIPALE UNE FOIS LE BOUTON CLIQUé (http://stackoverflow.com/questions/14292398/how-to-pass-data-from-2nd-activity-to-1st-activity-when-pressed-back-android)
        if (Util.workingDirectory != "") {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        // Toast.makeText(getApplicationContext(),"NO !",Toast.LENGTH_SHORT).show();
    }

    private void populateListView() {
        ArrayAdapter<String> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.ga_lvGroupes);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        public MyListAdapter() {
            super(GroupeActivity.this, R.layout.item_groupe, listeGroupe);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_groupe, parent, false);
            }

            final String groupe = listeGroupe.get(position);

            TextView txtNom = (TextView) itemView.findViewById(R.id.iG_txtNomGroupe);
            txtNom.setText(groupe);


            return itemView;
        }
    }
}
