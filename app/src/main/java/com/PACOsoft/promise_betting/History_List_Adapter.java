package com.PACOsoft.promise_betting;

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

public class History_List_Adapter extends RecyclerView.Adapter<History_List_Adapter.CustomViewHolder> {
    private ArrayList<History> arrayList;
    private Context context;

    public History_List_Adapter(ArrayList<History> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    //뷰홀더를 새로 만들 때마다  onCreateViewHolder 호출
    //onCreateViewHolder = 뷰홀더, 뷰홀더와 연결된 뷰 생성
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    //뷰홀더를 데이터와 연결할 때 onBindViewHolder 호출
    //데이터 가져와서 뷰 홀더의 레이아웃을 채움
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (arrayList.get(position).getPrize() == 1) {
            Glide.with(holder.itemView).load(R.drawable.first_prize_icon).into(holder.iv_prizeIcon);//서버로부터 prize가져와서 1등이면 first_prize_icon을 iv_prizeIcon으로 넘겨준다
        } else if (arrayList.get(position).getPrize() == 2) {
            Glide.with(holder.itemView).load(R.drawable.second_prize_icon).into(holder.iv_prizeIcon);//서버로부터 prize가져와서 2등이면 second_prize_icon을 iv_prizeIcon으로 넘겨준다

        } else if (arrayList.get(position).getPrize() == 3) {
            Glide.with(holder.itemView).load(R.drawable.third_prize_icon).into(holder.iv_prizeIcon);//서버로부터 prize가져와서 3등이면 third_prize_icon을 iv_prizeIcon으로 넘겨준다
        }
        holder.tv_promiseName.setText(arrayList.get(position).getPromiseName());//서버로부터 텍스트 받아와서 item에 삽입
        if (arrayList.get(position).getPrizeMoney() >= 0) {//약속 상금
            holder.tv_prizeMoney.setText("+" + String.valueOf(arrayList.get(position).getPrizeMoney()));
        }
        holder.tv_data.setText(arrayList.get(position).getDate());
    }

    @Override
    //데이터 세트 크기를 가져올 때 getItemCount 호출
    public int getItemCount() {
        //arrayList가 null인지 아닌지 판별
        //null이 아니면 arrayList.size()가져오고 null이면 0
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tv_promiseName;
        ImageView iv_prizeIcon;
        TextView tv_prizeMoney;
        TextView tv_data;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_promiseName = itemView.findViewById(R.id.tv_promiseName);
            this.iv_prizeIcon = itemView.findViewById(R.id.iv_prizeIcon);
            this.tv_prizeMoney = itemView.findViewById(R.id.tv_prizeMoney);
            this.tv_data = itemView.findViewById(R.id.tv_data);
        }
    }
}

