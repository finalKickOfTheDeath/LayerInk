package com.math.layerink_git;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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

        Log.d("deb", "ici ok5");

    }// fin du onCreate

    public void savePictureToFile() {
        OutputStream output;
        // Find the SD card path
        File filepath = Environment.getExternalStorageDirectory();
        // Create a new folder in the SD card
        File dir = new File(filepath.getAbsolutePath(), "PicturesFolder");
        dir.mkdirs();
        // Retrieve the image from the view
        com.math.layerink_git.DrawingView view = (com.math.layerink_git.DrawingView)findViewById(R.id.drawingView);
        Bitmap bitmap = view.getBitmap();
        // Create a name for the saved image
        File file = new File(dir, "Picture.jpg" );
        try {
            output = new FileOutputStream(file);
            // Compress into jpeg format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            addImageToGallery(file.getAbsolutePath(), FingerPaintingActivity.this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addImageToGallery(final String filePath, final Context context) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

}
