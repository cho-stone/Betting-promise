package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;


    public CustomAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);//서버로부터 이미지 받아와서 item에 삽입
        holder.tv_id.setText(arrayList.get(position).getId());//서버로부터 텍스트 받아와서 item에 삽입
        holder.tv_pw.setText(arrayList.get(position).getPw());
        holder.tv_userName.setText(arrayList.get(position).getUserName());

    }

    @Override
    public int getItemCount() {
        //arrayList가 null인지 아닌지 판별
        //null이 아니면 arrayList.size()가져오고 null이면 0
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        TextView tv_id;
        TextView tv_pw;
        TextView tv_userName;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_id = itemView.findViewById(R.id.tv_id);
            this.tv_pw = itemView.findViewById(R.id.tv_pw);
            this.tv_userName = itemView.findViewById(R.id.tv_userName);
        }
    }
}

