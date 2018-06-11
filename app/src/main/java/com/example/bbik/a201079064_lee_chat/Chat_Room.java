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

public class Chat_Room extends AppCompatActivity {
    private Button btn_send_msg;
    private EditText input_msg;
    private String user_name, room_name;
    private DatabaseReference ref_room;
    private String temp_key;
    private ChattingAdapter arrayAdapter;
    private ArrayList<Chatting> list_of_message = new ArrayList<>();
    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        btn_send_msg = (Button)findViewById(R.id.btn_send);
        input_msg = (EditText)findViewById(R.id.msg_input);
        listView=(ListView)findViewById(R.id.msg_List);
        arrayAdapter=new ChattingAdapter(this,list_of_message);
        listView.setAdapter(arrayAdapter);



        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        setTitle(" 채팅방 - "+room_name);
        ref_room = FirebaseDatabase.getInstance().getReference().child("chat_room").child(room_name);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = ref_room.push().getKey();
                ref_room.updateChildren(map);

                DatabaseReference message_root = ref_room.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("msg",input_msg.getText().toString());

                message_root.updateChildren(map2);
                input_msg.setText("");

            }
        });


        ref_room.addChildEventListener(new ChildEventListener() {
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



    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator i =dataSnapshot.getChildren().iterator();
        Chatting chat = new Chatting();
        while (i.hasNext()){
            chat.setMsg((String)((DataSnapshot)i.next()).getValue());
            chat.setId((String)((DataSnapshot)i.next()).getValue());
        }
        if(user_name.equals(chat.getId())){
            chat.setRes(R.layout.message_right);
        }else{
            chat.setRes(R.layout.message_left);
        }
        list_of_message.add(chat);
        arrayAdapter.notifyDataSetChanged();
    }
}

