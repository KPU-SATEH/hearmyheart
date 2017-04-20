package com.example.hearmyheart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    Button fnc1_btn,fnc2_btn,fnc3_btn,fnc4_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fnc1_btn = (Button) findViewById(R.id.fnc1_btn);
        fnc2_btn = (Button) findViewById(R.id.fnc2_btn);
        fnc3_btn = (Button) findViewById(R.id.fnc3_btn);
        fnc4_btn = (Button) findViewById(R.id.fnc4_btn);

        fnc1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //자모음연습
                Intent intent = new Intent(getApplicationContext(),Practice.class);
                startActivity(intent);
            }
        });
        fnc2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //단어연습
                Intent intent = new Intent(getApplicationContext(),BeforeWp.class);
                intent.putExtra("flag",2);
                startActivity(intent);
            }
        });
        fnc3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //게시판
                Intent intent = new Intent(getApplicationContext(),function3.class);
                startActivity(intent);
            }
        });
        fnc4_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //사용법
                Intent intent = new Intent(getApplicationContext(),ManualActivity.class);
                startActivity(intent);
            }
        });
    }
/*
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
                String title = c.getString(TAG_TITLE);



                String category = c.getString(TAG_CATEGORY);
                String view = c.getString(TAG_VIEW);
                String date = c.getString(TAG_DATE);
                String password = c.getString(TAG_PASSWORD);
                String bad = c.getString(TAG_BAD);
                String good = c.getString(TAG_GOOD);
                bbs = new HashMap<String, String>();





                idx_list.add(idx); // 키값은 따로 저장
                title_list.add(title);



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

            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    */
}