package com.example.jonathanguerne.themoneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by jonathan.guerne on 19.08.2016.
 */
public class NewDepenseActivity extends AppCompatActivity {

    ArrayList<Personne> listePersonne;
    ArrayList<Personne> listePotentielCreancier;

    ArrayList<Integer> listIDCreancier;

    Personne user;

    private String blockCharacterSet = ";";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depense);

        TextView titreNewDepense = (TextView) findViewById(R.id.ang_txtTitre);
        Button btnValider = (Button) findViewById(R.id.dep_btnValider);
        final EditText etDescription = (EditText) findViewById(R.id.dep_etDescription);
        final EditText etSommeDepense = (EditText) findViewById(R.id.dep_etSommeDepense);
        final DatePicker dpDate = (DatePicker) findViewById(R.id.dep_dpDate);

        etDescription.setFilters(new InputFilter[] { filter });

        listePotentielCreancier = new ArrayList<>();
        listIDCreancier = new ArrayList<>();

        Intent i = getIntent();

        listePersonne = (ArrayList<Personne>) getIntent().getSerializableExtra("ListePersonne");
        user = (Personne) listePersonne.get(i.getIntExtra("UserID", -1));

        titreNewDepense.setText("Nouvelle dépense de " + user.getName());

        for(Personne p:listePersonne){
            if(p.getId()!=user.getId()){
                listePotentielCreancier.add(p);
                listIDCreancier.add(p.getId());
            }
        }

        populateListView();

        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double sommeDepense = Double.parseDouble(etSommeDepense.getText().toString());

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                GregorianCalendar calendarBeg=new GregorianCalendar(dpDate.getYear(),
                        dpDate.getMonth(),dpDate.getDayOfMonth());
                String txtDate = sdf.format(calendarBeg.getTime());


                String description = etDescription.getText().toString();
                if(description.contains("\n")){
                    return;
                }

                Depense d= new Depense(Util.nbDepense,sommeDepense,user,etDescription.getText().toString(),txtDate);
                Util.nbDepense++;

                user.getListeDepense().add(d);

                double sommeDette = sommeDepense/(listIDCreancier.size()+1);
                double sommeARembourser;

                for(Personne p:listePotentielCreancier) {
                    sommeARembourser = sommeDette;

                    if (listIDCreancier.contains(p.getId())) {

                        for(Dette d2:user.getListeDette()){
                            if(d2.getDepense().getDebiteur().getId()==p.getId()){
                                if(sommeARembourser>d2.getSommeDette()-d2.getSommeDejaPayer()){
                                    sommeARembourser-=(d2.getSommeDette()-d2.getSommeDejaPayer());
                                    d2.setSommeDejaPayer(d2.getSommeDette());
                                }
                                else{
                                    d2.setSommeDejaPayer(d2.getSommeDejaPayer()+sommeARembourser);
                                    sommeARembourser=0;
                                }
                            }
                        }

                        Dette dette = new Dette(Util.nbDette,sommeDette,p,d);
                        Util.nbDette++;
                        dette.setSommeDejaPayer(sommeDette-sommeARembourser);
                        p.getListeDette().add(dette);
                        p.setDetteTotale(p.getDetteTotale()+sommeARembourser);
                    }
                }

                double usr_sommeDetteTot=0;

                for(Dette dette:user.getListeDette()){
                    usr_sommeDetteTot+=dette.getSommeDette()-dette.getSommeDejaPayer();
                }

                user.setDetteTotale(usr_sommeDetteTot);

                Intent intent = new Intent();
                intent.putExtra("ListePersonne", listePersonne);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("ListePersonne", listePersonne);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void populateListView() {
        ArrayAdapter<Personne> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.dep_lvListeCreancier);
        list.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private class MyListAdapter extends ArrayAdapter<Personne> {
        public MyListAdapter() {
            super(NewDepenseActivity.this, R.layout.item_newdep_creancier, listePotentielCreancier);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_newdep_creancier, parent, false);
            }

            final Personne p = listePotentielCreancier.get(position);

            TextView txtNom = (TextView) itemView.findViewById(R.id.inc_txtName);
            txtNom.setText(p.getName());

            final CheckBox cbIsCreancier = (CheckBox) itemView.findViewById(R.id.inc_cbIsCreancier);
            cbIsCreancier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!cbIsCreancier.isChecked()){
                        int tempID = p.getId();
                        for(int i=0;i<listIDCreancier.size();i++){
                            if(listIDCreancier.get(i)==tempID){
                                listIDCreancier.remove(i);
                            }
                        }
                    }
                    else{
                        listIDCreancier.add(p.getId());
                    }
                }
            });

            return itemView;
        }
    }
}
