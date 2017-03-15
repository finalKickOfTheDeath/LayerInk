package com.math.layerink_git.appli;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.math.layerink_git.R;
import com.math.layerink_git.database.Utilisation;
import com.math.layerink_git.database.UtilisationDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataReader extends ListActivity {

    private UtilisationDAO utilisationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActionBar bar = getSupportActionBar();
        //ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1690D0"));
        //bar.setBackgroundDrawable(colorDrawable);
        setContentView(R.layout.activity_data_reader);

        String message_data = getIntent().getStringExtra("MESSAGE_DATA");
        List<String> list;

        //on recupere les données de la base à afficher
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        String now = format.format(new Date().getTime());

        utilisationDAO = new UtilisationDAO(this);
        utilisationDAO.open();

        list = utilisationDAO.selectionDate(now);

        for(String u : list) {
            Log.d("data", " données recupérées : " + u);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        setListAdapter(adapter);

        //utilisationDAO.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        utilisationDAO.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        utilisationDAO.open();
    }
}
