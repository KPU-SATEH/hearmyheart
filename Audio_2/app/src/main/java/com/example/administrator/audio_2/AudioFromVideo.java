package com.example.administrator.audio_2;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-02-12.
 */
public class AudioFromVideo {
    private String audio, video;
    private MediaCodec mMediaCodec;
    private MediaExtractor mExtrator;
    private MediaFormat format;
    private String amime;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public AudioFromVideo(String srcVideo, String destAudio) {
        this.audio = destAudio;
        this.video = srcVideo;
        mExtrator = new MediaExtractor();
        init();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void init() {
        try {
            mExtrator.setDataSource(video);
            format = mExtrator.getTrackFormat(1);
            mExtrator.selectTrack(1);
            amime = format.getString(MediaFormat.KEY_MIME);
            mMediaCodec = MediaCodec.createDecoderByType(amime);
            mMediaCodec.configure(format, null, null, 0);
            mMediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new AudioService(mMediaCodec, mExtrator, audio).start();
    }

    private class AudioService extends Thread {
        private MediaCodec mMediaCodec;
        private MediaExtractor mMediaExtractor;
        private String destFile;

        AudioService(MediaCodec amc, MediaExtractor ame, String destFile) {
            mMediaCodec = amc;
            mMediaExtractor = ame;
            this.destFile = destFile;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public void run() {
            try {
                OutputStream os = new FileOutputStream(new File(destFile));
                ArrayList<Short> sample = new ArrayList<>();
                long count = 0;
                while (true) {
                        int inputIndex = mMediaCodec.dequeueInputBuffer(0);
                    if (inputIndex == -1) {
                        continue;
                    }
                    int sampleSize = mMediaExtractor.readSampleData(mMediaCodec.getInputBuffer(inputIndex), 0);
                    if (sampleSize == -1) break;
                    long presentationTime = mMediaExtractor.getSampleTime();
                    int flag = mMediaExtractor.getSampleFlags();
                    mMediaExtractor.advance();
                    mMediaCodec.queueInputBuffer(inputIndex, 0, sampleSize, presentationTime, flag);
                    MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                    int outputIndex = mMediaCodec.dequeueOutputBuffer(info, 0);
                    if (outputIndex >= 0) {
                        ByteBuffer bb = mMediaCodec.getOutputBuffer(outputIndex);
                        byte[] data = new byte[info.size];
                        bb.get(data);
                        count += data.length;

                        os.write(data);

                        Log.i("Sample Size", "" + sampleSize);
                        Log.i("write", "" + count);
                        Log.i("Output Index", "" + outputIndex);
                        Log.i("Presentation Time", "" + presentationTime);

                        mMediaCodec.releaseOutputBuffer(outputIndex, false);
                    } else if (outputIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {

                    }
                }
                Log.i("write", "done");
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}