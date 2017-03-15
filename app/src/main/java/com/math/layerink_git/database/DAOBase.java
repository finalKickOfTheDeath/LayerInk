package com.math.layerink_git.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Math on 09/03/2017.
 */

public abstract class DAOBase {
    //méthodes qui permettent de récuperer la base de donnée et de la fermée

    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    protected final static int VERSION = 1;
    // Le nom du fichier qui représente ma base
    protected final static String NOM = "databaseLayerInk.db";

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    public DAOBase(Context pContext) {
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
        Log.d("data", " 3 on à récuperer la base");
    }

    public SQLiteDatabase open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getWritableDatabase();
        Log.d("data", " 6 on ouvre la base");
        return mDb;
    }

    public void close() {
        mDb.close();
        Log.d("data", " 10 on ferme la base");
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

}
