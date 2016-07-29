package com.teach.mycanvasapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.show_canvas) {
            View v = new MyCanvas(getApplicationContext());
            Bitmap bitmap = Bitmap.createBitmap(500/*width*/, 500/*height*/, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            v.draw(canvas);
            imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
            return true;
        } else if (id == R.id.save_canvas) {
            saveImageToGallery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveImageToGallery() {
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        //Writing image to SD card.
        //It could also be saved to internal storage.
        // That way, we don't need to have extra permissions
        File dir = new File("/sdcard/tempfolder/");

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File output = new File(dir, "tempfile.jpg");
            if (!output.exists())
                output.createNewFile();

            OutputStream os = null;
            os = new FileOutputStream(output);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();

            //Scans image and save it in gallery
            MediaScannerConnection.scanFile(this, new String[] { output.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.d("Test", "Image saved in gallery!");
                        }
                    }
            );
        } catch (Exception e) {
            Log.d("Test", "Exception: " + e.getMessage());
        }
    }
}