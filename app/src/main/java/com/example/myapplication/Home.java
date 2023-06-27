package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    ArrayList<PhoneBook> phoneBookList;                     //객체 담을 리스트
    LayoutInflater layoutInflater;
    LinearLayout container;
    View view;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;                                                  //메인엑티비티 컨텍스트

        phoneBookList = new ArrayList<>();
        PhoneBook phoneBook1 = new PhoneBook(R.drawable.user_icon, "일하영", "010-0000-0000");
        PhoneBook phoneBook2 = new PhoneBook(R.drawable.user_icon, "이하영", "010-1111-1111");
        PhoneBook phoneBook3 = new PhoneBook(R.drawable.user_icon, "삼하영", "010-2222-2222");
        PhoneBook phoneBook4 = new PhoneBook(R.drawable.user_icon, "사하영", "010-3333-3333");
        PhoneBook phoneBook5 = new PhoneBook(R.drawable.user_icon, "오하영", "010-4444-4444");
        PhoneBook phoneBook6 = new PhoneBook(R.drawable.user_icon, "육하영", "010-4444-4444");
        PhoneBook phoneBook7 = new PhoneBook(R.drawable.user_icon, "칠하영", "010-4444-4444");
        PhoneBook phoneBook8 = new PhoneBook(R.drawable.user_icon, "팔하영", "010-4444-4444");
        PhoneBook phoneBook9 = new PhoneBook(R.drawable.user_icon, "구하영", "010-4444-4444");

        phoneBookList.add(phoneBook1);
        phoneBookList.add(phoneBook2);
        phoneBookList.add(phoneBook3);
        phoneBookList.add(phoneBook4);
        phoneBookList.add(phoneBook5);
        phoneBookList.add(phoneBook6);
        phoneBookList.add(phoneBook7);
        phoneBookList.add(phoneBook8);
        phoneBookList.add(phoneBook9);

        container = findViewById(R.id.container);
        layoutInflater = LayoutInflater.from(this);  //그릴곳

        for (int i = 0; i < (phoneBookList.size()); i++) {
            view = layoutInflater.inflate(R.layout.activity_home_dynamic_layout, null, false);      //넣을 레이아웃 뷰에 넣음
            //사진
            ImageView imageView = view.findViewById(R.id.item_image);
            imageView.setImageResource(phoneBookList.get(i).getImage());
            //이름
            TextView nameText = view.findViewById(R.id.item_name);
            nameText.setText(phoneBookList.get(i).getName());
            //번호
            TextView phoneText = view.findViewById(R.id.item_num);
            phoneText.setText(phoneBookList.get(i).getNum());
            container.addView(view);
        }


    }

    public class PhoneBook {
        private int image;
        private String name;
        private String num;

        PhoneBook(int image, String name, String num) {
            this.image = image;
            this.name = name;
            this.num = num;
        }

        public int getImage() {
            return image;
        }

        public String getName() {
            return name;
        }

        public String getNum() {
            return num;
        }
    }

}