package com.example.hearmyheart;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Owner on 2017-03-29.
 */
public class ImageRoader {

    private final String serverUrl = "http://52.78.155.88/image/";

    public ImageRoader() {

        new ThreadPolicy();
    }

    public Bitmap getBitmapImg(String imgStr) {

        Bitmap bitmapImg = null;

        try {
            URL url = new URL(serverUrl +
                    URLEncoder.encode(imgStr, "utf-8"));
            // Character is converted to 'UTF-8' to prevent broken

            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            bitmapImg = BitmapFactory.decodeStream(is);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return bitmapImg;
    }
}



