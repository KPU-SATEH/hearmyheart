package com.example.hearmyheart;

/**
 * Created by Administrator on 2017-03-20.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-01-09.
 */
public class SendPostWord extends AsyncTask<Void, Void, String> {

    String order;
    String idx;
    String word;

    String order2;
    String idx2;
    String word2;

    String order3;
    String idx3;
    String word3;


    public SendPostWord(String idx,String idx2,String idx3,String order,String order2,String order3,String word,String word2,String word3)
    {
        this.idx = idx;
        this.order = order;
        this.word = word;

        this.idx2 = idx2;
        this.order2 = order2;
        this.word2 = word2;

        this.idx3 = idx3;
        this.order3 = order3;
        this.word3 = word3;
    }

    protected String doInBackground(Void... unused) {
        String content = executeClient();
        return content;
    }

    protected void onPostExecute(String result) {
        // 모두 작업을 마치고 실행할 일 (메소드 등등)
    }

    // 실제 전송하는 부분
    public String executeClient() {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();

        post.add(new BasicNameValuePair("idx", idx));
        post.add(new BasicNameValuePair("order",order));
        post.add(new BasicNameValuePair("word",word));

        post.add(new BasicNameValuePair("idx2", idx2));
        post.add(new BasicNameValuePair("order2",order2));
        post.add(new BasicNameValuePair("word2",word2));

        post.add(new BasicNameValuePair("idx3", idx3));
        post.add(new BasicNameValuePair("order3",order3));
        post.add(new BasicNameValuePair("word3",word3));


        Log.i("idx1 ", upload.idx1);
        Log.i("idx2 ", upload2.idx2);
        Log.i("idx3 ", upload3.idx3);

        Log.i("order1 ", upload.order1);
        Log.i("order2 ", upload2.order2);
        Log.i("order3 ", upload3.order3);

        Log.i("word1 ", upload.word1);
        Log.i("word2 ", upload2.word2);
        Log.i("word3 ", upload3.word3);


        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://13.112.211.84/noticeboard/add_word3.php");

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
            httpPost.setEntity(entity);
            client.execute(httpPost);
            return EntityUtils.getContentCharSet(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}