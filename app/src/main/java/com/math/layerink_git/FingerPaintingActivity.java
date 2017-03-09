package com.math.layerink_git;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FingerPaintingActivity extends AppCompatActivity {

    private ImageButton btnMenu;
    private ImageButton btnSave;
    private ImageButton btnCamera;
    private ImageButton btnGallery;
    private ImageButton btnBrush;
    private ImageButton btnReset;

    DrawingView drawingView ;
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        // pour mettre l'activité en fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fingerpaint);
        getSupportActionBar().hide();

        Log.d("deb", "ici ok1");

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

        Log.d("deb", "ici ok2");

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

        Log.d("deb", "ici ok3");

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
                                        Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
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

        Log.d("deb", "ici ok5");

    }// fin du onCreate

    public void savePictureToFile() {
        com.math.layerink_git.DrawingView view = (com.math.layerink_git.DrawingView) findViewById(R.id.drawingView);
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
