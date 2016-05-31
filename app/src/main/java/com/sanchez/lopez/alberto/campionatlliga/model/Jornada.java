package com.sanchez.lopez.alberto.campionatlliga.model;


import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by SuNLoCK on 20/03/2016.
 */
public class Jornada extends RealmObject {
    Date data;
    int numJornada;
    RealmList<Partido> partidos;

    public Jornada() {
        partidos = new RealmList<Partido>();
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getNumJornada() {
        return numJornada;
    }

    public void setNumJornada(int numJornada) {
        this.numJornada = numJornada;
    }

    public RealmList<Partido> getPartidos() {
        return partidos;
    }
}
