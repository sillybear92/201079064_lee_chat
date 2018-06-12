package com.example.bbik.a201079064_lee_chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


//채팅방 구현
public class Chat_Room extends AppCompatActivity {
    private Button btn_send_msg;
    private EditText input_msg;
    private String user_name, room_name;
    private DatabaseReference ref_room;
    private String temp_key;
    private ChattingAdapter arrayAdapter; // list_of_message를 Textview 형태로 담아 하나의 row 로 표현_리스트 아이템.
    private ArrayList<Chatting> list_of_message = new ArrayList<>();  // Chatting 클래스로 이루어진 ArraryList-[String (id, message)]
    private ListView listView; // 채팅 내용(arrayAdapter :  Textview 형태의 채팅 row)의 집합_리스트뷰.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        btn_send_msg = (Button)findViewById(R.id.btn_send); // xml에서 ui 연결.
        input_msg = (EditText)findViewById(R.id.msg_input);
        listView=(ListView)findViewById(R.id.msg_List);
        arrayAdapter=new ChattingAdapter(this,list_of_message);
        listView.setAdapter(arrayAdapter);



        user_name = getIntent().getExtras().get("user_name").toString(); // Wait_Room.class의 Intent Extras : 유저 이름 및 방 제목 정보 받아옴.
        room_name = getIntent().getExtras().get("room_name").toString();
        setTitle(" 채팅방 - "+room_name);
        ref_room = FirebaseDatabase.getInstance().getReference().child("chat_room").child(room_name); // 내 Firebase DB의 "chat_room" 테이블에서
                                                                                                        // 하위 테이블 중 현재 방이름의 테이블을 reference로 설정.

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                 // 보내기 버튼의 온클릭.
                Map<String,Object> map = new HashMap<String, Object>();     // String, Object 형태로 받을 Map 생성.
                temp_key = ref_room.push().getKey();            // 현재 reference에서 임의의 테이블 생성(.push()-임의의 문자열로 이름 생성됨) 후 그 이름을 temp_key에 저장.
                ref_room.updateChildren(map);                   // 현 reference 갱신.

                DatabaseReference message_root = ref_room.child(temp_key); // 위에서 생성한 임의의 테이블을 message_root - reference로 설정.
                Map<String,Object> map2 = new HashMap<String, Object>(); // Map 생성.
                map2.put("name",user_name);         // Map에 Data 저장. key: "name", value: user_name;
                map2.put("msg",input_msg.getText().toString()); // Map에 Data 저장. key: "msg", value: EditText에 작성된 문자열

                message_root.updateChildren(map2); // DB에 Data 저장 및 갱신. Map Array 형태로 {name:user_name}, {msg:input_msg} 저장.
                input_msg.setText(""); // EditText 초기화

            }
        });


        ref_room.addChildEventListener(new ChildEventListener() {   // Firebase DB에 데이터 수신이 일어났을 때 발생되는 EventListener.
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                append_chat_conversation(dataSnapshot);
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



    private void append_chat_conversation(DataSnapshot dataSnapshot) { // 채팅방 뷰 -> DB 안에 저장되어있는 메시지를 가져와 뿌리는 함수
        Iterator i =dataSnapshot.getChildren().iterator();          // datasnapshot: parent Reference인 ref_room의 하위 데이터를 스캔 -> Iterator에 담아둠.
        Chatting chat = new Chatting();                             // Chatting [String id , String msg];
        while (i.hasNext()){                                        // i.hasNext: iterator i 에서 읽어올 데이터가 없을때 까지 (단위: 테이블)
            chat.setMsg((String)((DataSnapshot)i.next()).getValue()); // i.next() = 현재 요소의 다음 요소를 읽음.
            chat.setId((String)((DataSnapshot)i.next()).getValue());  // 그 후 Value 값을 String 타입으로 얻음.
        }
        if(user_name.equals(chat.getId())){                         // 만약 DB에서 가져온 메시지와 현재 작성자의 ID가 같으면 xml로 구현한 오른쪽 TextView로 뿌림
            chat.setRes(R.layout.message_right);
        }else{
            chat.setRes(R.layout.message_left);                     // 다른 사용자면 왼쪽 TextView로 뿌림.
        }
        list_of_message.add(chat);                                  // 현 메시지를 메시지 Array에 추가.
        arrayAdapter.notifyDataSetChanged();
    }
}

