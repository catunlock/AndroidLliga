package com.sanchez.lopez.alberto.campionatlliga.visualizadoras;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sanchez.lopez.alberto.campionatlliga.AnadirPartido;
import com.sanchez.lopez.alberto.campionatlliga.Jornadas;
import com.sanchez.lopez.alberto.campionatlliga.dialogs.DatePickerFragment;
import com.sanchez.lopez.alberto.campionatlliga.R;
import com.sanchez.lopez.alberto.campionatlliga.model.Equip;
import com.sanchez.lopez.alberto.campionatlliga.model.Jornada;
import com.sanchez.lopez.alberto.campionatlliga.model.Partido;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class JornadaActivity extends AppCompatActivity {


    private RealmConfiguration realmConfig;
    private Realm realm;
    private boolean locked = true;
    private Jornada jornada;

    private ListView listPartit;
    private TextView lblData;
    private TextView lblNum;
    private TextView lblNumPartits;
    private ImageButton btnCalendar;

    private Date date;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jornada);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jornada.getPartidos().size() < 5) {
                    Intent intent = new Intent(context, AnadirPartido.class);
                    intent.putExtra("numJornada", jornada.getNumJornada());
                    startActivity(intent);
                }else {
                    Context context = getApplicationContext();
                    CharSequence text = "No pots afegir mes partits a aquesta jornada.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        listPartit = (ListView) findViewById(R.id.listPartits);
        lblData = (TextView) findViewById(R.id.lblData);
        lblNum = (TextView) findViewById(R.id.lblNum);
        btnCalendar = (ImageButton) findViewById(R.id.btnCalendar);
        btnCalendar.setEnabled(false);
        lblNumPartits = (TextView) findViewById(R.id.lblNumPartits);

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        loadData();

        listPartit.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id)
            {
                final Partido partido = (Partido) parent.getItemAtPosition(position);

                Intent intent = new Intent(context, AnadirPartido.class);
                intent.putExtra("dataCreacio", partido.getDataCreacio());
                intent.putExtra("numJornada", jornada.getNumJornada());
                startActivity(intent);
            }

        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadData() {
        Intent intent = getIntent();
        int numJornada = intent.getIntExtra("numJornada",-1);
        locked = intent.getBooleanExtra("locked", true);

        if (numJornada != -1) {
            Log.println(Log.DEBUG, "[JORNADA]", "Cargando jornada: " + numJornada);
            jornada = realm.where(Jornada.class).equalTo("numJornada", numJornada).findFirst();

            lblData.setText(getStringDate(jornada.getData()));
            lblNum.setText(String.valueOf(numJornada));

            setTitle("Jornada " + String.valueOf(numJornada));

            final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this);
            listPartit.setAdapter(adapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jornada, menu);
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

        if (id == R.id.action_unlock) {
            if (locked) {
                locked = false;
                item.setIcon(R.drawable.ic_lock_white_48dp);
                btnCalendar.setImageResource(R.drawable.ic_today_blue_48px);
                updateLockableElements();
            } else {
                locked = true;
                item.setIcon(R.drawable.ic_lock_open_white_48dp);
                btnCalendar.setImageResource(R.drawable.ic_today_white_48dp);
                updateLockableElements();
            }
            return true;
        }

        if (id == R.id.action_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Esborrar jornada")
                    .setMessage("Esta segur que vol esborrar la jornada \""+jornada.getNumJornada()+"\" I TOTS ELS SEUS PARTITS?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO: Borrar todos los partidos asignados?
                            RealmResults<Jornada> result = realm.where(Jornada.class).equalTo("numJornada",jornada.getNumJornada()).findAll();
                            realm.beginTransaction();
                            RealmList<Partido> partidos = result.get(0).getPartidos();
                            int sizePartidos = partidos.size();
                            for (int i = 0; i < sizePartidos; ++i) {
                                partidos.get(i).desferResultat();
                                RealmResults<Partido> r2 = realm.where(Partido.class).equalTo("dataCreacio",partidos.get(i).getDataCreacio()).findAll();
                                r2.deleteAllFromRealm();
                            }

                            result.deleteAllFromRealm();
                            realm.commitTransaction();

                            Intent intent = new Intent(context, Jornadas.class);
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

    private void updateLockableElements() {
        btnCalendar.setEnabled(!locked);
    }

    private void updateDataBase() {
        realm.beginTransaction();

        jornada.setData(date);

        realm.commitTransaction();
    }

    private class MySimpleArrayAdapter extends ArrayAdapter<Partido> {
        private final Context context;
        private final List<Partido> partidos;

        public MySimpleArrayAdapter(Context context) {
            super(context, -1);
            partidos = jornada.getPartidos();
            lblNumPartits.setText(String.valueOf(partidos.size()));
            this.addAll(partidos);
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

            View rowView = inflater.inflate(R.layout.listview_partits, parent, false);

            TextView lblCasaNom = (TextView) rowView.findViewById(R.id.lblCasaNom);
            TextView lblForaNom = (TextView) rowView.findViewById(R.id.lblForaNom);
            TextView lblCasaGols = (TextView) rowView.findViewById(R.id.lblCasaGols);
            TextView lblForaGols = (TextView) rowView.findViewById(R.id.lblForaGols);

            ImageView imgCasaEscut = (ImageView) rowView.findViewById(R.id.imgCasaEscut);
            ImageView imgForaEscut = (ImageView) rowView.findViewById(R.id.imgForaEscut);

            Partido p = partidos.get(position);

            lblCasaNom.setText(p.getEquipA().getNom());
            lblForaNom.setText(p.getEquipB().getNom());
            lblCasaGols.setText(String.valueOf(p.getGolsA()));
            lblForaGols.setText(String.valueOf(p.getGolsB()));

            int idResourceEscutCasa = Integer.valueOf(p.getEquipA().getPathEscut());
            imgCasaEscut.setImageResource(idResourceEscutCasa);
            imgCasaEscut.setTag(idResourceEscutCasa);

            int idResourceEscutFora = Integer.valueOf(p.getEquipB().getPathEscut());
            imgForaEscut.setImageResource(idResourceEscutFora);
            imgForaEscut.setTag(idResourceEscutFora);

            return rowView;
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
            cal.set(year, monthOfYear + 1, dayOfMonth);

            date = new Date(cal.getTimeInMillis());
            lblData.setText(getStringDate(date));

            updateDataBase();
        }
    };

    private String getStringDate(Date date) {
        if (date == null) {
            return "00/00/0000";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(cal.get(Calendar.MONTH));
        String year = String.valueOf(cal.get(Calendar.YEAR));

        return day + "/" + month + "/" + year;
    }
}
