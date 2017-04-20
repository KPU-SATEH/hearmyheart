package com.example.hearmyheart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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

public class Ready extends AppCompatActivity {

    EditText password_text;

    TextView title;
    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_IDX = "idx";
    private static final String TAG_ORDER ="order";
    private static final String TAG_WORD ="word";

    JSONArray word = null;
    ArrayList<HashMap<String, String>> wordList; // 게시판 내용을 담을 해쉬맵 배열리스트


    static String word_[]=new String[3];
    int idx;
    int hit;
    int good,bad;
    String password;
    String s_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);

        Intent intent = getIntent();
        idx = intent.getExtras().getInt("idx");
        hit = intent.getExtras().getInt("hit");
        good = intent.getExtras().getInt("good");
        bad = intent.getExtras().getInt("bad");
        password = intent.getStringExtra("password");
        s_title = intent.getExtras().getString("s_title");


        title = (TextView)findViewById(R.id.title);
        title.setText(s_title);
        wordList = new ArrayList<HashMap<String,String>>();

        getData("http://13.112.211.84/word.php?idx="+Integer.toString(idx)); // 서버 주소
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
            word = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0; i<word.length(); i++) // 갯수만큼 리스트에 저장
            {
                JSONObject c = word.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String idx = c.getString(TAG_IDX);
                String order = c.getString(TAG_ORDER);
                String word = c.getString(TAG_WORD);


                HashMap<String,String> word_h = new HashMap<String,String>();


                //나머지는 해시맵에 담고
                word_h.put(TAG_ID, id);
                word_h.put(TAG_IDX, idx);
                word_h.put(TAG_ORDER, order);
                word_h.put(TAG_WORD,word);


                //해시맵을 배열리스트에 저장한다
                wordList.add(word_h);
            }


            HashMap<String,String> word1 = (HashMap<String,String>)wordList.get(0);
            word_[0] = word1.get(TAG_WORD);

            HashMap<String,String> word2 = (HashMap<String,String>)wordList.get(1);
            word_[1] = word2.get(TAG_WORD);

            HashMap<String,String> word3 = (HashMap<String,String>)wordList.get(2);
            word_[2] = word3.get(TAG_WORD);

        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }


    public void Ready(View view)
    {
        Log.i("idx2 : ", Integer.toString(idx));

        Intent intent = new Intent(getApplicationContext(),function2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("word",word_);
        intent.putExtra("idx", idx);
        intent.putExtra("hit", hit);
        intent.putExtra("good",good);
        intent.putExtra("bad",bad);
        intent.putExtra("password",password);
        startActivity(intent);

    }

    public void delete_list(View view) {
        Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_dialog,(ViewGroup) findViewById(R.id.layout_root));

        password_text = (EditText)layout.findViewById(R.id.password_text);

        final AlertDialog.Builder aDialog = new AlertDialog.Builder(Ready.this);
        aDialog.setTitle("비밀번호를 입력해 주세요");
        aDialog.setView(layout);

        aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if(password.equals(password_text.getText().toString()))
                {
                    new SendDelete(Integer.toString(idx)).execute();
                    Toast.makeText(getApplicationContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(),Best_noticeboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다. 다시 입력해 주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });
        aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = aDialog.create();
        ad.show();
    }
}