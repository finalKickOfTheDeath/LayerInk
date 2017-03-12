package com.math.layerink_git.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Math on 09/03/2017.
 */

public class UtilisationDAO extends DAOBase {
    //utilisationDAO extends DAOBase et peut donc utiliser les méthodes
    // pour accéder et fermer la base de donnée

    public static final String TABLE_NAME = "Utilisation";

    public static final String KEY = "id";
    public static final String DATE = "date";
    public static final String NBSAUV = "nbSauvegarde";
    public static final String FAVCOULEUR = "couleurFavorite";

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ("
            + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DATE + " TEXT, "
            + NBSAUV + " INTEGER, "
            + FAVCOULEUR + " TEXT "
            + ");";

    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";


    public UtilisationDAO(Context pContext) {
        super(pContext);
        Log.d("data", " 2 on creer utilisation dao");
    }

    public void ajouter(Utilisation u) {
        //ajouter une utilisation à la base
        ContentValues value = new ContentValues();
        value.put(UtilisationDAO.DATE, u.getDate());
        value.put(UtilisationDAO.NBSAUV, u.getNbSauvegarde());
        value.put(UtilisationDAO.FAVCOULEUR, u.getCouleurFavorite());

        //on insere une nouvelle entrée dans la base
        mDb.insert(UtilisationDAO.TABLE_NAME, null, value);
        Log.d("data", " 8 on insert l'utlisation dans la base");
    }

    public void supprimer(long id) {
        //suprimer une entrée d'utilisation de la base
        //delete renvoie le nombre de lignes supprimées
        mDb.delete(TABLE_NAME, KEY + " = ?", new String[] {String.valueOf(id)});
    }

    public void modifierNbSauv(Utilisation u) {
        //modifier une utilisation de la base
        ContentValues value = new ContentValues();
        value.put(UtilisationDAO.NBSAUV, u.getNbSauvegarde());

        mDb.update(TABLE_NAME, value, KEY  + " = ?", new String[] {String.valueOf(u.getId())});
    }

    public List<String> selectionDate(String d) {
        //obtenir les infos des utilisation pour une date donnée
        Cursor cursor = mDb.rawQuery("select id_utilisation as _id, date, nbSauvegarde, couleurFavorite from " + TABLE_NAME + " where date = ?", new String[]{d});

        List<String> list = new ArrayList<String>();
        Utilisation u = null;

        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String date = cursor.getString(1);
            int nbSauv = cursor.getInt(2);
            String couleur = cursor.getString(3);
            u = new Utilisation(id, date, nbSauv, couleur);
            list.add(u.toString());
        }
        cursor.close();
        return list;
    }


}
