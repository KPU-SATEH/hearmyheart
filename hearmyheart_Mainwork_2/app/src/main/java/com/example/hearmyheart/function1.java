package com.example.hearmyheart;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class function1 extends Activity implements Runnable , MediaRecorder.OnInfoListener{


    MediaPlayer mediaPlayer = null;
    Thread recordingThread;
    boolean isRecording = false;
    String path;

    int audioSource = MediaRecorder.AudioSource.MIC;
    int sampleRateInHz = 44100;
    int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);

    byte Data[] = new byte[bufferSizeInBytes];
    AudioRecord audioRecorder = null;


    Intent i;
    SpeechRecognizer mRecognizer;
    //WebView wb,wb2;

    // 음성 서버로 전송
    int serverResponseCode = 0;

    String upLoadServerUri = null;

    final String uploadFilePath = "/mnt/sdcard/";//경로를 모르겠으면, 갤러리 어플리케이션 가서 메뉴->상세 정보
    final String uploadFileName = "record.wav"; //전송하고자하는 파일 이름

    String mFilePath;
    String spec_path;

    //프로그래스바
    ProgressBar progressBar1,progressBar2;
    int progress=0;
    Thread thread;

    private static final String TAG = "MainActivity";
    private GifImageView gifImageView;
    private Button btnToggle;
    private Button btnTest;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean camera_flag = false;
    private boolean stopped = true;
    Button camera_btn,btn1,btn2;
    TextView tv;
    TextView txtSpeechInput;
    ArrayList<String> idx;
    ArrayList<String> word;
    ArrayList<String> pronunciation;
    boolean flag;
    int word_flag = 0;
    String url;
    int num;
    byte[][] gif;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        idx = (ArrayList<String>) getIntent().getSerializableExtra("idx");
        word = (ArrayList<String>) getIntent().getSerializableExtra("word");
        pronunciation=(ArrayList<String>)getIntent().getSerializableExtra("pronunciation");
        flag = getIntent().getExtras().getBoolean("flag");
        word_flag = getIntent().getExtras().getInt("word_flag");


        num = 0;
        gif = new byte[14][];
        mFilePath = uploadFilePath + uploadFileName;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        if(flag){
            path = "charC/";
        }
        else if(!flag){
            path = "charV/";
        }


        /************* Php script path ****************/
        upLoadServerUri = "http://52.78.155.88/test5.php";//서버컴퓨터의 ip주소

        setContentView(R.layout.activity_function1);
        /*
        wb = (WebView) findViewById(R.id.wb);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.setWebChromeClient(new CustomWebChromeClient());
        wb.setInitialScale(100);

        wb2 = (WebView) findViewById(R.id.wb2);
        wb2.getSettings().setJavaScriptEnabled(true);
        wb2.setWebChromeClient(new CustomWebChromeClient());
        wb2.setInitialScale(100);
        wb2.loadUrl("http://52.78.155.88/rec.html");
        */

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        btn1 = (Button) findViewById(R.id.prev_btn);
        btn2 = (Button) findViewById(R.id.next_btn);
        camera_btn = (Button) findViewById(R.id.button1);
        btnToggle = (Button) findViewById(R.id.btnToggle);
        btnTest = (Button) findViewById(R.id.test);

        txtSpeechInput=(TextView)findViewById(R.id.txtSpeechInput);
        tv = (TextView) findViewById(R.id.text);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceListener);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        progressBar1.setProgress(0);
        tv.setText(word.get(0));
        txtSpeechInput.setText(pronunciation.get(0));
        url = "http://52.78.155.88/guideline/" + path + "1.wav";
        getGif("http://13.112.211.84/gifData/");
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();

        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopped = false;
                progressBar1.setProgress(0);
                progressBar2.setProgress(0);
                progress = 0;
                if(num > 0) {
                    num--;
                    txtSpeechInput.setText(pronunciation.get(num));
                    tv.setText(word.get(num));
                    getGif("http://13.112.211.84/gifData/");
                    Log.i("qwer",Integer.toString(num+1));

                    url = "http://52.78.155.88/guideline/" + path + Integer.toString(num+1) + ".wav";
                    mediaPlayer = null;
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(url);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    txtSpeechInput.setText(pronunciation.get(num));
                    Log.i("qwer",pronunciation.get(num));
                    tv.setText(word.get(num));
                    getGif("http://13.112.211.84/gifData/");

                    url = "http://52.78.155.88/guideline/" + path + Integer.toString(num+1) + ".wav";
                    mediaPlayer = null;
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(url);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(camera_flag) {
                    surfaceView.setVisibility(View.INVISIBLE);
                    camera_flag = false;
                }
                else {
                    surfaceView.setVisibility(View.VISIBLE);
                    camera_flag = true;
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
                    stopped = true;
                    mediaPlayer.start();
                    progressBar1.setProgress(progress);
                    progressBar2.setProgress(progress);
                    gifImageView.startAnimation();
                    thread = new Thread(function1.this);
                    thread.start();
                }
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gifImageView.isAnimating()) {
                    stopped = false;
                    progressBar1.setProgress(progress);
                    progressBar2.setProgress(progress);
                    gifImageView.stopAnimation();
                } else {
                    stopped = true;
                    promptSpeechInput();
                    progressBar1.setProgress(progress);
                    progressBar2.setProgress(progress);
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

    /////////백버튼 이벤트 처리 함수//////////////////
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(getApplicationContext(), Practice.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }


    public void run(){
        while(progress<500&&stopped){
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
        stopRecording();
        gifImageView.stopAnimation();
        if(progress == 500)
            progress = 0;

        try {
            thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        save();
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

    public void getGif(String URL)
    {
        class GifDataDownloader extends AsyncTask<String, Void, byte[]> {
            @Override protected byte[] doInBackground(final String... params) {
                if (flag) {
                    String gifUrl = params[0] + "charC/" + Integer.toString(num+1)+ ".gif";
                    Log.i("asdf",Integer.toString(num+1));
                    spec_path = "charC";
                    gif[num] = ByteArrayHttpClient.get(gifUrl);
                } else if((!flag)) {
                    String gifUrl = params[0] + "charV/" +Integer.toString(num+1)+ ".gif";
                    Log.i("asdf",Integer.toString(num+1));
                    spec_path = "charV";
                    gif[num] = ByteArrayHttpClient.get(gifUrl);
                }

                return null;
            }
            protected void onPostExecute(final byte[] bytes) {
                Toast.makeText(function1.this, "complete", Toast.LENGTH_SHORT).show();
                gifImageView.setBytes(gif[num]);
                //wb. loadUrl("http://52.78.155.88/index.html?path="+spec_path+"&idx="+Integer.toString(num+1));
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
                            //wb2.loadUrl("http://52.78.155.88/rec.html");
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

    public void onInfo(MediaRecorder mr, int what, int extra) {
        switch( what ) {
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED :
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED :
                break;
        }
    }

    public void onClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }





    ////////////////////////////
    public void startRecording() {
        audioRecorder = new AudioRecord(audioSource,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                bufferSizeInBytes);

        audioRecorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                String filepath = Environment.getExternalStorageDirectory().getPath();
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(filepath+"/record.pcm");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                while(isRecording) {
                    audioRecorder.read(Data, 0, Data.length);
                    try {
                        os.write(Data, 0, bufferSizeInBytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        recordingThread.start();
    }

    public void stopRecording() {
        if (null != audioRecorder) {
            isRecording = false;
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
            recordingThread = null;
        }
    }

    private void promptSpeechInput() {
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(i);
      /*  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }*/
    }

    /**
     * Receiving speech input
     * */
  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                }
                break;
            }

        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/


    private RecognitionListener listener = new RecognitionListener() {

        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(),
                    "말 하세요!",
                    Toast.LENGTH_SHORT).show();
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
            mRecognizer.stopListening();
        }

        @Override
        public void onError(int error) {
        }

        @Override
        public void onResults(Bundle results) {
            ///
            String key= "AIzaSyCCmI3w7J2ixllazk6NU-vMJmtuG4ULmYo";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            txtSpeechInput.setText(""+rs[0]);

            if(rs[0].equals(pronunciation.get(num))){
                Toast.makeText(getApplicationContext(),
                        "정답입니다!",
                        Toast.LENGTH_SHORT).show();
                mRecognizer.stopListening();
            }
            if(!rs[0].equals(pronunciation.get(num))){
                Toast.makeText(getApplicationContext(),
                        "오답입니다!",
                        Toast.LENGTH_SHORT).show();
                mRecognizer.stopListening();
            }

            // mRecognizer.startListening(i);
            ///


        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }
    };


    ///////////////////////////여기서부터는 pcm->wav 파일 변환하는 함수 /////////////////////////////////
    private void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, 46300); // sample rate
            writeInt(output, Constants.RECORDER_SAMPLERATE * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }

            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }
    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }

    public void save() {
        File f1 = new File("/mnt/sdcard/record.pcm"); // The location of your PCM file
        File f2 = new File("/mnt/sdcard/record.wav"); // The location where you want your WAV file
        try {
            rawToWave(f1, f2);
            Log.i("sucess!", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}