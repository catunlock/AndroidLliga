package com.sanchez.lopez.alberto.campionatlliga;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sanchez.lopez.alberto.campionatlliga.model.Equip;
import com.sanchez.lopez.alberto.campionatlliga.model.Jugador;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;

public class Jugadors extends AppCompatActivity {

    ListView listJugadors;

    private RealmConfiguration realmConfig;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listJugadors = (ListView) findViewById(R.id.listJugadors);

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this);

        listJugadors.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ajuda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ajuda) {
            Intent intent = new Intent(this, AjudaActivity.class);
            intent.putExtra("ajudaId", R.raw.jugadors);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class MySimpleArrayAdapter extends ArrayAdapter<Jugador> {
        private final Context context;
        private final List<Jugador> jugadors;

        public MySimpleArrayAdapter(Context context) {
            super(context, -1);
            jugadors = realm.where(Jugador.class).findAll().sort("gols", Sort.DESCENDING);
            this.addAll(jugadors);
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

            View rowView = inflater.inflate(R.layout.listview_jugadors, parent, false);
            TextView lblPosition = (TextView) rowView.findViewById(R.id.lblPosition);
            TextView lblNomJugador = (TextView) rowView.findViewById(R.id.lblNomJugador);
            TextView lblNomEquip = (TextView) rowView.findViewById(R.id.lblNomEquip);
            TextView lblGols = (TextView) rowView.findViewById(R.id.lblGols);

            Jugador j = jugadors.get(position);

            lblPosition.setText(String.valueOf(position+1));
            lblNomJugador.setText(j.getNom());
            lblNomEquip.setText(j.getEquip().getNom());
            lblGols.setText(String.valueOf(j.getGols()));

            if (j.getEquip().isEliminat()) {
                lblNomEquip.setTextColor(Color.RED);
            }

            return rowView;
        }
    }

}
