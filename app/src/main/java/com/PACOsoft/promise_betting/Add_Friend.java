package com.PACOsoft.promise_betting;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Add_Friend extends Dialog {
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
}