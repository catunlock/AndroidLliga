package com.sanchez.lopez.alberto.campionatlliga;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sanchez.lopez.alberto.campionatlliga.model.Jornada;
import com.sanchez.lopez.alberto.campionatlliga.visualizadoras.JornadaActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Jornadas extends AppCompatActivity {

    ListView listJornadas;

    private RealmConfiguration realmConfig;
    private Realm realm;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jornadas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        listJornadas = (ListView) findViewById(R.id.listJornadas);


        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this);
        listJornadas.setAdapter(adapter);

        listJornadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id)
            {
                final Jornada jornada = (Jornada) parent.getItemAtPosition(position);

                Intent intent = new Intent(context, JornadaActivity.class);
                intent.putExtra("numJornada", jornada.getNumJornada());
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jornadas, menu);
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

        if (id == R.id.action_add) {
            Intent intent = new Intent(this, AfegirJornada.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class MySimpleArrayAdapter extends ArrayAdapter<Jornada> {
        private final Context context;
        private final List<Jornada> jornadas;

        public MySimpleArrayAdapter(Context context) {
            super(context, -1);
            jornadas = realm.where(Jornada.class).findAll();
            this.addAll(jornadas);
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

            View rowView = inflater.inflate(R.layout.listview_jornadas, parent, false);

            TextView lblNumJornada = (TextView) rowView.findViewById(R.id.lblNumJornada);
            TextView lblData = (TextView) rowView.findViewById(R.id.lblData);
            TextView lblNumPartidos = (TextView) rowView.findViewById(R.id.lblNumPartidos);

            Jornada j = jornadas.get(position);

            lblNumJornada.setText(String.valueOf(j.getNumJornada()));
            lblData.setText(getStringDate(j.getData()));
            lblNumPartidos.setText(String.valueOf(j.getPartidos().size()));

            return rowView;
        }
    }

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
