package com.math.layerink_git;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.math.layerink_git.R.layout.activity_fingerpaint;

public class TEMP_SavePictureToFile extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_fingerpaint);
    }

    public void savePictureToFile() {
        OutputStream output;
        // Find the SD card path
        File filepath = Environment.getExternalStorageDirectory();
        // Create a new folder in the SD card
        File dir = new File(filepath.getAbsolutePath(), "PicturesFolder");
        dir.mkdirs();
        // Retrieve the image from the view
        com.math.layerink_git.DrawingView view = (com.math.layerink_git.DrawingView)findViewById(R.id.drawingView);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        // Create a name for the saved image
        File file = new File(dir, "Picture.jpg" );
        try {
            output = new FileOutputStream(file);
            // Compress into jpeg format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            addImageToGallery(file.getAbsolutePath(), TEMP_SavePictureToFile.this);
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
