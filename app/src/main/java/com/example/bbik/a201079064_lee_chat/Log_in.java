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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// 로그인 화면 구현- 메인화면
// 테스트용 아이디 { test, test } 또는 { root, 1234 }
// git : https://github.com/sillybear92/201079064_lee_chat
public class Log_in extends AppCompatActivity {
    private Button bt_login;            // "로그인" 버튼
    private Button bt_signup;           // "사용자등록" 버튼
    private EditText input_id;          // 메인 "로그인" 뷰의 입력 id
    private EditText input_passwd;      // 메인 "로그인" 뷰의 입력 패스워드.

    UserData userdata = new UserData();  // UserData.class [String id, String pw] 객체생성.
    private String sign_id;             // "사용자등록" 뷰의 입력 id
    private String sign_pw;             // "사용자등록" 뷰의 입력 패스워드.


    private DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("user"); // 내 Firebase DB의 "user" 테이블을 현재 Reference로 지정.



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setTitle("로그인");

        bt_login=(Button)findViewById(R.id.bt_login);            // 구현한 xml의 UI와 연결.
        bt_signup=(Button)findViewById(R.id.bt_signup);
        input_id=(EditText)findViewById(R.id.input_id);
        input_passwd=(EditText)findViewById(R.id.input_passwd);

        // "로그인" 버튼 온 클릭.
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdata.id = input_id.getText().toString();            // 생성한 객체 userdata.set id,pw.
                userdata.pw = input_passwd.getText().toString();
                users.addChildEventListener(new ChildEventListener() {  // 현 Reference의 DB에 데이터 수신이 일어나면 발생하는 이벤트리스너.
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {  // 데이터 추가 이벤트 발생시.
                        Iterator i = dataSnapshot.getChildren().iterator(); // 현 Reference "user" 테이블의 하위 데이터들을 iterator에 담음.
                        String db_id;       // "user" -> id
                        String db_pw;       // "user" -> pw
                        while(i.hasNext()){
                            db_id=((DataSnapshot)i.next()).getValue().toString(); // i.next()로 읽어들인 데이터(db에 저장된 id)를 읽어옴. 루프문에 따라 하나의 테이블("철수"->{철수, 1234})에서 찾는다.
                            if(db_id.equals(userdata.id)){                          // db 에서 읽어온 id 와 입력된 id 가 같으면
                                db_pw=((DataSnapshot)i.next()).getValue().toString(); // i.next()로 읽어들인 데이터(db에 저장된 id)를 읽어옴.
                                if(db_pw.equals(userdata.pw)){                          // db 에서 읽어온 pw 와 입력된 pw 가 같으면
                                    Intent intent = new Intent(getApplicationContext(),Wait_Room.class);    // Intent 생성 -> "대기방" 뷰
                                    intent.putExtra("id",userdata.id);          // Intent시 넘겨줄 Data 생성 (사용자의 ID)
                                    startActivity(intent);              // 생성된 Intent View 실행
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
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { // 데이터 수정 이벤트 발생시.

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { // 데이터 삭제 이벤트 발생시.

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { // 데이터 이동 이벤트 발생시.

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { // 취소.

                    }
                });
            }
        });

        bt_signup.setOnClickListener(new View.OnClickListener() {       // "사용자등록" 버튼 온 클릭.
            @Override
            public void onClick(View v) {
                sign_up_page();
            }
        });


    }

    // 사용자 등록 페이지.
    private void sign_up_page(){

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);         // 구현한 sign_up_page.xml UI를
        final RelativeLayout signup_page=(RelativeLayout)vi.inflate(R.layout.sign_up_page,null);    // 알림창에 객체로 넣기 위해 inflate 설정.


        final EditText input_signid=(EditText)signup_page.findViewById(R.id.input_signid);  // "사용자 ID" 입력란.
        final EditText input_signpw=(EditText)signup_page.findViewById(R.id.input_signpw);  // "사용자 PW" 입력란.

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);  //알람창 객체 생성.
        builder.setTitle("사용자 등록");  // 사용자 생성 타이틀.

        builder.setView(signup_page);  // 알람창 뷰와 입력란 연결.
        builder.setPositiveButton("등록",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {    // 알림창의 "등록" 버튼 온 클릭.
                sign_id = input_signid.getText().toString();        // "사용자 ID" 입력란의 문자열 저장.
                sign_pw = input_signpw.getText().toString();        // "사용자 PW" 입력란의 문자열 저장.
                users.addListenerForSingleValueEvent(new ValueEventListener() { // "user" DB에 데이터 수신(Single Value)이 일어나면 밣생하는 이벤트
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int bool = 0;   // 이미 존재하는 ID 쳬크용
                        Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();  // 현 "user" Reference DB의 하위 데이터를 iterator에 담아둠.
                        while(i.hasNext()){                 // 다음 데이터 테이블을 읽어옴. ( 테이블이 있으면 루프문 반복, 없으면 루프문 종료)
                            if(i.next().getKey().equals(sign_id)){      // i.next().getKey() : 읽어온 테이블의 다음 데이터요소의 key(={id: 철수})와 등록하려는 사용자 ID가 같을 경우.
                                Toast.makeText(getApplicationContext(),"이미 존재하는 아이디입니다. ",Toast.LENGTH_SHORT);
                                bool=1; // 1 : 이미 존재 함.
                            }
                        }
                        if(bool==0 && sign_id!=""&& sign_pw != "") { // 사용자 등록 ID 와 PW 입력란이 공백이 아님 And 이미 존재하지 않는 ID의 경우.
                            Map<String,Object> map = new HashMap<String,Object>(); // ID와 PW 를 담을 Map 생성.
                            String temp_key=users.push().getKey();  // "user" DB에 push()-> 임의의 문자열의 테이블 생성. getKey()-> 생성한 임의의 문자열 받아옴
                            map.put("id",sign_id); //{ id : 철수 }
                            map.put("pw",sign_pw); //{ pw : 1234 }
                            DatabaseReference user = users.child(temp_key); // 현 "user" Reference 테이블 밑에 temp_key(임의의 문자열) 테이블 생성
                            user.updateChildren(map);  // 생성한 user 테이블에 map [{id:철수}, {pw:1234}] 갱신.
                            Toast.makeText(getApplicationContext(), "등록완료", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialog.cancel(); // 알림창 종료
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




