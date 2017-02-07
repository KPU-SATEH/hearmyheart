/*
package com.example.hearmyheart;

import android.os.AsyncTask;

public class GifDataDownloader extends AsyncTask<String, Void, byte[]> {
    @Override protected byte[] doInBackground(final String... params) {
        final String gifUrl = params[0];

        if (gifUrl == null)
            return null;

        try {
            return ByteArrayHttpClient.get(gifUrl);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
}

*/