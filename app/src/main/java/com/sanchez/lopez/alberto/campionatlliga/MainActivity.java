package com.sanchez.lopez.alberto.campionatlliga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.sanchez.lopez.alberto.campionatlliga.model.Entero;
import com.sanchez.lopez.alberto.campionatlliga.model.Equip;
import com.sanchez.lopez.alberto.campionatlliga.model.Jornada;
import com.sanchez.lopez.alberto.campionatlliga.model.Jugador;
import com.sanchez.lopez.alberto.campionatlliga.model.Partido;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "CampionatLLiga.settings";

    private RealmConfiguration realmConfig;
    private Realm realm;
    private Random rand;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rand = new Random();
        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean b = settings.getBoolean("generarDatosIniciales", true);
        if (b) {
            generarDatosIniciales();

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("generarDatosIniciales", false);

            editor.commit();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("generarDatosIniciales", false);

        editor.commit();
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

    public void openAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }


    private void generarDatosIniciales() {
        ArrayList<Equip> equips = new ArrayList<>();


        // Nom, ciutat, escut
        equips.add(new Equip("Los Pikachus", "Ciudad Trueno", String.valueOf(R.mipmap.medalla_trueno)));
        equips.add(new Equip("Team Rocket", "Ciudad Espacio", String.valueOf(R.mipmap.medalla_adoquin)));
        equips.add(new Equip("Gimnasio Agua", "Ciudad Azulona", String.valueOf(R.mipmap.medalla_arcoiris)));
        equips.add(new Equip("Gimnnasio Bosque", "Ciudad Vetusta", String.valueOf(R.mipmap.medalla_bosque)));
        equips.add(new Equip("Gimnnasio Fuego", "Ciudad Lavacalda", String.valueOf(R.mipmap.medalla_calor)));
        equips.add(new Equip("Gimnnasio Carambano", "Ciudad de Puntaneva", String.valueOf(R.mipmap.medalla_carambano)));
        equips.add(new Equip("Gimmnasio Cienaga", "Ciudad Pradera", String.valueOf(R.mipmap.medalla_cienaga)));
        equips.add(new Equip("Gimmnasio Faro", "Ciudad Marina", String.valueOf(R.mipmap.medalla_faro)));
        equips.add(new Equip("Gimmnasio Piedra", "Ciudad Ferrica", String.valueOf(R.mipmap.medalla_piedra)));
        equips.add(new Equip("Gimmnasio Tierra", "Ciudad Verde", String.valueOf(R.mipmap.medalla_tierra)));

        for (Equip e : equips) {
            for (int i = 0; i < 5; ++i) {
                Jugador j = new Jugador(generateName(),0,e);
                e.getTitulars().set(i,j);
            }

            for (int i = 0; i < 7; ++i) {
                Jugador j = new Jugador(generateName(),0,e);
                e.getReservas().set(i,j);
            }

        }

        Calendar cal = Calendar.getInstance();

        Jornada j1 = new Jornada();
        j1.setNumJornada(1);
        cal.set(2016, 05, 8);
        Date date = new Date(cal.getTimeInMillis());
        j1.setData(date);



        ArrayList<Partido> partidosA = new ArrayList<>(5);
        partidosA.add(new Partido(date, equips.get(0), equips.get(1), 1, 2));
        partidosA.add(new Partido(date, equips.get(1), equips.get(2), 1, 2));
        partidosA.add(new Partido(date, equips.get(2), equips.get(3), 1, 2));
        partidosA.add(new Partido(date, equips.get(3), equips.get(4), 1, 2));
        partidosA.add(new Partido(date, equips.get(4), equips.get(5), 1, 2));

        equips.get(1).guanya();
        equips.get(2).guanya();
        equips.get(3).guanya();
        equips.get(4).guanya();
        equips.get(5).guanya();

        equips.get(0).perd();
        equips.get(1).perd();
        equips.get(2).perd();
        equips.get(3).perd();
        equips.get(4).perd();

        for (Partido p : partidosA) {
            Entero golsTA = new Entero();
            golsTA.value = 1;

            Entero golsTB = new Entero();
            golsTB.value = 2;

            p.getEquipA().getTitulars().get(0).addGols(golsTA.value);
            p.getGolsTitularsA().set(0,golsTA);
            p.getEquipB().getTitulars().get(0).addGols(golsTB.value);
            p.getGolsTitularsB().set(0,golsTB);
        }


        j1.getPartidos().add(partidosA.get(0));
        j1.getPartidos().add(partidosA.get(1));
        j1.getPartidos().add(partidosA.get(2));
        j1.getPartidos().add(partidosA.get(3));
        j1.getPartidos().add(partidosA.get(4));

        Jornada j2 = new Jornada();
        j2.setNumJornada(2);
        cal.set(2016, 05, 15);
        Date date2 = new Date(cal.getTimeInMillis());
        j2.setData(date2);

        ArrayList<Partido> partidosB = new ArrayList<>(5);

        partidosB.add(new Partido(date2, equips.get(8), equips.get(6), 2, 1));
        partidosB.add(new Partido(date2, equips.get(6), equips.get(7), 2, 1));
        partidosB.add(new Partido(date2, equips.get(7), equips.get(8), 2, 1));
        partidosB.add(new Partido(date2, equips.get(8), equips.get(9), 2, 1));
        partidosB.add(new Partido(date2, equips.get(9), equips.get(6), 2, 1));

        equips.get(6).guanya();
        equips.get(7).guanya();
        equips.get(8).guanya();
        equips.get(9).guanya();
        equips.get(6).guanya();

        equips.get(8).perd();
        equips.get(6).perd();
        equips.get(7).perd();
        equips.get(8).perd();
        equips.get(9).perd();

        for (Partido p : partidosB) {
            Entero golsTA = new Entero();
            golsTA.value = 1;

            Entero golsTB = new Entero();
            golsTB.value = 2;

            p.getEquipA().getTitulars().get(0).addGols(golsTA.value);
            p.getGolsTitularsA().set(0,golsTA);
            p.getEquipB().getTitulars().get(0).addGols(golsTB.value);
            p.getGolsTitularsB().set(0,golsTB);
        }

        j2.getPartidos().add(partidosB.get(0));
        j2.getPartidos().add(partidosB.get(1));
        j2.getPartidos().add(partidosB.get(2));
        j2.getPartidos().add(partidosB.get(3));
        j2.getPartidos().add(partidosB.get(4));

        realm.beginTransaction();
        realm.copyToRealm(j1);
        realm.copyToRealm(j2);
        realm.commitTransaction();
    }

    private String generateName() {
        StringBuilder sb = new StringBuilder();

        final char[] vocals = {'A','E','I','O','U'};
        final char[] consonants = {'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L' ,'M' ,'N' ,'P' ,'Q' ,'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'};

        int length = 4 + rand.nextInt(6);

        for (int i = 0; i < length; i+=2) {
            sb.append(consonants[rand.nextInt(21)]);
            sb.append(vocals[rand.nextInt(5)]);
        }

        return sb.toString();
    }
}
