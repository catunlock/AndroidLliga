package com.sanchez.lopez.alberto.campionatlliga.model;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by SuNLoCK on 20/03/2016.
 */
public class Partido extends RealmObject{
    Date dataRealitzat;
    Equip equipA;
    Equip equipB;
    int golsA;
    int golsB;
    long dataCreacio;

    RealmList<Entero> golsTitularsA = new RealmList<Entero>();
    RealmList<Entero> golsTitularsB = new RealmList<Entero>();
    RealmList<Entero> golsReservasA = new RealmList<Entero>();
    RealmList<Entero> golsReservasB = new RealmList<Entero>();

    public Partido() {
        dataCreacio = System.nanoTime();
        for (int i = 0; i < 5; i++) {
            golsTitularsA.add(new Entero());
            golsTitularsB.add(new Entero());
        }

        for (int i = 0; i < 7; i++) {
            golsReservasA.add(new Entero());
            golsReservasB.add(new Entero());
        }
    }

    public Partido(Date dataRealitzat, Equip equipA, Equip equipB, int golsA, int golsB) {
        this();
        this.dataRealitzat = dataRealitzat;
        this.equipA = equipA;
        this.equipB = equipB;
        this.golsA = golsA;
        this.golsB = golsB;
    }

    public RealmList<Entero> getGolsTitularsA() {
        return golsTitularsA;
    }

    public RealmList<Entero> getGolsTitularsB() {
        return golsTitularsB;
    }

    public RealmList<Entero> getGolsReservasA() {
        return golsReservasA;
    }

    public RealmList<Entero> getGolsReservasB() {
        return golsReservasB;
    }

    public long getDataCreacio() {
        return dataCreacio;
    }

    public void ferEfectiuResultat() {
        if (golsA == golsB) {
            equipA.empata();
            equipB.empata();
        } else {
            if (golsA > golsB) {
                equipA.guanya();
                equipB.perd();
            } else {
                equipB.guanya();
                equipA.perd();
            }
        }

        for (int i = 0; i < 5; ++i) {
            equipA.getTitulars().get(i).addGols(golsTitularsA.get(i).value);
            equipB.getTitulars().get(i).addGols(golsTitularsB.get(i).value);
        }

        for (int i = 0; i < 7; ++i) {
            equipA.getReservas().get(i).addGols(golsReservasA.get(i).value);
            equipB.getReservas().get(i).addGols(golsReservasB.get(i).value);
        }

    }

    public void desferResultat() {
        if (golsA == golsB) {
            equipA.desferEmpat();
            equipB.desferEmpat();
        }
        else {
            if (golsA > golsB) {
                equipA.desferGuany();
                equipB.desferPerdut();
            }
            else {
                equipB.desferGuany();
                equipA.desferPerdut();
            }
        }

        for (int i = 0; i < 5; ++i) {
            equipA.getTitulars().get(i).subsGols(golsTitularsA.get(i).value);
            equipB.getTitulars().get(i).subsGols(golsTitularsB.get(i).value);
        }

        for (int i = 0; i < 7; ++i) {
            equipA.getReservas().get(i).subsGols(golsReservasA.get(i).value);
            equipB.getReservas().get(i).subsGols(golsReservasB.get(i).value);
        }
    }

    public Date getDataRealitzat() {
        return dataRealitzat;
    }

    public void setDataRealitzat(Date dataRealitzat) {
        this.dataRealitzat = dataRealitzat;
    }

    public Equip getEquipA() {
        return equipA;
    }

    public void setEquipA(Equip equipA) {
        this.equipA = equipA;
    }

    public Equip getEquipB() {
        return equipB;
    }

    public void setEquipB(Equip equipB) {
        this.equipB = equipB;
    }

    public int getGolsA() {
        return golsA;
    }

    public void setGolsA(int golsA) {
        this.golsA = golsA;
    }

    public int getGolsB() {
        return golsB;
    }

    public void setGolsB(int golsB) {
        this.golsB = golsB;
    }
}
