package com.PACOsoft.promise_betting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Search_Local extends AppCompatActivity {
    private EditText search_word;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Location> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_local);
        recyclerView = findViewById(R.id.SearchLocationRecyclerview); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();// Location 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)


        search_word = findViewById(R.id.et_search_local);
        search_word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Thread thread = new Make_Local_JSON_Thread();
                thread.start();
                //todo thread에 값 edittext 값을 넘겨주고 새로운 값을 받아와서 arraylist에 넣고 어뎁터에 연결해서 리사이클러뷰에 그린다
//                adapter = new Location_List_Adapter(arrayList, getApplicationContext());
//                recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    //지우기
    public void btn_search_local_cancel(View v) {
        search_word.setText("");
    }
}