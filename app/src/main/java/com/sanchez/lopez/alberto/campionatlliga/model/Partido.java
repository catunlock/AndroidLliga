package com.sanchez.lopez.alberto.campionatlliga.model;

import com.orm.SugarRecord;

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

    public void desferResultat() {
        if (golsA == golsB) {
            equipA.setEmpatats(equipA.getEmpatats() - 1);
            equipB.setEmpatats(equipB.getEmpatats() - 1);

            equipA.setPunts(equipA.getPunts() - 1);
            equipB.setPunts(equipB.getPunts() - 1);
        }
        else {
            if (golsA > golsB) {
                equipA.setGuanyats(equipA.getGuanyats() -1);
                equipB.setPerduts(equipB.getPerduts() -1 );
                equipA.setPunts(equipA.getPunts() - 3);
            }
            else {
                equipB.setGuanyats(equipB.getGuanyats() -1);
                equipA.setPerduts(equipA.getPerduts() -1 );
                equipB.setPunts(equipB.getPunts() - 3);
            }
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
