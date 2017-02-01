package com.example.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private CustomDialog mCustomDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickView(View v){
        switch (v.getId()) {
            case R.id.bt_main:
                mCustomDialog = new CustomDialog(this,
                        "어제 술을 너무 마셔서",
                        "피곤해 죽겠네요",
                        leftClickListener,
                        rightClickListener);
                mCustomDialog.show();
                break;
        }
    }

    private View.OnClickListener leftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "왼쪽버튼 Click!!",
                    Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "오른쪽버튼 Click!!",
                    Toast.LENGTH_SHORT).show();
        }
    };
}