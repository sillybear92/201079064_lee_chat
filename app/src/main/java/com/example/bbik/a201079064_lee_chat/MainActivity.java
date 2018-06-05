package com.example.bbik.a201079064_lee_chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class MainActivity extends AppCompatActivity {

    private Button send_button;
    private EditText room_name;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_message= new ArrayList<>();

    private String name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send_button=(Button)findViewById(R.id.SEND_Button);
        room_name=(EditText)findViewById(R.id.MSG_Text);
        listView=(ListView)findViewById(R.id.MSG_List);

        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list_of_message);
        listView.setAdapter(arrayAdapter);

        request_user_name();

        send_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put(room_name.getText().toString(),"");
                root.updateChildren(map);
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while(i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_message.clear();
                list_of_message.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Chat_Room.class);
                intent.putExtra("room_name",((TextView)view).getText().toString());
                intent.putExtra("user_name",name);
                startActivity(intent);

            }
        });
    }

    // 사용자 이름 생성.
    private void request_user_name(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //알람창 객체 생성.
        builder.setTitle("사용하실 이름을 입력하세요.");  // 사용자 생성 안내 문구 출력.

        final EditText input_filed= new EditText(this); // 사용자 이름 기입란 생성.
        builder.setView(input_filed);  // 알람창 뷰와 입력란 연결.
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {  // 입력 완료시 발생 이벤트.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input_filed.getText().toString();            // 입력란 텍스트 get.
            }
        });

        builder.setNegativeButton("랜덤이름 사용", new DialogInterface.OnClickListener(){   // 입력 취소시 발생 이벤트.
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Random random = new Random();
                name = "사용자_" + random.nextInt(99999);
            }
        });

        builder.show();
    }
}
