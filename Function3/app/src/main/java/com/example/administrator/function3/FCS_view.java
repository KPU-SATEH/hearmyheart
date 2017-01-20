package com.example.administrator.function3;

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
 * Created by Administrator on 2017-01-16.
 */
public class FCS_view extends AppCompatActivity {

    Button good_btn;
    Button bad_btn;
    Button delete_btn;
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
        delete_btn = (Button)findViewById(R.id.delete);

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
        delete_btn.setOnClickListener(myClickListener);

        //조회수 늘리기 쓰레드
        new SendPostView(Integer.toString(hit), Integer.toString(idx)).execute();

    }


    /////////백버튼 이벤트 처리 함수//////////////////
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent intent = new Intent(getApplicationContext(), FCS_noticeboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

                break;
        }

        return true;
    }

    //버튼 이벤트 처리
    View.OnClickListener myClickListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {

            switch(v.getId())
            {
                case R.id.good_btn : // good 버튼을 눌렀을때
                    good = good+1;
                    new SendPostGood(Integer.toString(good),Integer.toString(idx)).execute();
                    break;


                case R.id.bad_btn: // bad버튼을 눌렀을때
                    bad = bad +1;
                    new SendPostBad(Integer.toString(bad),Integer.toString(idx)).execute();
                    break;

                case R.id.delete: //삭제 버튼을 눌렀을때

                    Context mContext = getApplicationContext();
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.custom_dialog,(ViewGroup) findViewById(R.id.layout_root));

                    password_text = (EditText)layout.findViewById(R.id.password_text);

                    final AlertDialog.Builder aDialog = new AlertDialog.Builder(FCS_view.this);
                    aDialog.setTitle("비밀번호를 입력해 주세요");
                    aDialog.setView(layout);

                    aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if(password.equals(password_text.getText().toString()))
                            {
                                new SendDelete(Integer.toString(idx)).execute();
                                Toast.makeText(getApplicationContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(),FCS_noticeboard.class);
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




                    break;
            }

        }
    };

}
