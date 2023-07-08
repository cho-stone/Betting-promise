package com.example.myapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Add_Friend extends Dialog {
    private TextView txt_contents;
    private Button shutdownClick;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    User_List_Adapter adapter;

    public Add_Friend(@NonNull Context context, ArrayList<User> arrayList) {
        super(context);
        setContentView(R.layout.activity_add_friend);
        recyclerView = findViewById(R.id.addFriendsRecyclerview); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(context);//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);

        adapter = new User_List_Adapter(arrayList, context);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

    }

    public void btn_add_friend(View view) {//친구 추가 버튼

    }

    public void btn_add_friend_cancel (View view){//취소 버튼

    }
}