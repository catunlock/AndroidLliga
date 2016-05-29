package com.sanchez.lopez.alberto.campionatlliga.visualizadoras;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sanchez.lopez.alberto.campionatlliga.AfegirEquip;
import com.sanchez.lopez.alberto.campionatlliga.R;
import com.sanchez.lopez.alberto.campionatlliga.model.Equip;
import com.sanchez.lopez.alberto.campionatlliga.model.Jugador;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class EquipViewer extends AppCompatActivity {

    private Equip equip;

    private TextView lblNomEquip;
    private TextView lblNomCiutat;
    private ImageView imgEscut;
    private ListView listJugadors;

    private RealmConfiguration realmConfig;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        lblNomEquip = (TextView) findViewById(R.id.lblNomEquip);
        lblNomCiutat = (TextView) findViewById(R.id.lblNomCiutat);
        imgEscut = (ImageView) findViewById(R.id.imgEscut);
        listJugadors = (ListView) findViewById(R.id.listJugadors);

        Intent intent = getIntent();
        String nomEquip = intent.getStringExtra("nomEquip");

        Log.println(Log.DEBUG, "[EQUIP]", nomEquip);
        equip = realm.where(Equip.class).equalTo("nom", nomEquip).findFirst();


        lblNomEquip.setText(equip.getNom());
        lblNomCiutat.setText(equip.getCiutat());
        int idResourceEscut = Integer.valueOf(equip.getPathEscut());
        imgEscut.setImageResource(idResourceEscut);
        imgEscut.setTag(idResourceEscut);

        listJugadors.setAdapter(new MySimpleArrayAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_equip_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, AfegirEquip.class);
            intent.putExtra("nomEquip", equip.getNom());
            startActivity(intent);
        }

        if (id == R.id.action_delete) {

            new AlertDialog.Builder(this)
                    .setTitle("Esborrar equip")
                    .setMessage("Esta segur que vol esborrar l'equip \""+equip.getNom()+"\"?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Equip e = realm.where(Equip.class).equalTo("nom",equip.getNom()).equalTo("ciutat",equip.getCiutat()).equalTo("pathEscut",equip.getPathEscut()).findFirst();
                            realm.beginTransaction();
                            e.setEliminat(true);
                            realm.commitTransaction();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private class MySimpleArrayAdapter extends ArrayAdapter<Jugador> {
        private final Context context;

        public MySimpleArrayAdapter(Context context) {
            super(context, -1);

            this.addAll(equip.getTitulars());
            this.addAll(equip.getReservas());
            this.context = context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.listview_equip_jugadors, parent, false);
            TextView lblTipus = (TextView) rowView.findViewById(R.id.lblTipus);
            TextView lblNomJugador = (TextView) rowView.findViewById(R.id.lblNomJugador);
            TextView lblGols = (TextView) rowView.findViewById(R.id.lblGols);

            Jugador j = getItem(position);

            if (position < 5) {
                lblTipus.setText("Titular:");
            }else {
                lblTipus.setText("Reserva:");
            }

            lblNomJugador.setText(j.getNom());
            lblGols.setText(String.valueOf(j.getGols()));

            return rowView;
        }
    }

}
