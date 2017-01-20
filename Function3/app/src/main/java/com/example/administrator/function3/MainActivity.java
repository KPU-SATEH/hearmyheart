package com.example.administrator.function3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void post(View view) {
        Intent intent = new Intent(getApplicationContext(),noticeboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void etc(View view) {
        Intent intent = new Intent(getApplicationContext(),Etc_noticeboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void transport(View view) {
        Intent intent = new Intent(getApplicationContext(),Transport_noticeboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void fcs(View view) {
        Intent intent = new Intent(getApplicationContext(),FCS_noticeboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void location(View view) {
        Intent intent = new Intent(getApplicationContext(),Location_noticeboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void best(View view) {
        Intent intent = new Intent(getApplicationContext(),Best_noticeboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
