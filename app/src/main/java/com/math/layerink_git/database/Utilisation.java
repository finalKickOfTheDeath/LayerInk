package com.math.layerink_git.database;

/**
 * Created by Math on 09/03/2017.
 */

public class Utilisation {

    private long id;
    private String date;
    private int nbSauvegarde;
    private String couleurFavorite;


    public Utilisation(long id, String date, int nbSauvegarde, String couleurFavorite) {
        this.id = id;
        this.date = date;
        this.nbSauvegarde = nbSauvegarde;
        this.couleurFavorite = couleurFavorite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNbSauvegarde() {
        return nbSauvegarde;
    }

    public void setNbSauvegarde(int nbSauvegarde) {
        this.nbSauvegarde = nbSauvegarde;
    }

    public String getCouleurFavorite() {
        return couleurFavorite;
    }

    public void setCouleurFavorite(String couleurFavorite) {
        this.couleurFavorite = couleurFavorite;
    }

    public String toString() {
        return "" + this.getId() + " " + this.getDate() + " " + this.getNbSauvegarde() + " " + this.getCouleurFavorite();
    }


}
