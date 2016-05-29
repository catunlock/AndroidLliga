package com.sanchez.lopez.alberto.campionatlliga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private ArrayList<ImageView> imgViews = new ArrayList<>(9);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgViews.add((ImageView) findViewById(R.id.imageView7));
        imgViews.add((ImageView) findViewById(R.id.imageView8));
        imgViews.add((ImageView) findViewById(R.id.imageView9));
        imgViews.add((ImageView) findViewById(R.id.imageView10));
        imgViews.add((ImageView) findViewById(R.id.imageView11));
        imgViews.add((ImageView) findViewById(R.id.imageView12));
        imgViews.add((ImageView) findViewById(R.id.imageView13));
        imgViews.add((ImageView) findViewById(R.id.imageView14));
        imgViews.add((ImageView) findViewById(R.id.imageView15));
        imgViews.add((ImageView) findViewById(R.id.imageView16));
        imgViews.add((ImageView) findViewById(R.id.imageView17));
        imgViews.add((ImageView) findViewById(R.id.imageView18));


        imgViews.get(0).setImageResource(R.mipmap.medalla_adoquin);
        imgViews.get(1).setImageResource(R.mipmap.medalla_arcoiris);
        imgViews.get(2).setImageResource(R.mipmap.medalla_bosque);
        imgViews.get(3).setImageResource(R.mipmap.medalla_calor);
        imgViews.get(4).setImageResource(R.mipmap.medalla_carambano);
        imgViews.get(5).setImageResource(R.mipmap.medalla_cienaga);
        imgViews.get(6).setImageResource(R.mipmap.medalla_faro);
        imgViews.get(7).setImageResource(R.mipmap.medalla_lignito);
        imgViews.get(8).setImageResource(R.mipmap.medalla_piedra);
        imgViews.get(9).setImageResource(R.mipmap.medalla_tierra);
        imgViews.get(10).setImageResource(R.mipmap.medalla_volcan);
        imgViews.get(11).setImageResource(R.mipmap.medalla_trueno);

        imgViews.get(0).setTag(R.mipmap.medalla_adoquin);
        imgViews.get(1).setTag(R.mipmap.medalla_arcoiris);
        imgViews.get(2).setTag(R.mipmap.medalla_bosque);
        imgViews.get(3).setTag(R.mipmap.medalla_calor);
        imgViews.get(4).setTag(R.mipmap.medalla_carambano);
        imgViews.get(5).setTag(R.mipmap.medalla_cienaga);
        imgViews.get(6).setTag(R.mipmap.medalla_faro);
        imgViews.get(7).setTag(R.mipmap.medalla_lignito);
        imgViews.get(8).setTag(R.mipmap.medalla_piedra);
        imgViews.get(9).setTag(R.mipmap.medalla_tierra);
        imgViews.get(10).setTag(R.mipmap.medalla_volcan);
        imgViews.get(11).setTag(R.mipmap.medalla_trueno);


        for (int i = 0; i < 12; ++i) {
            imgViews.get(i).setOnClickListener(new MyOnClickListner());
        }
    }


    private class MyOnClickListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Intent resultIntent = new Intent();
            ImageView imgView = (ImageView) v;
            resultIntent.putExtra("imgSelected", (int) imgView.getTag());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
