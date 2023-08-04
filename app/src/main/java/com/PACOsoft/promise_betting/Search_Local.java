package com.PACOsoft.promise_betting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Search_Local extends AppCompatActivity {
    private static EditText search_word;
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView.LayoutManager layoutManager;
    private static ArrayList<Location> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_local);


        recyclerView = findViewById(R.id.SearchLocationRecyclerview); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(recyclerView.getContext());//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<Location>();// Location 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)

        search_word = findViewById(R.id.et_search_local);
        search_word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                arrayList.clear(); //기존 배열리스트를 초기화
                //adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
                Search_Location(search_word.getText().toString());


                //adapter = new Location_List_Adapter(arrayList, getApplicationContext());
                //recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
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

    private void Search_Location(String search_word) {
        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String clientId = "FuKF0Wwy5ToDyuXhRuDW"; //애플리케이션 클라이언트 아이디값"
                        String clientSecret = "E1aG6QAOhi"; //애플리케이션 클라이언트 시크릿값"

                        String text = null;
                        try {
                            text = URLEncoder.encode(search_word, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException("검색어 인코딩 실패", e);
                        }

                        String apiURL = "https://openapi.naver.com/v1/search/local?query=" + text + "&display=3&sort=sim";    // json 결과
                        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
                        java.util.Map<String, String> requestHeaders = new HashMap<>();
                        requestHeaders.put("X-Naver-Client-Id", clientId);
                        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
                        new Thread(() -> {
                            String responseBody = get(apiURL, requestHeaders);//네트워크에 접근해서 데이터를 받아오는 부분이므로 별도의 스레드에서 돌려야하므로 새 스레드 만들어서 돌렸다.
                            parseData(responseBody);//받아온 데이터 쪼개기
                        }).start();

                        //System.out.println(arrayList.get(0).getTitle());
                        //System.out.println(arrayList.get(1).getTitle());
                        //adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
                    }
                });

            }
        }.start();


    }


    private static String get(String apiUrl, java.util.Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }

        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    private static void parseData(String responseBody) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseBody.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String title;
                String category;
                String address;
                String roadAddress;
                int mapx;
                int mapy;
                title = item.getString("title");
                category = item.getString("category");
                address = item.getString("address");
                roadAddress = item.getString("address");
                mapx = item.getInt("mapx");
                mapy = item.getInt("mapy");

                Location location = new Location(title, category, address, roadAddress, mapx, mapy);//location 객체 생성 후 정보 담음
                arrayList.add(location);//담은 데이터를 어레이리스트에 넣고 리사이클러뷰로 보낼 준비함
            }

            //System.out.println(arrayList.get(0).getTitle());
            //System.out.println(arrayList.get(1).getTitle());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}