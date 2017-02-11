package com.example.administrator.audio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void clickclick(View view) {
        String PATH = "C:\\Users\\Administrator\\Nox_share\\Other";
        String videoPath=PATH+"/Russian.mp4";
        String audioPath=PATH+"/audio.pcm";

        new AudioFromVideo(videoPath,audioPath).start();
    }
}
