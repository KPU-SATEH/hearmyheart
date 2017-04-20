package com.example.hearmyheart;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-01-09.
 */
public class view extends AppCompatActivity {

    Button good_btn;
    Button bad_btn;
    int good ;
    int bad ;
    int idx;
    String password;
    EditText password_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);

        good_btn = (Button)findViewById(R.id.good_btn);
        bad_btn = (Button)findViewById(R.id.bad_btn);


        password_text = (EditText)findViewById(R.id.password_text);


        Intent intent = getIntent();
        idx = intent.getExtras().getInt("idx");
        int hit = intent.getExtras().getInt("hit");
        good = intent.getExtras().getInt("good");
        bad = intent.getExtras().getInt("bad");
        password = intent.getStringExtra("password");

        Log.i("password2 : ", password);

        good_btn.setOnClickListener(myClickListener);
        bad_btn.setOnClickListener(myClickListener);


        //조회수 늘리기 쓰레드
        new SendPostView(Integer.toString(hit), Integer.toString(idx)).execute();

    }


    /////////백버튼 이벤트 처리 함수//////////////////
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent intent = new Intent(getApplicationContext(), noticeboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

                break;
        }

        return true;
    }

    //버튼 이벤트 처리
    View.OnClickListener myClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.good_btn: // good 버튼을 눌렀을때
                    good = good + 1;
                    new SendPostGood(Integer.toString(good), Integer.toString(idx)).execute();
                    Log.i("good", Integer.toString(good));
                    break;


                case R.id.bad_btn: // bad버튼을 눌렀을때
                    bad = bad + 1;
                    new SendPostBad(Integer.toString(bad), Integer.toString(idx)).execute();
                    break;


            }
        }
    };
}