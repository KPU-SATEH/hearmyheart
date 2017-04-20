package com.example.hearmyheart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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

public class db_upload extends AppCompatActivity {

    Spinner post_category;
    EditText post_password;
    EditText post_title;
    EditText post_content_edt;
    Button next1_btn;
    Button cancel_btn;

    String videoPath;
    String voicePath;

    RadioButton woman;
    RadioButton man;
    String gender="남";

    int IDX;

    private static final String TAG_RESULTS="result";
    private static final String TAG_IDX = "idx";

    JSONArray board = null;
    ArrayList<HashMap<String, String>> boardList; // 게시판 내용을 담을 해쉬맵 배열리스트

    ArrayList<String> idx_list; // 키값 게시글을 구분하기 위하여 따로 저장
    ArrayList<String> password_list; // 패스워드값 저장

    String myJSON;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_upload);

        post_category = (Spinner)findViewById(R.id.post_category);

        //스피너 생성성
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        post_category.setAdapter(adapter);

        post_password = (EditText)findViewById(R.id.post_password);
        post_title = (EditText)findViewById(R.id.post_title);
        next1_btn = (Button)findViewById(R.id.next1_btn);
        cancel_btn = (Button)findViewById(R.id.cancel_btn);

        videoPath = "";
        voicePath = "";

        next1_btn.setOnClickListener(myClickListener);
        cancel_btn.setOnClickListener(myClickListener);

        idx_list = new ArrayList<>();
        password_list = new ArrayList<>();
        boardList = new ArrayList<HashMap<String,String>>();

        woman = (RadioButton)findViewById(R.id.woman);
        man = (RadioButton)findViewById(R.id.man);


        woman.setOnClickListener(optionOnClickListener);
        man.setOnClickListener(optionOnClickListener);

        man.setChecked(true);
        //getData("http://192.168.0.75/noticeboard/noticeboard.php");
        getData("http://13.112.211.84/noticeboard/noticeboard.php");
    }

    RadioButton.OnClickListener optionOnClickListener
            = new RadioButton.OnClickListener() {

        public void onClick(View v) {

            if(woman.isChecked())
            {
                gender = "여";
                Log.i("여자 : ",gender);
            }
            else
            {
                gender="남";
                Log.i("남자 : ",gender);
            }

        }
    };
    //버튼 이벤트 처리
    View.OnClickListener myClickListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {



            switch(v.getId()) // 글등록 버튼을 눌렀을때
            {


                case R.id.next1_btn:

                    String title = post_title.getText().toString();
                    String password = post_password.getText().toString();
                    String spinner = post_category.getSelectedItem().toString();

                    Log.i("title", title);
                    Log.i("password", password);
                    Log.i("spiner", spinner);

                    if (spinner.equals("선택하세요")) {
                        Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else if (title.getBytes().length <= 0)
                    {
                        Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                     }
                     else if(password.getBytes().length<=0)
                    {
                        Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                     }
                    else {   // 서버에다가 비번,제목,카테고리 내용이 보내진다.

                        new SendPost( post_category.getSelectedItem().toString() , post_password.getText().toString() , post_title.getText().toString(),videoPath,voicePath,gender ).execute();

                        Intent intent = new Intent(db_upload.this,upload.class);
                        Log.i("미희미희",Integer.toString(IDX));
                        intent.putExtra("idx", IDX);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    }

                    break;


                case R.id.cancel_btn: // 취소버튼을 눌렀을때
                    finish();
                    break;
            }

        }
    };

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
                SaveIdx();
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);

    }

    /////////// idx값 얻어오는 함수 //////////////////
    protected void SaveIdx(){
        try {

            JSONObject jsonObj = new JSONObject(myJSON);
            board = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < board.length(); i++) // 갯수만큼 리스트에 저장
            {
                JSONObject c = board.getJSONObject(i);
                String idx = c.getString(TAG_IDX);


                idx_list.add(idx); // 키값은 따로 저장

                //해시맵을 배열리스트에 저장한다
            }
            getIdx();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void getIdx()
    {
        String sidx = idx_list.get(idx_list.size()-1);
        IDX = Integer.parseInt(sidx)+1;

        Log.i("idx_getInt()", Integer.toString(IDX));
    }


}