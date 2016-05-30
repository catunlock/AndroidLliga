package com.sanchez.lopez.alberto.campionatlliga;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sanchez.lopez.alberto.campionatlliga.dialogs.DatePickerFragment;
import com.sanchez.lopez.alberto.campionatlliga.model.Equip;
import com.sanchez.lopez.alberto.campionatlliga.model.Jornada;
import com.sanchez.lopez.alberto.campionatlliga.model.Partido;
import com.sanchez.lopez.alberto.campionatlliga.visualizadoras.JornadaActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AnadirPartido extends AppCompatActivity {

    private RealmConfiguration realmConfig;
    private Realm realm;
    private List<Equip> equips;
    private Spinner spnCasa;
    private Spinner spnFora;
    private TextView lblGolsCasa;
    private TextView lblGolsFora;

    private Dialog dialogData;

    private Equip equipCasa;
    private Equip equipFora;

    private Date date;
    private Partido partido;
    private int numJornada;

    private HashMap<String, Equip> mapEquips;

    private ArrayList<TextView> lblNomTitularsCasa;
    private ArrayList<Button> btnMinusTitularsCasa;
    private ArrayList<TextView> lblGolsTitularsCasa;
    private ArrayList<Button> btnPlusTitularsCasa;

    private ArrayList<TextView> lblNomReservaCasa;
    private ArrayList<Button> btnMinusReservaCasa;
    private ArrayList<TextView> lblGolsReservaCasa;
    private ArrayList<Button> btnPlusReservaCasa;

    private ArrayList<TextView> lblNomTitularsFora;
    private ArrayList<Button> btnMinusTitularsFora;
    private ArrayList<TextView> lblGolsTitularsFora;
    private ArrayList<Button> btnPlusTitularsFora;

    private ArrayList<TextView> lblNomReservaFora;
    private ArrayList<Button> btnMinusReservaFora;
    private ArrayList<TextView> lblGolsReservaFora;
    private ArrayList<Button> btnPlusReservaFora;

    private boolean equipsIguals;

    Context context;
    private boolean tancarActivitat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_partido);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("00/00/0000");

        context = this;
        getAllViewsById();
        createAlertDialog();

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        equips = realm.where(Equip.class).equalTo("eliminat",false).findAll();;
        mapEquips = new HashMap<>(equips.size());

        ArrayList<String> nomEquips = new ArrayList<>(equips.size());

        for (Equip e : equips) {
            String nomEquip = e.getNom();

            nomEquips.add(nomEquip);
            mapEquips.put(nomEquip, e);

        }

// Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, nomEquips);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view

        spnFora.setAdapter(spinnerArrayAdapter);
        spnCasa.setAdapter(spinnerArrayAdapter);


        spnFora.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nomEquipFora = spnFora.getSelectedItem().toString();
                Log.d("SPINNER", "nomEquipFora: " + nomEquipFora);

                equipFora = mapEquips.get(nomEquipFora);

                for (int i = 0; i < 5; ++i) {
                    String nomTitularFora = equipFora.getTitulars().get(i).getNom();
                    lblNomTitularsFora.get(i).setText(nomTitularFora);

                }
                for (int i = 0; i < 7; ++i) {
                    String nomReservaFora = equipFora.getReservas().get(i).getNom();
                    lblNomReservaFora.get(i).setText(nomReservaFora);

                }
                comprobarEquipsDiferents(spnCasa, spnFora);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnCasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nomEquipCasa = spnCasa.getSelectedItem().toString();
                Log.d("SPINNER", "nomEquipCasa: " + nomEquipCasa);

                equipCasa = mapEquips.get(nomEquipCasa);

                for (int i = 0; i < 5; ++i) {
                    String nomTitularCasa = equipCasa.getTitulars().get(i).getNom();
                    lblNomTitularsCasa.get(i).setText(nomTitularCasa);

                }
                for (int i = 0; i < 7; ++i) {
                    String nomReservaCasa = equipCasa.getReservas().get(i).getNom();
                    lblNomReservaCasa.get(i).setText(nomReservaCasa);
                }
                comprobarEquipsDiferents(spnCasa, spnFora);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadData();
        comprobarEquipsDiferents(spnCasa, spnFora);
    }

    private void comprobarEquipsDiferents(Spinner spnA, Spinner spnB) {
        String nomEquipCasa = spnA.getSelectedItem().toString();
        String nomEquipFora = spnB.getSelectedItem().toString();

        if (nomEquipCasa == nomEquipFora) {
            spnA.setBackgroundColor(Color.RED);
            spnB.setBackgroundColor(Color.RED);
            equipsIguals = true;
        }
        else {
            spnA.setBackgroundColor(Color.TRANSPARENT);
            spnB.setBackgroundColor(Color.TRANSPARENT);
            equipsIguals = false;
        }
    }

    private void loadData() {
        Intent intent = getIntent();
        long dataCreacio = intent.getLongExtra("dataCreacio", -1);

        numJornada = intent.getIntExtra("numJornada", -1);
        Log.println(Log.DEBUG, "[PARTIDO]", "Cargando partido: " + dataCreacio);

        if (dataCreacio != -1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            partido = realm.where(Partido.class).equalTo("dataCreacio", dataCreacio).findFirst();
            date = partido.getDataRealitzat();

            setTitle(getStringDate(date));

            spnCasa.setSelection(getIndex(spnCasa, partido.getEquipA().getNom()));
            spnFora.setSelection(getIndex(spnFora, partido.getEquipB().getNom()));

            lblGolsCasa.setText(String.valueOf(partido.getGolsA()));
            lblGolsFora.setText(String.valueOf(partido.getGolsB()));

            for (int i = 0; i < 5 ; ++i){
                int golTitularA = partido.getGolsTitularsA().get(i).value;
                lblGolsTitularsCasa.get(i).setText(String.valueOf(golTitularA));
                int golTitularB = partido.getGolsTitularsB().get(i).value;
                lblGolsTitularsFora.get(i).setText(String.valueOf(golTitularB));
            }

            for (int i = 0; i < 7; ++i) {
                int golReservaA = partido.getGolsReservasA().get(i).value;
                lblGolsReservaCasa.get(i).setText(String.valueOf(golReservaA));
                int golReservaB = partido.getGolsReservasB().get(i).value;
                lblGolsReservaFora.get(i).setText(String.valueOf(golReservaB));
            }
        }
    }

    private String getStringDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(cal.get(Calendar.MONTH));
        String year = String.valueOf(cal.get(Calendar.YEAR));

        return day + "/" + month + "/" + year;
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (partido != null) {
            getMenuInflater().inflate(R.menu.menu_editar_partido, menu);
        }else {
            getMenuInflater().inflate(R.menu.menu_anadir_partido, menu);
        }

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
            intent.putExtra("ajudaId", R.raw.afegir_partit);
            startActivity(intent);
        }

        if (id == R.id.action_calendar) {
            showDatePicker();

        }

        if (id == R.id.action_save) {
            if (equipsIguals) {
                Snackbar.make(findViewById(android.R.id.content), "Has de seleccionar equips diferents", Snackbar.LENGTH_SHORT).show();
                //Toast.makeText(this,"Has de seleccionar equips diferents.",Toast.LENGTH_SHORT).show();
                return true;
            }

            if (date != null) {
                guardarPartit();
                tancaActivitat();
            }else {
                tancarActivitat = true;
                dialogData.show();
            }

            return true;
        }

        if (id == R.id.action_delete) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Esborrar equip")
                    .setMessage("Esta segur que vol esborrar el partit?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            RealmResults<Partido> partidos = realm.where(Partido.class).equalTo("dataCreacio",partido.getDataCreacio()).findAll();
                            int partidosSize = partidos.size();
                            realm.beginTransaction();
                            for (int i = 0; i < partidosSize; ++i) {
                                partidos.get(i).desferResultat();
                            }
                            partidos.deleteAllFromRealm();
                            realm.commitTransaction();

                            Intent intent = new Intent(context, JornadaActivity.class);
                            intent.putExtra("numJornada", numJornada);
                            startActivity(intent);
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

    private void getAllViewsById() {
        spnCasa = (Spinner) findViewById(R.id.spnCasa);
        spnFora = (Spinner) findViewById(R.id.spnFora);

        lblGolsCasa = (TextView) findViewById(R.id.lblGolsCasa);
        lblGolsFora = (TextView) findViewById(R.id.lblGolsFora);

        lblNomTitularsCasa = new ArrayList<>();
        btnMinusTitularsCasa = new ArrayList<>();
        lblGolsTitularsCasa = new ArrayList<>();
        btnPlusTitularsCasa = new ArrayList<>();

        lblNomReservaCasa = new ArrayList<>();
        btnMinusReservaCasa = new ArrayList<>();
        lblGolsReservaCasa = new ArrayList<>();
        btnPlusReservaCasa = new ArrayList<>();

        lblNomTitularsFora = new ArrayList<>();
        btnMinusTitularsFora = new ArrayList<>();
        lblGolsTitularsFora = new ArrayList<>();
        btnPlusTitularsFora = new ArrayList<>();

        lblNomReservaFora = new ArrayList<>();
        btnMinusReservaFora = new ArrayList<>();
        lblGolsReservaFora = new ArrayList<>();
        btnPlusReservaFora = new ArrayList<>();

        lblNomTitularsCasa.add((TextView) findViewById(R.id.lblTitularCasa1));
        lblNomTitularsCasa.add((TextView) findViewById(R.id.lblTitularCasa2));
        lblNomTitularsCasa.add((TextView) findViewById(R.id.lblTitularCasa3));
        lblNomTitularsCasa.add((TextView) findViewById(R.id.lblTitularCasa4));
        lblNomTitularsCasa.add((TextView) findViewById(R.id.lblTitularCasa5));

        lblNomReservaCasa.add((TextView) findViewById(R.id.lblReservaCasa1));
        lblNomReservaCasa.add((TextView) findViewById(R.id.lblReservaCasa2));
        lblNomReservaCasa.add((TextView) findViewById(R.id.lblReservaCasa3));
        lblNomReservaCasa.add((TextView) findViewById(R.id.lblReservaCasa4));
        lblNomReservaCasa.add((TextView) findViewById(R.id.lblReservaCasa5));
        lblNomReservaCasa.add((TextView) findViewById(R.id.lblReservaCasa6));
        lblNomReservaCasa.add((TextView) findViewById(R.id.lblReservaCasa7));

        lblNomTitularsFora.add((TextView) findViewById(R.id.lblTitularFora1));
        lblNomTitularsFora.add((TextView) findViewById(R.id.lblTitularFora2));
        lblNomTitularsFora.add((TextView) findViewById(R.id.lblTitularFora3));
        lblNomTitularsFora.add((TextView) findViewById(R.id.lblTitularFora4));
        lblNomTitularsFora.add((TextView) findViewById(R.id.lblTitularFora5));

        lblNomReservaFora.add((TextView) findViewById(R.id.lblReservaFora1));
        lblNomReservaFora.add((TextView) findViewById(R.id.lblReservaFora2));
        lblNomReservaFora.add((TextView) findViewById(R.id.lblReservaFora3));
        lblNomReservaFora.add((TextView) findViewById(R.id.lblReservaFora4));
        lblNomReservaFora.add((TextView) findViewById(R.id.lblReservaFora5));
        lblNomReservaFora.add((TextView) findViewById(R.id.lblReservaFora6));
        lblNomReservaFora.add((TextView) findViewById(R.id.lblReservaFora7));

        btnMinusTitularsCasa.add((Button) findViewById(R.id.btnTitularMinusCasa1));
        btnMinusTitularsCasa.add((Button) findViewById(R.id.btnTitularMinusCasa2));
        btnMinusTitularsCasa.add((Button) findViewById(R.id.btnTitularMinusCasa3));
        btnMinusTitularsCasa.add((Button) findViewById(R.id.btnTitularMinusCasa4));
        btnMinusTitularsCasa.add((Button) findViewById(R.id.btnTitularMinusCasa5));

        btnMinusReservaCasa.add((Button) findViewById(R.id.btnReservaMinusCasa1));
        btnMinusReservaCasa.add((Button) findViewById(R.id.btnReservaMinusCasa2));
        btnMinusReservaCasa.add((Button) findViewById(R.id.btnReservaMinusCasa3));
        btnMinusReservaCasa.add((Button) findViewById(R.id.btnReservaMinusCasa4));
        btnMinusReservaCasa.add((Button) findViewById(R.id.btnReservaMinusCasa5));
        btnMinusReservaCasa.add((Button) findViewById(R.id.btnReservaMinusCasa6));
        btnMinusReservaCasa.add((Button) findViewById(R.id.btnReservaMinusCasa7));

        btnMinusTitularsFora.add((Button) findViewById(R.id.btnTitularForaMinus1));
        btnMinusTitularsFora.add((Button) findViewById(R.id.btnTitularForaMinus2));
        btnMinusTitularsFora.add((Button) findViewById(R.id.btnTitularForaMinus3));
        btnMinusTitularsFora.add((Button) findViewById(R.id.btnTitularForaMinus4));
        btnMinusTitularsFora.add((Button) findViewById(R.id.btnTitularForaMinus5));

        btnMinusReservaFora.add( (Button) findViewById(R.id.btnReservaMinusFora1));
        btnMinusReservaFora.add( (Button) findViewById(R.id.btnReservaMinusFora2));
        btnMinusReservaFora.add( (Button) findViewById(R.id.btnReservaMinusFora3));
        btnMinusReservaFora.add( (Button) findViewById(R.id.btnReservaMinusFora4));
        btnMinusReservaFora.add( (Button) findViewById(R.id.btnReservaMinusFora5));
        btnMinusReservaFora.add( (Button) findViewById(R.id.btnReservaMinusFora6));
        btnMinusReservaFora.add( (Button) findViewById(R.id.btnReservaMinusFora7));

        lblGolsTitularsCasa.add( (TextView) findViewById(R.id.lblGolsCasaTitular1));
        lblGolsTitularsCasa.add( (TextView) findViewById(R.id.lblGolsCasaTitular2));
        lblGolsTitularsCasa.add( (TextView) findViewById(R.id.lblGolsCasaTitular3));
        lblGolsTitularsCasa.add( (TextView) findViewById(R.id.lblGolsCasaTitular4));
        lblGolsTitularsCasa.add( (TextView) findViewById(R.id.lblGolsCasaTitular5));

        lblGolsReservaCasa.add((TextView) findViewById(R.id.lblGolsCasaReserva1));
        lblGolsReservaCasa.add((TextView) findViewById(R.id.lblGolsCasaReserva2));
        lblGolsReservaCasa.add((TextView) findViewById(R.id.lblGolsCasaReserva3));
        lblGolsReservaCasa.add((TextView) findViewById(R.id.lblGolsCasaReserva4));
        lblGolsReservaCasa.add((TextView) findViewById(R.id.lblGolsCasaReserva5));
        lblGolsReservaCasa.add((TextView) findViewById(R.id.lblGolsCasaReserva6));
        lblGolsReservaCasa.add((TextView) findViewById(R.id.lblGolsCasaReserva7));

        lblGolsTitularsFora.add((TextView) findViewById(R.id.lblGolsForaTitular1));
        lblGolsTitularsFora.add((TextView) findViewById(R.id.lblGolsForaTitular2));
        lblGolsTitularsFora.add((TextView) findViewById(R.id.lblGolsForaTitular3));
        lblGolsTitularsFora.add((TextView) findViewById(R.id.lblGolsForaTitular4));
        lblGolsTitularsFora.add((TextView) findViewById(R.id.lblGolsForaTitular5));

        lblGolsReservaFora.add((TextView) findViewById(R.id.lblGolsForaReserva1));
        lblGolsReservaFora.add((TextView) findViewById(R.id.lblGolsForaReserva2));
        lblGolsReservaFora.add((TextView) findViewById(R.id.lblGolsForaReserva3));
        lblGolsReservaFora.add((TextView) findViewById(R.id.lblGolsForaReserva4));
        lblGolsReservaFora.add((TextView) findViewById(R.id.lblGolsForaReserva5));
        lblGolsReservaFora.add((TextView) findViewById(R.id.lblGolsForaReserva6));
        lblGolsReservaFora.add((TextView) findViewById(R.id.lblGolsForaReserva7));

        btnPlusTitularsCasa.add((Button) findViewById(R.id.btnTitularPlusCasa1));
        btnPlusTitularsCasa.add((Button) findViewById(R.id.btnTitularPlusCasa2));
        btnPlusTitularsCasa.add((Button) findViewById(R.id.btnTitularPlusCasa3));
        btnPlusTitularsCasa.add((Button) findViewById(R.id.btnTitularPlusCasa4));
        btnPlusTitularsCasa.add((Button) findViewById(R.id.btnTitularPlusCasa5));

        btnPlusReservaCasa.add((Button) findViewById(R.id.btnReservaPlusCasa1));
        btnPlusReservaCasa.add((Button) findViewById(R.id.btnReservaPlusCasa2));
        btnPlusReservaCasa.add((Button) findViewById(R.id.btnReservaPlusCasa3));
        btnPlusReservaCasa.add((Button) findViewById(R.id.btnReservaPlusCasa4));
        btnPlusReservaCasa.add((Button) findViewById(R.id.btnReservaPlusCasa5));
        btnPlusReservaCasa.add((Button) findViewById(R.id.btnReservaPlusCasa6));
        btnPlusReservaCasa.add((Button) findViewById(R.id.btnReservaPlusCasa7));

        btnPlusTitularsFora.add((Button) findViewById(R.id.btnTitularPlusFora1));
        btnPlusTitularsFora.add((Button) findViewById(R.id.btnTitularPlusFora2));
        btnPlusTitularsFora.add((Button) findViewById(R.id.btnTitularPlusFora3));
        btnPlusTitularsFora.add((Button) findViewById(R.id.btnTitularPlusFora4));
        btnPlusTitularsFora.add((Button) findViewById(R.id.btnTitularPlusFora5));

        btnPlusReservaFora.add((Button) findViewById(R.id.btnReservaPlusFora1));
        btnPlusReservaFora.add((Button) findViewById(R.id.btnReservaPlusFora2));
        btnPlusReservaFora.add((Button) findViewById(R.id.btnReservaPlusFora3));
        btnPlusReservaFora.add((Button) findViewById(R.id.btnReservaPlusFora4));
        btnPlusReservaFora.add((Button) findViewById(R.id.btnReservaPlusFora5));
        btnPlusReservaFora.add((Button) findViewById(R.id.btnReservaPlusFora6));
        btnPlusReservaFora.add((Button) findViewById(R.id.btnReservaPlusFora7));


        for (int i = 0; i < 5; i++) {
            btnMinusTitularsCasa.get(i).setOnClickListener(new MinusClickListener(lblGolsTitularsCasa.get(i), lblGolsCasa));
            btnMinusTitularsFora.get(i).setOnClickListener(new MinusClickListener(lblGolsTitularsFora.get(i), lblGolsFora));

            btnPlusTitularsCasa.get(i).setOnClickListener(new PlusClickListener(lblGolsTitularsCasa.get(i),lblGolsCasa));
            btnPlusTitularsFora.get(i).setOnClickListener(new PlusClickListener(lblGolsTitularsFora.get(i),lblGolsFora));
        }

        for (int i = 0; i < 7; i++) {
            btnMinusReservaCasa.get(i).setOnClickListener(new MinusClickListener(lblGolsReservaCasa.get(i),lblGolsCasa));
            btnMinusReservaFora.get(i).setOnClickListener(new MinusClickListener(lblGolsReservaFora.get(i),lblGolsFora));

            btnPlusReservaCasa.get(i).setOnClickListener(new PlusClickListener(lblGolsReservaCasa.get(i),lblGolsCasa));
            btnPlusReservaFora.get(i).setOnClickListener(new PlusClickListener(lblGolsReservaFora.get(i),lblGolsFora));
        }


    }

    class PlusClickListener implements View.OnClickListener {

        TextView gols;
        TextView golsEquip;

        public PlusClickListener(TextView gols, TextView golsEquip) {
            this.gols = gols;
            this.golsEquip = golsEquip;
        }

        @Override
        public void onClick(View v) {
            int nGols = Integer.parseInt(gols.getText().toString());
            nGols++;
            gols.setText(String.valueOf(nGols));

            int nGolsEquip = Integer.parseInt(golsEquip.getText().toString());
            nGolsEquip++;
            golsEquip.setText((String.valueOf(nGolsEquip)));
        }
    }

    class MinusClickListener implements View.OnClickListener {

        TextView gols;
        TextView golsEquip;

        public MinusClickListener(TextView gols, TextView golsEquip) {
            this.gols = gols;
            this.golsEquip = golsEquip;
        }

        @Override
        public void onClick(View v) {
            int nGols = Integer.parseInt(gols.getText().toString());
            if (nGols > 0) {
                nGols--;
                gols.setText(String.valueOf(nGols));

                int nGolsEquip = Integer.parseInt(golsEquip.getText().toString());
                nGolsEquip--;
                golsEquip.setText((String.valueOf(nGolsEquip)));
            }


        }
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, monthOfYear+1, dayOfMonth);

            date = new Date(cal.getTimeInMillis());
            setTitle(getStringDate(date));

            if (tancarActivitat) {
                guardarPartit();
                tancaActivitat();
            }
        }
    };

    private void tancaActivitat() {
        Intent intentTanca = new Intent(context, JornadaActivity.class);
        intentTanca.putExtra("numJornada", numJornada);
        startActivity(intentTanca);
    }

    private void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Ha de seleccionar quan s'ha jugat el partit.").setTitle("Seleccionar data");
        builder.setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDatePicker();
                //guardarPartit(true);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogData = builder.create();
    }

    private void guardarPartit() {
        Jornada j = realm.where(Jornada.class).equalTo("numJornada", numJornada).findFirst();

        realm.beginTransaction();

        Partido p;
        if (partido == null) {
            p = new Partido();
        } else {
            p = partido;
            p.desferResultat();
            j.getPartidos().remove(p);
        }

        p.setEquipA(equipCasa);
        p.setEquipB(equipFora);
        p.setDataRealitzat(date);

        p.setGolsA(Integer.valueOf(lblGolsCasa.getText().toString()));
        p.setGolsB(Integer.valueOf(lblGolsFora.getText().toString()));

        for (int i = 0; i < 5; ++i) {
            p.getEquipA().getTitulars().get(i).addGols(Integer.valueOf(lblGolsTitularsCasa.get(i).getText().toString()));
            p.getEquipB().getTitulars().get(i).addGols(Integer.valueOf(lblGolsTitularsFora.get(i).getText().toString()));

            p.getGolsTitularsA().get(i).value = Integer.valueOf(lblGolsTitularsCasa.get(i).getText().toString());
            p.getGolsTitularsB().get(i).value = Integer.valueOf(lblGolsTitularsFora.get(i).getText().toString());
        }

        for (int i = 0; i < 7; ++i) {
            p.getEquipA().getReservas().get(i).addGols(Integer.valueOf(lblGolsReservaCasa.get(i).getText().toString()));
            p.getEquipB().getReservas().get(i).addGols(Integer.valueOf(lblGolsReservaFora.get(i).getText().toString()));

            p.getGolsReservasA().get(i).value = Integer.valueOf(lblGolsReservaCasa.get(i).getText().toString());
            p.getGolsReservasB().get(i).value = Integer.valueOf(lblGolsReservaFora.get(i).getText().toString());
        }

        if (p.getGolsA() == p.getGolsB()) {
            p.getEquipA().empata();
            p.getEquipB().empata();
        }else {
            if (p.getGolsA() > p.getGolsB()) {
                p.getEquipA().guanya();
                p.getEquipB().perd();
            }else {
                p.getEquipB().guanya();
                p.getEquipA().perd();
            }
        }

        j.getPartidos().add(p);
        realm.commitTransaction();
    }
}
