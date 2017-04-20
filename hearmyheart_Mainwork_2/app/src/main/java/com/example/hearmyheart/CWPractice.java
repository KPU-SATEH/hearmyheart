package com.example.hearmyheart;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Owner on 2017-03-30.
 */

public class CWPractice extends Activity implements Runnable , MediaRecorder.OnInfoListener{
    //boolean spectro = false;

    Intent i;
    SpeechRecognizer mRecognizer;
    WebView wb2;

    // 음성 서버로 전송
    int serverResponseCode = 0;

    String upLoadServerUri = null;

    final String uploadFilePath = "/sdcard/";//경로를 모르겠으면, 갤러리 어플리케이션 가서 메뉴->상세 정보
    final String uploadFileName = "record.acc"; //전송하고자하는 파일 이름

    MediaPlayer mPlayer = null;
    MediaRecorder mRecorder = null;
    String mFilePath;
    Boolean wordFlag;

    //프로그래스바
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
    ArrayList<String> category;
    int num;
    byte[][] gif;
    Button home;

    ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        wordFlag = getIntent().getExtras().getBoolean("flag");
        idx = (ArrayList<String>) getIntent().getSerializableExtra("idx");
        title = (ArrayList<String>) getIntent().getSerializableExtra("title");
        category = (ArrayList<String>) getIntent().getSerializableExtra("category");
        num = Integer.parseInt(idx.get(0));
        gif = new byte[30][];

        home=(Button)findViewById(R.id.home);
        mFilePath = uploadFilePath + uploadFileName;

        /************* Php script path ****************/
        upLoadServerUri = "http://52.78.155.88/test5.php";//서버컴퓨터의 ip주소


        getGif("http://13.112.211.84/");


        setContentView(R.layout.activity_cwpractice);



        wb2 = (WebView) findViewById(R.id.wb2);
        wb2.getSettings().setJavaScriptEnabled(true);
        wb2.setWebChromeClient(new CustomWebChromeClient());
        wb2.setInitialScale(100);

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
        progressBar2.setProgress(0);
        tv.setText(title.get(0));

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopped = false;
                progressBar1.setProgress(0);
                progressBar2.setProgress(0);
                progress = 0;
                if(num > 0) {
                    num--;
                    tv.setText(title.get(num));
                    gifImageView.setBytes(gif[num]);
                    //wb.loadUrl("http://52.78.155.88/spectrogram/harin.html");
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopped = false;
                progressBar1.setProgress(0);
                progressBar2.setProgress(0);
                progress = 0;
                if(num < idx.size()-1) {
                    num++;
                    tv.setText(title.get(num));
                    gifImageView.setBytes(gif[num]);
                    //wb.loadUrl("http://52.78.155.88/spectrogram/harin.html");
                }
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
                    progressBar2.setProgress(progress);
                    gifImageView.stopAnimation();
                } else {
                    onBtnRecord();
                    stopped = true;
                    progressBar1.setProgress(progress);
                    progressBar2.setProgress(progress);
                    gifImageView.startAnimation();
                    thread = new Thread(CWPractice.this);
                    thread.start();


                }
            }
        });

        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gifImageView.isAnimating()) {
                    stopped = false;
                    progressBar1.setProgress(progress);
                    progressBar2.setProgress(progress);
                    gifImageView.stopAnimation();
                } else {
                    onBtnRecord();
                    stopped = true;
                    progressBar1.setProgress(progress);
                    progressBar2.setProgress(progress);
                    gifImageView.startAnimation();
                    thread = new Thread(CWPractice.this);
                    thread.start();


                }
            }
        });


        gifImageView.setOnAnimationStop(new GifImageView.OnAnimationStop() {
            @Override public void onAnimationStop() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(CWPractice.this, "Animation stopped", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public void run(){
        while(progress<200&&stopped){
            ++progress;
            progressBar1.setProgress(progress);
            progressBar2.setProgress(progress);
            try {
                thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        gifImageView.stopAnimation();
        if(progress == 200)
            progress = 0;

        try {
            thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        uploadFile(mFilePath);

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

    public void STT() {
        class MyAsyncTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

                mRecognizer = SpeechRecognizer.createSpeechRecognizer(CWPractice.this);
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(i);

                return null;
            }

            protected void onPreExecute() {
            }


            private RecognitionListener listener = new RecognitionListener() {

                @Override
                public void onReadyForSpeech(Bundle params) {
                }

                @Override
                public void onBeginningOfSpeech() {
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                }

                @Override
                public void onEndOfSpeech() {
                }

                @Override
                public void onError(int error) {
                }

                @Override
                public void onResults(Bundle results) {
                    ///
                    String key = "AIzaSyCCmI3w7J2ixllazk6NU-vMJmtuG4ULmYo";
                    key = SpeechRecognizer.RESULTS_RECOGNITION;
                    ArrayList<String> mResult = results.getStringArrayList(key);
                    String[] rs = new String[mResult.size()];
                    mResult.toArray(rs);
                    if (rs[0].equals(title.get(0))) {
                        Toast.makeText(getApplicationContext(),
                                "정답입니다!",
                                Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(),
                                "오답입니다!",
                                Toast.LENGTH_SHORT).show();
                    mRecognizer.startListening(i);
                    ///


                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                }
            };
        }
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }



    public void getGif(String URL)
    {
        class GifDataDownloader extends AsyncTask<String, String, byte[]> {
            @Override protected byte[] doInBackground(final String... params) {
                for(int i = 0 ; i < idx.size(); i++) {
                    if(wordFlag) {
                        String gifUrl = params[0] + "test" + idx.get(i) + ".gif";
                        gif[i] = ByteArrayHttpClient.get(gifUrl);
                    }
                    else {
                        String gifUrl = params[0] + "side" + idx.get(i) + ".gif";
                        gif[i] = ByteArrayHttpClient.get(gifUrl);
                    }
                }

                return null;
            }
            protected void onPostExecute(final byte[] bytes) {

                Toast.makeText(CWPractice.this, "complete", Toast.LENGTH_SHORT).show();
            }
        }
        GifDataDownloader gifDown = new GifDataDownloader();
        gifDown.execute(URL);
    }


    public int uploadFile(String sourceFileUri) {

        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            runOnUiThread(new Runnable() {
                public void run() {
                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {
                            wb2.loadUrl("http://52.78.155.88/spectrogram/d.html");
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });
            }
            return serverResponseCode;

        } // End else block

    }

    public void onBtnRecord() {
        if( mRecorder != null ) {
            mRecorder.release();
            mRecorder = null;
        }
        mRecorder = new MediaRecorder();
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        mRecorder.setMaxDuration(2000);
        mRecorder.setMaxFileSize(5 * 1000 * 1000);
        mRecorder.setOnInfoListener(this);

        try {
            mRecorder.prepare();
        } catch(IOException e) {
            Log.d("tag", "Record Prepare error");
        }
        mRecorder.start();
    }
    public void onBtnStop() {
        mRecorder.stop();
        mRecorder.release();

    }

    public void onBtnPlay() {
        if( mPlayer != null ) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(mFilePath);
            mPlayer.prepare();
        } catch(IOException e) {
            Log.d("tag", "Audio Play error");
            return;
        }
        mPlayer.start();
    }
    public void onInfo(MediaRecorder mr, int what, int extra) {
        switch( what ) {
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED :
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED :
                onBtnStop();
                break;
        }
    }

    public void onClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity .class);
        startActivity(intent);
    }
}
