package com.sanchez.lopez.alberto.campionatlliga;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sanchez.lopez.alberto.campionatlliga.model.Equip;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;

public class Classificacio extends AppCompatActivity {

    ListView listClassificacio;

    private RealmConfiguration realmConfig;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classificacio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listClassificacio = (ListView) findViewById(R.id.listClasificacio);

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this);

        listClassificacio.setAdapter(adapter);
    }

    private class MySimpleArrayAdapter extends ArrayAdapter<Equip> {
        private final Context context;
        private final List<Equip> equips;

        public MySimpleArrayAdapter(Context context) {
            super(context, -1);
            equips = realm.where(Equip.class).findAll().sort("punts", Sort.DESCENDING);
            this.addAll(equips);
            this.context = context;

        }

        private int getResourceId(String pVariableName, String pResourcename, String pPackageName)
        {
            try {
                return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.listview_clasificacio, parent, false);
            TextView lblPunts = (TextView) rowView.findViewById(R.id.lblPunts);
            TextView lblNomEquip = (TextView) rowView.findViewById(R.id.lblNomJugador);
            TextView lblCiutatEquip = (TextView) rowView.findViewById(R.id.lblCiutatEquip);
            TextView lblWins = (TextView) rowView.findViewById(R.id.lblWins);
            TextView lblLoses = (TextView) rowView.findViewById(R.id.lblLoses);
            TextView lblEmpates = (TextView) rowView.findViewById(R.id.lblEmpates);
            TextView lblEstat = (TextView) rowView.findViewById(R.id.lblEstat);
            ImageView imgEscut = (ImageView) rowView.findViewById(R.id.imgEscut);

            Equip e = equips.get(position);

            lblPunts.setText(String.valueOf(e.getPunts()));
            lblNomEquip.setText(e.getNom());
            lblCiutatEquip.setText(e.getCiutat());
            lblWins.setText(String.valueOf(e.getGuanyats()));
            lblLoses.setText(String.valueOf(e.getPerduts()));
            lblEmpates.setText(String.valueOf(e.getEmpatats()));

            if(e.isEliminat()) {
                lblEstat.setText("Eliminat");
                lblEstat.setTextColor(Color.RED);
            }

            int idResourceEscut = Integer.valueOf(e.getPathEscut());
            imgEscut.setImageResource(idResourceEscut);
            imgEscut.setTag(idResourceEscut);

            return rowView;
        }
    }
}
