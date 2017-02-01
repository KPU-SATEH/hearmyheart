package com.example.hearmyheart;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;

import java.util.ArrayList;

public class function1 extends Activity implements Runnable{
    ProgressBar progressBar1,progressBar2;
    int progress=0;
    Thread thread;

    private static final String TAG = "MainActivity";
    private GifImageView gifImageView;
    private Button btnToggle;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean flag = false;
    private boolean stopped = true;
    Button camera_btn,btn1,btn2;
    TextView tv;
    ArrayList<String> idx;
    ArrayList<String> title;
    int num;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        idx = (ArrayList<String>) getIntent().getSerializableExtra("idx");
        title = (ArrayList<String>) getIntent().getSerializableExtra("title");
        num = Integer.parseInt(idx.get(0));

        setContentView(R.layout.activity_function1);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        btn1 = (Button) findViewById(R.id.prev_btn);
        btn2 = (Button) findViewById(R.id.next_btn);
        camera_btn = (Button) findViewById(R.id.button1);
        btnToggle = (Button) findViewById(R.id.btnToggle);

        tv = (TextView) findViewById(R.id.text);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceListener);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        progressBar1.setProgress(0);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopped = false;
                progressBar1.setProgress(0);
                gifImageView.stopAnimation();
                if(num > 0) {
                    num--;
                    tv.setText(title.get(num));
                }
                new GifDataDownloader() {
                    @Override protected void onPostExecute(final byte[] bytes) {
                        gifImageView.setBytes(bytes);
                        Log.d(TAG, "GIF width is " + gifImageView.getGifWidth());
                        Log.d(TAG, "GIF height is " + gifImageView.getGifHeight());
                    }
                }.execute("http://13.112.10.21/test"+idx.get(0)+".gif");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopped = false;
                progressBar1.setProgress(0);
                gifImageView.stopAnimation();
                if(num < idx.size()-1) {
                    num++;
                    tv.setText(title.get(num));
                }

                new GifDataDownloader() {
                    @Override protected void onPostExecute(final byte[] bytes) {
                        gifImageView.setBytes(bytes);
                        Log.d(TAG, "GIF width is " + gifImageView.getGifWidth());
                        Log.d(TAG, "GIF height is " + gifImageView.getGifHeight());
                    }
                }.execute("http://13.112.10.21/test"+idx.get(1)+".gif");
            }
        });

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag) {
                    surfaceView.setVisibility(View.INVISIBLE);
                    flag = false;
                }
                else {
                    surfaceView.setVisibility(View.VISIBLE);
                    flag = true;
                }
            }
        });

        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gifImageView.isAnimating()) {
                    stopped = false;
                    progressBar1.setProgress(progress);
                    gifImageView.stopAnimation();
                } else {
                    stopped = true;
                    progressBar1.setProgress(progress);
                    gifImageView.startAnimation();
                    thread = new Thread(function1.this);
                    thread.start();
                }
            }
        });

        gifImageView.setOnAnimationStop(new GifImageView.OnAnimationStop() {
            @Override public void onAnimationStop() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(function1.this, "Animation stopped", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void run(){
        while(progress<650&&stopped){
            ++progress;
            progressBar1.setProgress(progress);
            try {
                thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        gifImageView.stopAnimation();
        if(progress == 650)
            progress = 0;
    }

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