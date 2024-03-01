package com.PACOsoft.promise_betting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.Promise;
import com.PACOsoft.promise_betting.obj.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Promise_List_Adapter extends RecyclerView.Adapter<Promise_List_Adapter.CustomViewHolder> {

    private ArrayList<Promise> arrayList;
    private Context context;

    public Promise_List_Adapter(ArrayList<Promise> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    //뷰홀더를 새로 만들 때마다  onCreateViewHolder 호출
    //onCreateViewHolder = 뷰홀더, 뷰홀더와 연결된 뷰 생성
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promise_list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    //뷰홀더를 데이터와 연결할 때 onBindViewHolder 호출
    //데이터 가져와서 뷰 홀더의 레이아웃을 채움
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.tv_promiseKey.setText(arrayList.get(position).getPromiseKey().toString());
        holder.tv_promiseName.setText(arrayList.get(position).getPromiseName().toString());
        holder.tv_numOfPlayer.setText(String.valueOf(arrayList.get(position).getNumOfPlayer()));//서버로부터 텍스트 받아와서 item에 삽입
        holder.tv_bettingMoney.setText(String.valueOf(arrayList.get(position).getbettingMoney()));
    }

    @Override
    //데이터 세트 크기를 가져올 때 getItemCount 호출
    public int getItemCount() {
        //arrayList가 null인지 아닌지 판별
        //null이 아니면 arrayList.size()가져오고 null이면 0
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_promiseKey;
        TextView tv_promiseName;
        TextView tv_numOfPlayer;
        TextView tv_bettingMoney;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_promiseKey = itemView.findViewById(R.id.tv_promiseKey);
            this.tv_promiseName = itemView.findViewById(R.id.tv_promiseName);
            this.tv_numOfPlayer = itemView.findViewById(R.id.tv_numOfPlayer);
            this.tv_bettingMoney = itemView.findViewById(R.id.tv_bettingMoney);
        }
    }
}

