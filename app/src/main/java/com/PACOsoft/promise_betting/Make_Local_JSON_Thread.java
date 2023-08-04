package com.PACOsoft.promise_betting;



import android.content.Intent;

import android.util.Log;

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


public class Make_Local_JSON_Thread extends Thread {

    static public String clientId = "FuKF0Wwy5ToDyuXhRuDW"; //애플리케이션 클라이언트 아이디값"
    static public String clientSecret = "E1aG6QAOhi"; //애플리케이션 클라이언트 시크릿값"
    static private String title;
    static private String  category;
    static private String address;
    static private String roadAddress;
    static private int mapx;
    static private int mapy;

    static private String search_word;

    static private Intent intent;

    static private ArrayList<Location> arrayList;

    public void run() {
        main();
    }

    public static void main() {

        //search_word = intent.getStringExtra("search_word"); //mainActivity에서 intent해준 id를 받아옴

        String text = null;
        try {
            text = URLEncoder.encode("우남퍼스트빌", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/local?query=" + text + "&display=3&sort=sim";    // json 결과
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL, requestHeaders);

        parseData(responseBody);

        Log.e("tag",responseBody);
        //Log.e("tag",search_word);
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders) {
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

            arrayList = new ArrayList<>();// Location 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
            arrayList.clear(); //기존 배열리스트를 초기화
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                title = item.getString("title");
                //System.out.println("TITLE : " + title);
                category = item.getString("category");
                //System.out.println("category : " + category);
                address = item.getString("address");
                //System.out.println("ADDRESS : " + address);
                roadAddress = item.getString("address");
                //System.out.println("ROADADDRESS : " + roadAddress);
                mapx = item.getInt("mapx");
                //System.out.println("mapx : " + mapx);
                mapy = item.getInt("mapy");
                //System.out.println("mapy : " + mapy);

                Location location = new Location(title,category,address,roadAddress,mapx,mapy);//location 객체 생성 후 정보 담음
                System.out.println("location : " + location.getTitle());
                System.out.println("location : " + location.getCategory());
                System.out.println("location : " + location.getAddress());
                System.out.println("location : " + location.getRoadaddress());
                System.out.println("location : " + location.getMapx());
                System.out.println("location : " + location.getMapy());
//                arrayList.add(location);//담은 데이터를 어레이리스트에 넣고 리사이클러뷰로 보낼 준비함
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}