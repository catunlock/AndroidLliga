package com.sanchez.lopez.alberto.campionatlliga;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sanchez.lopez.alberto.campionatlliga.model.Equip;
import com.sanchez.lopez.alberto.campionatlliga.model.Jugador;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AfegirEquip extends AppCompatActivity {

    public static final int ESCUT_REQUEST = 1234;

    EditText txtNomEquip;
    EditText txtNomCiutat;
    ImageButton ibtnEscut;
    Context context;

    ArrayList<EditText> txtNomTitulars = new ArrayList<>();
    ArrayList<EditText> txtNomReservas = new ArrayList<>();

    Equip equip;
    Toast toastEquipExisteix;

    private RealmConfiguration realmConfig;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegir_equip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toastEquipExisteix = Toast.makeText(this,"Ja existeix un equip amb aquest nom.", Toast.LENGTH_LONG);
        context = this;
        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        txtNomEquip = (EditText) findViewById(R.id.lblNomJugador);
        txtNomCiutat = (EditText) findViewById(R.id.txtNomCiutat);
        ibtnEscut = (ImageButton) findViewById(R.id.ibtnEscut);

        txtNomTitulars = new ArrayList<>(5);
        txtNomTitulars.add((EditText) findViewById(R.id.txtTitular1));
        txtNomTitulars.add((EditText) findViewById(R.id.txtTitular2));
        txtNomTitulars.add((EditText) findViewById(R.id.txtTitular3));
        txtNomTitulars.add((EditText) findViewById(R.id.txtTitular4));
        txtNomTitulars.add((EditText) findViewById(R.id.txtTitular5));

        txtNomReservas = new ArrayList<>(7);
        txtNomReservas.add((EditText) findViewById(R.id.txtReserva1));
        txtNomReservas.add((EditText) findViewById(R.id.txtReserva2));
        txtNomReservas.add((EditText) findViewById(R.id.txtReserva3));
        txtNomReservas.add((EditText) findViewById(R.id.txtReserva4));
        txtNomReservas.add((EditText) findViewById(R.id.txtReserva5));
        txtNomReservas.add((EditText) findViewById(R.id.txtReserva6));
        txtNomReservas.add((EditText) findViewById(R.id.txtReserva7));

        ibtnEscut.setImageResource(R.mipmap.ic_shield);
        ibtnEscut.setTag(R.mipmap.ic_shield);

        ibtnEscut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, GalleryActivity.class);
                startActivityForResult(i, ESCUT_REQUEST);
            }
        });

        Intent intent = getIntent();
        String nomEquip = intent.getStringExtra("nomEquip");

        if (nomEquip != null) {
            loadData(nomEquip);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (ESCUT_REQUEST) : {
                if (resultCode == Activity.RESULT_OK) {
                    int resourceId = data.getIntExtra("imgSelected", -1);
                    if (resourceId != -1) {
                        ibtnEscut.setImageResource(resourceId);
                        ibtnEscut.setTag(resourceId);
                    }
                }
                break;
            }
        }
    }

    private void loadData(String nomEquip) {
        Log.println(Log.DEBUG, "[EQUIP]", "Cargando equipo: " + nomEquip);
        equip = realm.where(Equip.class).equalTo("nom", nomEquip).findFirst();

        txtNomEquip.setText(equip.getNom());
        txtNomCiutat.setText(equip.getCiutat());

        int idResourceEscut = Integer.valueOf(equip.getPathEscut());
        ibtnEscut.setImageResource(idResourceEscut);
        ibtnEscut.setTag(idResourceEscut);

        List<Jugador> titulars = equip.getTitulars();
        List<Jugador> reservas = equip.getReservas();

        for (int i = 0; i < txtNomTitulars.size(); ++i) {
            txtNomTitulars.get(i).setText(titulars.get(i).getNom());
        }

        for (int i = 0; i < txtNomReservas.size(); ++i) {
            txtNomReservas.get(i).setText(reservas.get(i).getNom());
        }

        String nomEquipo = equip.getNom();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_afegir_equip, menu);
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
            intent.putExtra("ajudaId", R.raw.afegir_equip);
            startActivity(intent);
        }

        if (id == R.id.action_save) {

            String nomEquip = txtNomEquip.getText().toString();
            if (comprobarExisteixEquip(nomEquip)) {
                toastEquipExisteix.show();
                txtNomEquip.setTextColor(Color.RED);
                return true;
            }

            realm.beginTransaction();
            String nomCiutat = txtNomCiutat.getText().toString();
            String tagEscut = ibtnEscut.getTag().toString();
            if (equip == null) {
                equip = new Equip();
            }

            equip.setPathEscut(tagEscut);
            equip.setNom(nomEquip);
            equip.setCiutat(nomCiutat);


            List<Jugador> titulars = equip.getTitulars();
            List<Jugador> reservas = equip.getReservas();

            for (int i = 0; i < txtNomTitulars.size(); ++i) {
                EditText control = txtNomTitulars.get(i);
                String nom = control.getText().toString();
                titulars.get(i).setNom(nom);
            }

            for (int i = 0; i < txtNomReservas.size(); ++i) {
                String nom = txtNomReservas.get(i).getText().toString();
                reservas.get(i).setNom(nom);
            }

            realm.copyToRealm(equip);
            realm.commitTransaction();

            /*
            Intent intent = new Intent(this, EquipViewer.class);
            intent.putExtra("nomEquip", equip.getNom());

            startActivity(intent);
            */
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean comprobarExisteixEquip(String nomEquip) {

        Equip e = realm.where(Equip.class).equalTo("nom", nomEquip).findFirst();

        return e != null;
    }
}
