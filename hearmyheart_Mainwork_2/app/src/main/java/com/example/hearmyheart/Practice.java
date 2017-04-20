package com.example.hearmyheart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 린린 on 2017-03-27.
 */

public class Practice extends AppCompatActivity implements Serializable {
    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_IDX = "idx";
    private static final String TAG_WORD = "word";
    private static final String TAG_PRONUNCIATION = "pronunciation";

    JSONArray board = null;

    ArrayList<String> idx_list; // 키값 게시글을 구분하기 위하여 따로 저장
    ArrayList<String> word_list;
    ArrayList<String> pronunciation_list;

    boolean flag;


    Button consonant,vowel,home;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        idx_list = new ArrayList<>();
        word_list = new ArrayList<>();
        pronunciation_list = new ArrayList<>();
        consonant = (Button) findViewById(R.id.Consonant);
        vowel = (Button) findViewById(R.id.vowel);
        home = (Button) findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        consonant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                getData("http://13.112.211.84/noticeboard/charC.php");
            }
        });
        vowel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = false;
                getData("http://13.112.211.84/noticeboard/charV.php");
            }

        });
    }

    public void getData(String url)
    {
        class GetDataJSON extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!=null)
                    {
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();
                }
                catch (Exception e)
                {
                    return null;
                }
            }

            protected void onPostExecute(String result)
            {
                myJSON = result;
                showList();
                Intent intent = new Intent(getApplicationContext(), function1.class);
                intent.putExtra("idx", idx_list);
                intent.putExtra("word", word_list);
                intent.putExtra("flag", flag);
                intent.putExtra("pronunciation",pronunciation_list);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);

    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            board = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < board.length(); i++) // 갯수만큼 리스트에 저장
            {
                JSONObject c = board.getJSONObject(i);
                String idx = c.getString(TAG_IDX);
                String word = c.getString(TAG_WORD);
                String pronunciation = c.getString(TAG_PRONUNCIATION);

                idx_list.add(idx); // 키값은 따로 저장
                word_list.add(word);
                pronunciation_list.add(pronunciation);
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }
}