package com.example.bbik.a201079064_lee_chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//Custom Array Adapter 구현 - 내용은 기본 가이드 대로 작성 , getView 메소드만 참고하여 구현.
public class ChattingAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Chatting> arrayList;

    public ChattingAdapter(Context context, ArrayList<Chatting>arrayList){
        this.mContext=context;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;            // ViewHolder.class [TextView id, TextView msg]
        Chatting chatting=arrayList.get(position);

        holder=new ViewHolder();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(chatting.getRes(),parent,false);     // inflate를 사용하여 데이터 하나하나를 View 형태로 담아 뿌리는 데에
                                                                                        // 부담이 없도록 해준다.

        convertView.setTag(holder);                 // 동적으로 텍스트뷰 생성을 위해 Tag 설정.

        holder.id=(TextView)convertView.findViewById(R.id.msg_id);
        holder.msg=(TextView)convertView.findViewById(R.id.msg_msg);

        holder.id.setText(chatting.getId());
        holder.msg.setText(chatting.getMsg());

        return convertView;
    }
}
