package com.example.administrator.function3_2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Spinner post_category;
    EditText post_password;
    EditText post_title;
    EditText post_content_edt;
    Button add_post_btn;
    Button cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        post_category = (Spinner)findViewById(R.id.post_category);

        //스피너 생성성
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        post_category.setAdapter(adapter);

        post_password = (EditText)findViewById(R.id.post_password);
        post_title = (EditText)findViewById(R.id.post_title);
        post_content_edt = (EditText)findViewById(R.id.post_content_edt);
        add_post_btn = (Button)findViewById(R.id.add_post_btn);
        cancel_btn = (Button)findViewById(R.id.cancel_btn);

        add_post_btn.setOnClickListener(myClickListener);
        cancel_btn.setOnClickListener(myClickListener);


    }


    //버튼 이벤트 처리
    View.OnClickListener myClickListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {

            switch(v.getId()) // 글등록 버튼을 눌렀을때
            {
                case R.id.add_post_btn :

                    if(post_category.getSelectedItem().toString().equals("선택하세요")&&    //비번,제목,카테고리가 비어있으면
                            post_password.equals(null)&&
                            post_title.equals(null)) {
                        Toast.makeText(getApplicationContext(),"모든 항목을 입력해 주세요",Toast.LENGTH_SHORT).show();
                    }
                    else {   // 서버에다가 비번,제목,카테고리 내용이 보내진다.
                        new SendPost( post_category.getSelectedItem().toString() , post_password.getText().toString() , post_title.getText().toString() ).execute();
                    }

                    break;


                case R.id.cancel_btn: // 취소버튼을 눌렀을때
                    finish();
                    break;
            }

        }
    };


}
