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
 * Created by Owner on 2017-03-28.
 */

public class WordPractice extends AppCompatActivity implements Serializable { //단어연습
    String myJSON;

    boolean gender;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_IDX = "idx";
    private static final String TAG_WORD = "word";

    JSONArray board = null;

    ArrayList<String> idx_list; // 키값 게시글을 구분하기 위하여 따로 저장
    ArrayList<String> word_list;
    int num=0;


    Button wordzip1,wordzip2,wordzip3,wordzip4,wordzip5,wordzip6,
            wordzip7,wordzip8,wordzip9,wordzip10,wordzip11,wordzip12,wordzip13,
            wordzip14,home;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordpractice);
        gender=getIntent().getExtras().getBoolean("gender");

        idx_list = new ArrayList<>();
        word_list = new ArrayList<>();
        wordzip1 = (Button) findViewById(R.id.wordzip1);
        wordzip2 = (Button) findViewById(R.id.wordzip2);
        wordzip3 = (Button) findViewById(R.id.wordzip3);
        wordzip4 = (Button) findViewById(R.id.wordzip4);
        wordzip5 = (Button) findViewById(R.id.wordzip5);
        wordzip6 = (Button) findViewById(R.id.wordzip6);
        wordzip7 = (Button) findViewById(R.id.wordzip7);
        wordzip8 = (Button) findViewById(R.id.wordzip8);
        wordzip9 = (Button) findViewById(R.id.wordzip9);
        wordzip10 = (Button) findViewById(R.id.wordzip10);
        wordzip11 = (Button) findViewById(R.id.wordzip11);
        wordzip12 = (Button) findViewById(R.id.wordzip12);
        wordzip13 = (Button) findViewById(R.id.wordzip13);
        wordzip14 = (Button) findViewById(R.id.wordzip14);
        home=(Button)findViewById(R.id.home);

        wordzip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=1;
                getData("http://13.112.211.84/noticeboard/g.php");
            }
        });
        wordzip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=2;
                getData("http://13.112.211.84/noticeboard/n.php");
            }
        });
        wordzip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=3;
                getData("http://13.112.211.84/noticeboard/d.php");
            }
        });
        wordzip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=4;
                getData("http://13.112.211.84/noticeboard/r.php");
            }
        });
        wordzip5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=5;
                getData("http://13.112.211.84/noticeboard/m.php");
            }
        });
        wordzip6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=6;
                getData("http://13.112.211.84/noticeboard/b.php");
            }
        });
        wordzip7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=7;
                getData("http://13.112.211.84/noticeboard/s.php");
            }
        });
        wordzip8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=8;
                getData("http://13.112.211.84/noticeboard/o.php");
            }
        });
        wordzip9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=9;
                getData("http://13.112.211.84/noticeboard/z.php");
            }
        });
        wordzip10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=10;
                getData("http://13.112.211.84/noticeboard/ch.php");
            }
        });
        wordzip11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=11;
                getData("http://13.112.211.84/noticeboard/k.php");
            }
        });
        wordzip12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=12;
                getData("http://13.112.211.84/noticeboard/t.php");
            }
        });
        wordzip13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=13;
                getData("http://13.112.211.84/noticeboard/p.php");
            }
        });
        wordzip14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=14;
                getData("http://13.112.211.84/noticeboard/h.php");
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }

            protected void onPostExecute(String result) {
                if(gender){
                    myJSON = result;
                    showList();
                    Intent intent = new Intent(getApplicationContext(), Man_Practice.class);
                    intent.putExtra("word_flag", num);
                    intent.putExtra("idx", idx_list);
                    intent.putExtra("word", word_list);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);

                }else if(!gender){
                    myJSON = result;
                    showList();
                    Intent intent = new Intent(getApplicationContext(), Woman_Practice.class);
                    intent.putExtra("word_flag", num);
                    intent.putExtra("idx", idx_list);
                    intent.putExtra("word", word_list);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);

                }
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);

    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            board = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < board.length(); i++) // 갯수만큼 리스트에 저장
            {
                JSONObject c = board.getJSONObject(i);
                String idx = c.getString(TAG_IDX);
                String title = c.getString(TAG_WORD);

                idx_list.add(idx); // 키값은 따로 저장
                word_list.add(title);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}