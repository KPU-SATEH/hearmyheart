package com.example.hearmyheart;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class function2 extends Activity {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    boolean flag = false;
    Button camera_btn,btn1,btn2;
    TextView tv;
    ArrayList<String> list;
    ArrayList<String> title;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        list = (ArrayList<String>) getIntent().getSerializableExtra("idx");
        title = (ArrayList<String>) getIntent().getSerializableExtra("title");
        num = Integer.parseInt(list.get(0));

        setContentView(R.layout.activity_function1);
        btn1 = (Button) findViewById(R.id.prev_btn);
        btn2 = (Button) findViewById(R.id.next_btn);
        tv = (TextView) findViewById(R.id.text);
        camera_btn = (Button) findViewById(R.id.button1);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceListener);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num > 0) {
                    num--;
                    tv.setText(title.get(num));
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num < list.size()-1) {
                    num++;
                    tv.setText(title.get(num));
                }
            }
        });

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    surfaceView.setVisibility(View.INVISIBLE);
                    flag = false;
                } else {
                    surfaceView.setVisibility(View.VISIBLE);
                    flag = true;
                }
            }
        });
    }

    /////////// 안드로이드 화면에 게시판 목록화를 띄우는 함수 //////////////////

    private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback(){

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            camera = Camera.open(1);
            try{
                camera.setPreviewDisplay(holder);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }



        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(width, height);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.release();
            camera = null;
        }
    };
}