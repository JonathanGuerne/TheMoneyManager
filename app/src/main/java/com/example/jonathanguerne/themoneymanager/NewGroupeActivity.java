package com.example.jonathanguerne.themoneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jonathan.guerne on 23.08.2016.
 */
public class NewGroupeActivity extends AppCompatActivity {

    ArrayList<String> listeGroupe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_groupe);

        Intent i = getIntent();

        listeGroupe = (ArrayList<String>) getIntent().getSerializableExtra("ListeGroupe");

        Button btnValider = (Button) findViewById(R.id.ang_btnValider);
        final EditText edNomGroupe = (EditText) findViewById(R.id.ang_etNomGroupe);
        final EditText edlistePersonne = (EditText) findViewById(R.id.ang_etListePeronnes);

        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = edNomGroupe.getText().toString();
                String txtListePersone = edlistePersonne.getText().toString();

                if (nom!=""&&txtListePersone!=""){
                    if(nom.contains("\n")||txtListePersone.contains("\n")){
                        //message alerte
                    }
                    else if(listeGroupe.contains(nom)){
                        //message alerte
                    }
                    else{
                        File newDir = new File(Util.path+"/"+nom);
                        newDir.mkdirs();

                        Util.workingDirectory=newDir.getPath();
                        String[] data={nom};
                        Util.SaveToFile(new File(Util.pathPref),data);

                        String[] tabListePersonne = txtListePersone.split(",");
                        ArrayList<Personne> listePeronnse = new ArrayList<Personne>();
                        for(int i=0;i<tabListePersonne.length;i++){
                           listePeronnse.add(new Personne(i,tabListePersonne[i]));
                        }

                        if(listePeronnse.size()>=2) {

                            Util.Sauvegarder(listePeronnse);

                            Intent intent = new Intent();
                            intent.putExtra("Groupe", nom);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        //ENVOYER LES INFOS VERS L'ACTIVITY PRINCIPALE UNE FOIS LE BOUTON CLIQUé (http://stackoverflow.com/questions/14292398/how-to-pass-data-from-2nd-activity-to-1st-activity-when-pressed-back-android)
        if(Util.workingDirectory!="") {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        // Toast.makeText(getApplicationContext(),"NO !",Toast.LENGTH_SHORT).show();
    }

}
