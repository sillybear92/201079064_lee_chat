package com.example.bbik.a201079064_lee_chat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

// 대기방 구현
public class Wait_Room extends AppCompatActivity {

    private Button create_button;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_room= new ArrayList<>();  // 방 리스트

    private String name;
    private DatabaseReference chat_room = FirebaseDatabase.getInstance().getReference().child("chat_room");  // FireBase의 "chat_room" 테이블을 현 Reference 로 지정.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("대기방");
        create_button=(Button)findViewById(R.id.SEND_Button);   // 구현한 xml UI 와 연결.
        listView=(ListView)findViewById(R.id.room_List);
        name = getIntent().getExtras().get("id").toString();  // Log_in View에서 넘겨 받은 Intent Extra Data.
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list_of_room);
        listView.setAdapter(arrayAdapter);

        // "방 만들기" 버튼 온 클릭.
        create_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CreateRoom_Page();
            }
        });

        // chat_room reference DB 에 데이터 수신이 일어날 때 발생하는 이벤트 리스너.
        chat_room.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while(i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_room.clear();
                list_of_room.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // listView 아이템 클릭 시 발생하는 리스너
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Chat_Room.class);        // Chat_Room View 인텐트 생성.
                intent.putExtra("room_name",((TextView)view).getText().toString());   // 방 이름 정보 넘김.
                intent.putExtra("user_name",name);                                    // 사용자 이름 정보 넘김.
                startActivity(intent);

            }
        });
    }

    // "방 만들기" 페이지
    private void CreateRoom_Page(){
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout create_room=(RelativeLayout)vi.inflate(R.layout.create_room,null);
        final EditText input_createroom=(EditText)create_room.findViewById(R.id.input_createroom);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("방 생성");
        builder.setView(create_room);
        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put(input_createroom.getText().toString(),"");
                chat_room.updateChildren(map);
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
