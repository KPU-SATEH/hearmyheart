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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements Serializable {
    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_IDX = "idx";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CATEGORY ="category";
    private static final String TAG_VIEW ="view";
    private static final String TAG_DATE ="date";
    private static final String TAG_PASSWORD ="password";
    private static final String TAG_BAD ="bad";
    private static final String TAG_GOOD ="good";

    JSONArray board = null;
    ArrayList<HashMap<String, String>> boardList; // 게시판 내용을 담을 해쉬맵 배열리스트
    HashMap<String, String> bbs;

    ArrayList<String> idx_list; // 키값 게시글을 구분하기 위하여 따로 저장
    ArrayList<String> password_list; // 패스워드값 저장

    Button fnc1_btn,fnc2_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idx_list = new ArrayList<>();
        getData("http://13.112.113.155/noticeboard.php");

        fnc1_btn = (Button) findViewById(R.id.fnc1_btn);
        fnc2_btn = (Button) findViewById(R.id.fnc2_btn);

        fnc1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),function1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        fnc2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),function2.class);
                intent.putExtra("idx",idx_list);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
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
                /*
                String title = c.getString(TAG_TITLE);
                String category = c.getString(TAG_CATEGORY);
                String view = c.getString(TAG_VIEW);
                String date = c.getString(TAG_DATE);
                String password = c.getString(TAG_PASSWORD);
                String bad = c.getString(TAG_BAD);
                String good = c.getString(TAG_GOOD);
                bbs = new HashMap<String, String>();
                */

                idx_list.add(idx); // 키값은 따로 저장
               /*
               password_list.add(password); // 패스워드 값 따로 저장


                //나머지는 해시맵에 담고
                bbs.put(TAG_TITLE, title);
                bbs.put(TAG_CATEGORY, category);
                bbs.put(TAG_VIEW, view);
                bbs.put(TAG_DATE, date);
                bbs.put(TAG_BAD, bad);
                bbs.put(TAG_GOOD, good);

                //해시맵을 배열리스트에 저장한다
                boardList.add(bbs);
                num++;
                */
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }
}