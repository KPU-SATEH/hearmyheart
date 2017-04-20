package com.example.hearmyheart;

import android.os.AsyncTask;

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
 * Created by Administrator on 2017-01-06.
 */
class SendPost extends AsyncTask<Void, Void, String> {

    String category,password,title,videoPath,voicePath,gender;

    public SendPost(String category , String password, String title,String videoPath,String voicePath,String gender)
    {
        this.category = category;
        this.password = password;
        this.title = title;
        this.videoPath = videoPath;
        this.voicePath = voicePath;
        this.gender = gender;
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
        post.add(new BasicNameValuePair("category", category));
        post.add(new BasicNameValuePair("password",password));
        post.add(new BasicNameValuePair("title",title));
        post.add(new BasicNameValuePair("videoPath",videoPath));
        post.add(new BasicNameValuePair("voicePath",voicePath));
        post.add(new BasicNameValuePair("gender",gender));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://13.112.211.84/noticeboard/post.php");

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