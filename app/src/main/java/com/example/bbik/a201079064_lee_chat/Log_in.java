package com.example.bbik.a201079064_lee_chat;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.Random;

public class Log_in extends AppCompatActivity {
    private Button bt_login;
    private Button bt_signup;
    private EditText input_id;
    private EditText input_passwd;

    private String user_id;
    private String user_pw;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.login);

        bt_login=(Button)findViewById(R.id.bt_login);
        bt_signup=(Button)findViewById(R.id.bt_signup);
        input_id=(EditText)findViewById(R.id.input_id);
        input_passwd=(EditText)findViewById(R.id.input_passwd);


    }

    // 사용자 이름 생성.
    private void sing_up_page(){
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout signup_page=(RelativeLayout)vi.inflate(R.layout.sign_up_page,null);

        final EditText input_signid=(EditText)signup_page.findViewById(R.id.input_signid);
        final EditText input_signpw=(EditText)signup_page.findViewById(R.id.input_signpw);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //알람창 객체 생성.
        builder.setTitle("사용자 등록");  // 사용자 생성 타이틀.

        builder.setView(signup_page);  // 알람창 뷰와 입력란 연결.
        builder.setPositiveButton("등록",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }


}
