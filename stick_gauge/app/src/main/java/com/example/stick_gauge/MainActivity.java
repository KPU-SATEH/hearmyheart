package com.example.stick_gauge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements Runnable{
    ProgressBar progressBar1;
    int progress=0;
    Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        try {
            progressBar1.setProgress(500);
            //setProgress는 프로그레스바의 진행정도를 지정해 줍니다, ()안에는 int형 숫자가 들어갈수 있습니다
        } catch (Exception e) {

        }
    }


    public void Start(View v){
        thread = new Thread(this);
        thread.start();
    }

    public void run(){
        progress=0;
        while(progress<500){
            ++progress;
            progressBar1.setProgress(progress);
            try {
                thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
