package com.sanchez.lopez.alberto.campionatlliga.model;

import com.orm.SugarRecord;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by SuNLoCK on 20/03/2016.
 */
public class Equip extends RealmObject {
    public static final int NUM_TITULARS = 5;
    public static final int NUM_RESERVAS = 7;

    @Required
    String nom;
    @Required
    String ciutat;
    @Required
    String pathEscut;

    int punts;
    int guanyats;
    int perduts;
    int empatats;

    boolean eliminat = false;

    public boolean isEliminat() {
        return eliminat;
    }

    public void setEliminat(boolean eliminat) {
        this.eliminat = eliminat;
    }

    public void setPathEscut(String pathEscut) {
        this.pathEscut = pathEscut;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCiutat(String ciutat) {
        this.ciutat = ciutat;
    }

    public int getGuanyats() {
        return guanyats;
    }

    public int getPerduts() {
        return perduts;
    }

    public int getEmpatats() {
        return empatats;
    }

    public void setGuanyats(int guanyats) {
        this.guanyats = guanyats;
    }

    public void setPerduts(int perduts) {
        this.perduts = perduts;
    }

    public void setEmpatats(int empatats) {
        this.empatats = empatats;
    }

    public void empata() {
        empatats += 1;
        punts += 1;
    }

    public void perd() {
        perduts += 1;
    }

    public void guanya() {
        guanyats += 1;
        punts += 3;
    }

    public void desferGuany() {
        guanyats -= 1;
        punts -= 3;
    }

    public void desferPerdut() {
        perduts -= 1;
    }

    public void desferEmpat() {
        empatats -= 1;
        punts -= 1;
    }



    RealmList<Jugador> titulars;
    RealmList<Jugador> reservas;

    public Equip() {
        titulars = new RealmList<>();
        for (int i = 0; i < NUM_TITULARS; ++i) {
            titulars.add(new Jugador("", 0, this));
        }

        reservas = new RealmList<>();
        for (int i = 0; i < NUM_RESERVAS; ++i) {
            reservas.add(new Jugador("", 0, this));
        }
    }

    public Equip(String nom, String ciutat, String pathEscut) {
        this();
        this.nom = nom;
        this.ciutat = ciutat;
        this.pathEscut = pathEscut;
        punts = 0;
    }

    @Override
    public String toString() {
        return "EquipJugadors{" +
                "nom='" + nom + '\'' +
                ", ciutat='" + ciutat + '\'' +
                ", pathEscut='" + pathEscut + '\'' +
                ", titulars=" + titulars +
                ", reservas=" + reservas +
                '}';
    }

    public String getNom() {
        return nom;
    }

    public String getCiutat() {
        return ciutat;
    }

    public String getPathEscut() {
        return pathEscut;
    }

    public RealmList<Jugador> getTitulars() {
        return titulars;
    }

    public RealmList<Jugador> getReservas() {
        return reservas;
    }

    public int getPunts() {
        return punts;
    }

}
