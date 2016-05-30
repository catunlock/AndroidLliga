package com.sanchez.lopez.alberto.campionatlliga;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class AjudaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imgAjuda = (ImageView) findViewById(R.id.imgAjuda);

        Intent intent = getIntent();
        int imgId = intent.getIntExtra("ajudaId", -1);

        if (imgId != -1) {
            imgAjuda.setImageResource(imgId);
        }
    }

}
