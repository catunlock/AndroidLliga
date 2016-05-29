package com.sanchez.lopez.alberto.campionatlliga;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.sanchez.lopez.alberto.campionatlliga.model.Jornada;

import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void openEquipsActivity(View view) {
        Intent intent = new Intent(this, Equips.class);
        startActivity(intent);
    }

    public void openAfegirPartitActivity(View view) {
        Intent intent = new Intent(this, AnadirPartido.class);
        startActivity(intent);
    }

    public void openAfegirJornadaActivity(View view) {
        Intent intent = new Intent(this, Jornadas.class);
        startActivity(intent);
    }

    public void openClassificacio(View view) {
        Intent intent = new Intent(this, Classificacio.class);
        startActivity(intent);
    }

    public void openJugadorsActivity(View view) {
        Intent intent = new Intent(this, Jugadors.class);
        startActivity(intent);
    }
}
