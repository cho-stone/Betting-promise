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

public class Location_List_Adapter extends RecyclerView.Adapter<Location_List_Adapter.CustomViewHolder> {

    private ArrayList<Location> arrayList;
    private Context context;

    public Location_List_Adapter(ArrayList<Location> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    //뷰홀더를 새로 만들 때마다  onCreateViewHolder 호출
    //onCreateViewHolder = 뷰홀더, 뷰홀더와 연결된 뷰 생성
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    //뷰홀더를 데이터와 연결할 때 onBindViewHolder 호출
    //데이터 가져와서 뷰 홀더의 레이아웃을 채움
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.tv_Search_location_Title.setText(arrayList.get(position).getTitle());
        holder.tv_Search_location_Category.setText(arrayList.get(position).getCategory());
        holder.tv_Search_RoadAddress.setText(arrayList.get(position).getRoadaddress());

    }

    @Override
    //데이터 세트 크기를 가져올 때 getItemCount 호출
    public int getItemCount() {
        //arrayList가 null인지 아닌지 판별
        //null이 아니면 arrayList.size()가져오고 null이면 0
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Search_location_Title;
        TextView tv_Search_location_Category;
        TextView tv_Search_RoadAddress;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_Search_location_Title = itemView.findViewById(R.id.tv_Search_location_Title);
            this.tv_Search_location_Category = itemView.findViewById(R.id.tv_Search_location_Category);
            this.tv_Search_RoadAddress = itemView.findViewById(R.id.tv_Search_RoadAddress);
        }
    }
}

