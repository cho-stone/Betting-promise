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

public class User_List_Adapter extends RecyclerView.Adapter<User_List_Adapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;


    public User_List_Adapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    //뷰홀더를 새로 만들 때마다  onCreateViewHolder 호출
    //onCreateViewHolder = 뷰홀더, 뷰홀더와 연결된 뷰 생성
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);


        return holder;
    }

    @Override
    //뷰홀더를 데이터와 연결할 때 onBindViewHolder 호출
    //데이터 가져와서 뷰 홀더의 레이아웃을 채움
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);//서버로부터 이미지 받아와서 item에 삽입
        holder.tv_id.setText(arrayList.get(position).getId());//서버로부터 텍스트 받아와서 item에 삽입
        holder.tv_pw.setText(arrayList.get(position).getPw());
        holder.tv_userName.setText(arrayList.get(position).getUserName());

    }

    @Override
    //데이터 세트 크기를 가져올 때 getItemCount 호출
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

