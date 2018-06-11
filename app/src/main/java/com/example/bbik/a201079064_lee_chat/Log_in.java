package com.example.bbik.a201079064_lee_chat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Log_in extends AppCompatActivity {
    private Button bt_login;
    private Button bt_signup;
    private EditText input_id;
    private EditText input_passwd;

    UserData userdata = new UserData();
    private String sign_id;
    private String sign_pw;


    private DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("user");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        bt_login=(Button)findViewById(R.id.bt_login);
        bt_signup=(Button)findViewById(R.id.bt_signup);
        input_id=(EditText)findViewById(R.id.input_id);
        input_passwd=(EditText)findViewById(R.id.input_passwd);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdata.id= input_id.getText().toString();
                userdata.pw=input_passwd.getText().toString();
                users.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Iterator i = dataSnapshot.getChildren().iterator();
                        String db_id;
                        String db_pw;
                        while(i.hasNext()){
                            db_id=((DataSnapshot)i.next()).getValue().toString();
                            if(db_id.equals(userdata.id)){
                                db_pw=((DataSnapshot)i.next()).getValue().toString();
                                if(db_pw.equals(userdata.pw)){
                                    Intent intent = new Intent(getApplicationContext(),Wait_Room.class);
                                    intent.putExtra("id",userdata.id);//로그인
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(),"비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT);
                                    return;
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"아이디가 존재하지 않습니다.",Toast.LENGTH_SHORT);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up_page();
            }
        });


    }

    // 사용자 이름 생성.
    private void sign_up_page(){

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout signup_page=(RelativeLayout)vi.inflate(R.layout.sign_up_page,null);


        final EditText input_signid=(EditText)signup_page.findViewById(R.id.input_signid);
        final EditText input_signpw=(EditText)signup_page.findViewById(R.id.input_signpw);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);  //알람창 객체 생성.
        builder.setTitle("사용자 등록");  // 사용자 생성 타이틀.

        builder.setView(signup_page);  // 알람창 뷰와 입력란 연결.
        builder.setPositiveButton("등록",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                sign_id = input_signid.getText().toString();
                sign_pw = input_signpw.getText().toString();
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int bool = 0;
                        Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                        while(i.hasNext()){
                            if(i.next().getKey().equals(sign_id)){
                                Toast.makeText(getApplicationContext(),"이미 존재하는 아이디입니다. ",Toast.LENGTH_SHORT);
                                bool=1;
                            }
                        }
                        if(bool==0 && sign_id!=""&& sign_pw != "") {
                            Map<String,Object> map = new HashMap<String,Object>();
                            Map<String,Object> ran = new HashMap<String, Object>();
                            String temp_key=users.push().getKey();
                            map.put("id",sign_id);
                            map.put("pw",sign_pw);
                            DatabaseReference user = users.child(temp_key);
                            user.updateChildren(map);
                            Toast.makeText(getApplicationContext(), "등록완료", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialog.cancel();
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

