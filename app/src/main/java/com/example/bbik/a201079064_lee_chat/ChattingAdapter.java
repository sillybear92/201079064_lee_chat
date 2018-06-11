package com.example.bbik.a201079064_lee_chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
        final ViewHolder holder;
        Chatting chatting=arrayList.get(position);

        holder=new ViewHolder();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(chatting.getRes(),parent,false);

        convertView.setTag(holder);

        holder.id=(TextView)convertView.findViewById(R.id.msg_id);
        holder.msg=(TextView)convertView.findViewById(R.id.msg_msg);

        holder.id.setText(chatting.getId());
        holder.msg.setText(chatting.getMsg());

        return convertView;
    }
}
