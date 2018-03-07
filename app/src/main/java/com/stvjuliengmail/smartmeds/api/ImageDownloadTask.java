package com.stvjuliengmail.smartmeds.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Steven on 2/22/2018.
 */

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private final String TAG = getClass().getSimpleName();

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
            return myBitmap;
        } catch (Exception e) {
//            Log.d(TAG, "Exception: " + e.getMessage());
            return null;
        }
    }

}
