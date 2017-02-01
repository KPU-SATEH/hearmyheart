package com.felipecsl.gifimageview.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private static final String TAG = "MainActivity";
  private GifImageView gifImageView;
  private Button btnToggle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    gifImageView = (GifImageView) findViewById(R.id.gifImageView);
    btnToggle = (Button) findViewById(R.id.btnToggle);

    gifImageView.setOnAnimationStop(new GifImageView.OnAnimationStop() {
      @Override public void onAnimationStop() {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            Toast.makeText(MainActivity.this, "Animation stopped", Toast.LENGTH_SHORT).show();
          }
        });
      }
    });

    btnToggle.setOnClickListener(this);

    new GifDataDownloader() {
      @Override protected void onPostExecute(final byte[] bytes) {
        gifImageView.setBytes(bytes);
        gifImageView.startAnimation();
        Log.d(TAG, "GIF width is " + gifImageView.getGifWidth());
        Log.d(TAG, "GIF height is " + gifImageView.getGifHeight());
      }
    }.execute("http://13.112.155.106/test.gif");
  }

  @Override public void onClick(final View v) {
    if (v.equals(btnToggle)) {
      if (gifImageView.isAnimating())
        gifImageView.stopAnimation();
      else
        gifImageView.startAnimation();
    }
  }
}
