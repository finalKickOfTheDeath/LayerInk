package com.math.layerink_git.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Math on 09/03/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    //classe pour gérer les relation avec la base de donnée

    public static final String UTILISATION_KEY = "id_utilisation";
    public static final String UTILISATION_DATE = "date";
    public static final String UTILISATION_NBSAUV = "nbSauvegarde";
    public static final String UTILISATION_FAVCOULEUR = "couleurFavorite";

    public static final String UTILISATION_TABLE_NAME = "Utilisation";

    public static final String UTILISATION_TABLE_CREATE =
            "CREATE TABLE " + UTILISATION_TABLE_NAME
            + " ("
            + UTILISATION_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + UTILISATION_DATE + " TEXT, "
            + UTILISATION_NBSAUV + " INTEGER, "
            + UTILISATION_FAVCOULEUR + " TEXT "
            + ");";

    public static final String UTILISATION_TABLE_DROP = "DROP TABLE IF EXISTS " + UTILISATION_TABLE_NAME + ";";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d("data", "on est dans le constructeur du databasehandler");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UTILISATION_TABLE_CREATE);
        Log.d("data", " 5 creation de la base");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UTILISATION_TABLE_DROP);
        onCreate(db);
    }
}
