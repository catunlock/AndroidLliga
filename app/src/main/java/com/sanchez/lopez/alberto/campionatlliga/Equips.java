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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sanchez.lopez.alberto.campionatlliga.model.Equip;
import com.sanchez.lopez.alberto.campionatlliga.visualizadoras.EquipViewer;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Equips extends AppCompatActivity {

    ListView listView;

    private RealmConfiguration realmConfig;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equips);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listView);

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this);

        listView.setAdapter(adapter);

        final Equips equips = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id)
            {
                final Equip equip = (Equip) parent.getItemAtPosition(position);

                Intent intent = new Intent(equips, EquipViewer.class);
                intent.putExtra("nomEquip", equip.getNom());
                startActivity(intent);
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        MySimpleArrayAdapter adapter = (MySimpleArrayAdapter) listView.getAdapter();
        adapter.reload();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_equips, menu);
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
            intent.putExtra("ajudaId", R.raw.equips);
            startActivity(intent);
        }

        if (id == R.id.action_add) {
            Intent intent = new Intent(this, AfegirEquip.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    private class MySimpleArrayAdapter extends ArrayAdapter<Equip> {
        private final Context context;
        private List<Equip> equips;

        public MySimpleArrayAdapter(Context context) {
            super(context, -1);
            reload();
            this.context = context;

        }

        public void reload() {
            equips = realm.where(Equip.class).equalTo("eliminat",false).findAll();
            this.clear();
            this.addAll(equips);
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

            View rowView = inflater.inflate(R.layout.listview_equips, parent, false);
            TextView txtNomEquip = (TextView) rowView.findViewById(R.id.lblNomJugador);
            TextView txtCiutatEquip = (TextView) rowView.findViewById(R.id.txtCiutatEquip);
            ImageView imgEscut = (ImageView) rowView.findViewById(R.id.imgEscut);

            Equip e = equips.get(position);

            txtNomEquip.setText(e.getNom());
            txtCiutatEquip.setText(e.getCiutat());

            int idResourceEscut = Integer.valueOf(e.getPathEscut());
            imgEscut.setImageResource(idResourceEscut);
            imgEscut.setTag(idResourceEscut);

            return rowView;
        }
    }
}
