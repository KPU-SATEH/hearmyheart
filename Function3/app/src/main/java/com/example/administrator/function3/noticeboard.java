package com.example.administrator.function3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017-01-09.
 */
public class noticeboard extends AppCompatActivity {
    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_IDX = "idx";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CATEGORY ="category";
    private static final String TAG_VIEW ="view";
    private static final String TAG_DATE ="date";
    private static final String TAG_PASSWORD ="password";
    private static final String TAG_GOOD ="good";
    private static final String TAG_BAD ="bad";

    JSONArray board = null;
    ArrayList<HashMap<String, String>> boardList; // 게시판 내용을 담을 해쉬맵 배열리스트

    ArrayList<String> idx_list; // 키값 게시글을 구분하기 위하여 따로 저장
    ArrayList<String> password_list; // 패스워드값 저장

    ListView list; // 화면에 띄울 리스트뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        list = (ListView)findViewById(R.id.listView);
        idx_list = new ArrayList<>();
        password_list = new ArrayList<>();
        boardList = new ArrayList<HashMap<String,String>>();
        getData("http://192.168.0.108/noticeboard/noticeboard.php"); // 서버 주소

    }

    /////////////웹에서 데이터를 받아오는 함수 - php와 json 이용 ////////////////////
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

    /////////// 안드로이드 화면에 게시판 목록화를 띄우는 함수 //////////////////
    protected void showList(){
        try{

            JSONObject jsonObj = new JSONObject(myJSON);
            board = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0; i<board.length(); i++) // 갯수만큼 리스트에 저장
            {
                JSONObject c = board.getJSONObject(i);
                String idx = c.getString(TAG_IDX);
                String title = c.getString(TAG_TITLE);
                String category = c.getString(TAG_CATEGORY);
                String view = c.getString(TAG_VIEW);
                String date = c.getString(TAG_DATE);
                String password =c.getString(TAG_PASSWORD);
                String bad =c.getString(TAG_BAD);
                String good =c.getString(TAG_GOOD);



                HashMap<String,String> bbs = new HashMap<String,String>();


                idx_list.add(idx); // 키값은 따로 저장
                password_list.add(password); // 패스워드 값 따로 저장

                //나머지는 해시맵에 담고
                bbs.put(TAG_TITLE, title);
                bbs.put(TAG_CATEGORY, category);
                bbs.put(TAG_VIEW, view);
                bbs.put(TAG_DATE,date);
                bbs.put(TAG_BAD,bad);
                bbs.put(TAG_GOOD,good);

                //해시맵을 배열리스트에 저장한다
                boardList.add(bbs);
            }

            // 리스트 목록화 하기
            ListAdapter adapter = new SimpleAdapter(
                    noticeboard.this,boardList,R.layout.list_item,
                    new String[]{TAG_TITLE,TAG_CATEGORY,TAG_VIEW,TAG_DATE,TAG_GOOD,TAG_BAD},
                    new int[]{R.id.tt,R.id.category,R.id.view,R.id.date,R.id.good,R.id.bad}
            );

            list.setAdapter(adapter);

            //리스트 이벤트 처리
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String p = Integer.toString(position);
                    Toast.makeText(getBaseContext(), p, Toast.LENGTH_SHORT).show();

                    //인덱스 값
                    int idx = Integer.parseInt(idx_list.get(position));
                    //조회수 값
                    HashMap<String,String> Hit  = (HashMap<String,String>)boardList.get(position);
                    int hit = Integer.parseInt(Hit.get(TAG_VIEW))+1;
                    //good 값
                    HashMap<String,String> Good  = (HashMap<String,String>)boardList.get(position);
                    int good = Integer.parseInt(Good.get(TAG_GOOD));
                    //bad 값
                    HashMap<String,String> Bad  = (HashMap<String,String>)boardList.get(position);
                    int bad = Integer.parseInt(Bad.get(TAG_GOOD));


                    Log.i("hit : ", Integer.toString(hit));



                    Intent intent = new Intent(getApplicationContext(),view.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("idx", idx);
                    intent.putExtra("hit", hit);
                    intent.putExtra("good",good);
                    intent.putExtra("bad",bad);
                    intent.putExtra("password",password_list.get(position));
                    startActivity(intent);

                }
            });

        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    /////////백버튼 이벤트 처리 함수//////////////////
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

                break;
        }

        return true;
    }
}
