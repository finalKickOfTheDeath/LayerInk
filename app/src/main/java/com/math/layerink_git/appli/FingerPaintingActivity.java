package com.math.layerink_git.appli;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.math.layerink_git.database.Utilisation;
import com.math.layerink_git.database.UtilisationDAO;
import com.math.layerink_git.drawing.DrawingView;
import com.math.layerink_git.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FingerPaintingActivity extends AppCompatActivity {

    private final static String MESSAGE_DATA = "com.math.layerink_git.appli.DATA";

    private ImageButton btnMenu;
    private ImageButton btnSave;
    private ImageButton btnCamera;
    private ImageButton btnGallery;
    private ImageButton btnBrush;
    private ImageButton btnReset;

    private String favcolor;
    private int nbSauv;

    DrawingView drawingView ;
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        // pour mettre l'activité en fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fingerpaint);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        favcolor = "nan";
        nbSauv = 0;

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnCamera = (ImageButton) findViewById(R.id.btnCamera);
        btnGallery = (ImageButton) findViewById(R.id.btnGallery);
        btnBrush = (ImageButton) findViewById(R.id.btnBrush);
        btnReset = (ImageButton) findViewById(R.id.btnReset);

        btnSave.setVisibility(View.INVISIBLE);
        btnCamera.setVisibility(View.INVISIBLE);
        btnGallery.setVisibility(View.INVISIBLE);
        btnBrush.setVisibility(View.INVISIBLE);
        btnReset.setVisibility(View.INVISIBLE);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);

        drawingView = (DrawingView) findViewById(R.id.drawingView);
        drawingView.setPaint(paint);

        btnMenu.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Log.d("deb", "btnMenu touched!");

                        if(btnSave.getVisibility() == View.INVISIBLE) {
                            btnSave.setVisibility(View.VISIBLE);
                            btnCamera.setVisibility(View.VISIBLE);
                            btnGallery.setVisibility(View.VISIBLE);
                            btnBrush.setVisibility(View.VISIBLE);
                            btnReset.setVisibility(View.VISIBLE);
                        } else {
                            btnSave.setVisibility(View.INVISIBLE);
                            btnCamera.setVisibility(View.INVISIBLE);
                            btnGallery.setVisibility(View.INVISIBLE);
                            btnBrush.setVisibility(View.INVISIBLE);
                            btnReset.setVisibility(View.INVISIBLE);
                        }
                    }
                }
        );

        btnMenu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), DataReader.class);
                intent.putExtra("MESSAGE_DATA", "data en cours");
                startActivity(intent);
                return true;
            }
        });

        btnReset.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        drawingView.clean();
                    }
                }
        );

        btnSave.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        savePictureToFile();
                    }
                }
        );

        btnBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = FingerPaintingActivity.this;

                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle(R.string.titleBrushDialog)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorChangedListener(new OnColorChangedListener() {
                            @Override
                            public void onColorChanged(int selectedColor) {
                                // Handle on color change
                                Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setPositiveButton(R.string.doneBushDialog, new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                changePaintColor(selectedColor);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Color List:");
                                        sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null)
                                        favcolor = sb.toString();
                                        Toast.makeText(getApplicationContext(), favcolor, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancelBrushDialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(FingerPaintingActivity.this, android.R.color.holo_blue_bright))
                        .build()
                        .show();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });

    }// fin du onCreate

    @Override
    protected void onPause() {
        super.onPause();
        //on enregistre le nombre de sauvegarde et la couleur favorite (la dernière couleur utilisée)
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        String date = format.format(new Date().getTime());
        Utilisation u = new Utilisation(0, date, nbSauv, favcolor);
        Log.d("data", "uilisation : " + u.getId() + " " + u.getDate() + " " + u.getNbSauvegarde() + " " + u.getCouleurFavorite());
        if(!u.getCouleurFavorite().equals("nan")) {
            //on rentre l'utilisation dans la base de donnée
            //on recupere la base de donnée
            UtilisationDAO utilisationDAO = new UtilisationDAO(this);
            Log.d("data", " 1 on va récuperer la base");
            utilisationDAO.open();
            Log.d("data", " 4 on va ouvrir la base");
            utilisationDAO.ajouter(u);
            Log.d("data", " 7 on va ajouter à la base");
            utilisationDAO.close();
            Log.d("data", " 9 on va fermer la base");
        }
    }

    public void takePhoto(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Toast.makeText(getApplicationContext(), R.string.message_takephoto, Toast.LENGTH_SHORT).show();
            startActivityForResult(takePictureIntent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Drawable image = new BitmapDrawable(getResources(), imageBitmap);
            //findViewById(R.id.activity_main).setBackground(image);
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            Rect rect = new Rect(0,0, metrics.widthPixels, metrics.heightPixels);
            drawingView.getCanvas().drawBitmap(imageBitmap, null, rect, null);
        }
    }

    public void savePictureToFile() {
        DrawingView view = (DrawingView) findViewById(R.id.drawingView);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.destroyDrawingCache();
        File file, f;
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "PicturesFolder");
        file.mkdirs();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        Date now = new Date();
        String fileName = formatter.format(now);
        f = new File(file.getAbsolutePath() + file.separator + "image_" + fileName + ".png");
        FileOutputStream ostream = null;
        try {
            ostream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            addImageToGallery(f.getAbsolutePath(), FingerPaintingActivity.this);
            nbSauv++;
            Toast.makeText(getApplicationContext(), R.string.message_sauv, Toast.LENGTH_SHORT).show();
            try {
                ostream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addImageToGallery(final String filePath, final Context context) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void changePaintColor(int selectedColor) {
        paint.setColor(selectedColor);
    }

}
