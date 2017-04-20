package com.example.hearmyheart;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

/**
 * Created by Owner on 2017-03-29.
 */

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ThreadPolicy {

    // For smooth networking
    public ThreadPolicy() {

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
}



