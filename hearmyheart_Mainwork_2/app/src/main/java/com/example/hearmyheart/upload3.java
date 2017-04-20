package com.example.hearmyheart;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Administrator on 2017-03-19.
 */
public class upload3 extends AppCompatActivity {
    private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;

    Button myButton;
    Button next_btn;
    Button finish_btn;


    EditText word;
    SurfaceHolder surfaceHolder;
    boolean recording;

    int idx;

    static String idx3;
    static String order3="3";
    static String word3;

    AudioFromVideo mAudioFromVideo;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recording = false;

        setContentView(R.layout.upload);

        //Get Camera for preview
        myCamera = getCameraInstance();
        if(myCamera == null){
            Toast.makeText(upload3.this,
                    "Fail to get Camera",
                    Toast.LENGTH_LONG).show();
        }

        myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
        FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
        myCameraPreview.addView(myCameraSurfaceView);

        myButton = (Button)findViewById(R.id.mybutton);
        next_btn = (Button)findViewById(R.id.next_btn2);
        finish_btn = (Button)findViewById(R.id.finish_btn);

        word = (EditText)findViewById(R.id.word);

        myButton.setOnClickListener(myButtonOnClickListener);
        next_btn.setOnClickListener(myClickListener);
        finish_btn.setOnClickListener(myClickListener);

        next_btn.setText("완료");

/*
        Intent intent = getIntent();
        idx = intent.getExtras().getInt("idx");
        Log.i("idx_three", Integer.toString(idx));*/

        idx = upload2.idx;
        idx3 = Integer.toString(idx);
    }


    // 버튼 이벤트 처리
    View.OnClickListener myClickListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {

            switch(v.getId()) // 글등록 버튼을 눌렀을때
            {
                case R.id.next_btn2 :

                    if(word.getText().toString().equals(null)) {
                        Toast.makeText(getApplicationContext(),"단어를 입력해 주세요",Toast.LENGTH_SHORT).show();
                    }
                    else if(recording==false) {
                        Toast.makeText(getApplicationContext(),"동영상 촬영을 해주세요",Toast.LENGTH_SHORT).show();
                    }
                    else { /////////////////////////////버튼처리수정///////////////////////////////////////////////

                        //Toast.makeText(getApplicationContext(),"끝",Toast.LENGTH_SHORT).show();
                        word3 = word.getText().toString();

                        File f1 = new File("/mnt/sdcard/"+idx3+"-3"+".pcm"); // The location of your PCM file
                        File f2 = new File("/mnt/sdcard/"+idx3+"-3"+".wav"); // The location where you want your WAV file
                        try {
                            rawToWave(f1, f2);
                            Log.i("sucess!","");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        /*
                        Log.i("idx1 ",upload.idx1);
                        Log.i("idx2 ",upload2.idx2);
                        Log.i("idx3 ",upload3.idx3);

                        Log.i("order1 ",upload.order1);
                        Log.i("order2 ",upload2.order2);
                        Log.i("order3 ",upload3.order3);

                        Log.i("word1 ",upload.word1);
                        Log.i("word2 ",upload2.word2);
                        Log.i("word3 ",upload3.word3);
                        */



                        UploadVideoTask3 videoTask = new UploadVideoTask3();
                        videoTask.execute("/mnt/sdcard/"+idx3+"-3"+".mp4");

                        UploadVideoTask3 voiceTask = new UploadVideoTask3();
                        voiceTask.execute("/mnt/sdcard/" + idx3 + "-3" + ".wav");


                        new SendPostWord(upload.idx1, upload2.idx2, upload3.idx3, upload.order1, upload2.order2, upload3.order3, upload.word1, upload2.word2, upload3.word3).execute();

                        Intent intent = new Intent(getApplicationContext(), function3.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                        Toast.makeText(getApplication(),"글이 등록되었습니다.",Toast.LENGTH_SHORT).show();

                    }

                    break;

                case R.id.finish_btn :
                    finish();
                    break;



            }

        }
    };

    Button.OnClickListener myButtonOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            //Release Camera before MediaRecorder start
            releaseCamera();

            if(!prepareMediaRecorder()){
                Toast.makeText(upload3.this,
                        "Fail in prepareMediaRecorder()!\n - Ended -",
                        Toast.LENGTH_LONG).show();
                //  finish();
            }

            mediaRecorder.start();
            recording = true;

            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                Toast.makeText(getBaseContext(), "시작", Toast.LENGTH_SHORT).show();
                                Thread.sleep(5000);
                                myButton.setText("STOP");
                                mediaRecorder.stop();  // stop the recording
                                releaseMediaRecorder(); // release the MediaRecorder object
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }
            }).start();

            /////mp4->pcm////
            final String video ="/mnt/sdcard/"+idx3+"-3"+".mp4";
            final String audio = "/mnt/sdcard/"+idx3+"-3"+".pcm";


            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mAudioFromVideo = new AudioFromVideo(video, audio);
                            mAudioFromVideo.start();
                        }
                    });

                }
            }).start();
                /*mAudioFromVideo = new AudioFromVideo(video,audio);
                mAudioFromVideo.start();*/




        }};

    private Camera getCameraInstance(){
// TODO Auto-generated method stub
        Camera c = null;

        int cameraId = findFrontSideCamera();
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
            c.setDisplayOrientation(0);
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private boolean prepareMediaRecorder(){
        myCamera = getCameraInstance();
        mediaRecorder = new MediaRecorder();

        myCamera.unlock();
        mediaRecorder.setCamera(myCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));


        mediaRecorder.setOutputFile("/sdcard/"+idx3+"-3"+".mp4"); //동영상 저장 경로 및 이름 지정
        mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(5000000); // Set max file size 5M

        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (myCamera != null){
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }

    public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

        private SurfaceHolder mHolder;
        private Camera mCamera;

        public MyCameraSurfaceView(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int weight,
                                   int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // make any resize, rotate or reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }
    }


    /////////전면 카메라 사용 함수/////////
    private int findFrontSideCamera() {
        int cameraId = -1;

        int numberOfCameras = Camera.getNumberOfCameras();



        for (int i = 0; i < numberOfCameras; i++) {

            Camera.CameraInfo cmInfo = new Camera.CameraInfo();

            Camera.getCameraInfo(i, cmInfo);



            if (cmInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {

                cameraId = i;

                break;

            }

        }



        return cameraId;

    }

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
            writeInt(output, 100000); // sample rate
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
}