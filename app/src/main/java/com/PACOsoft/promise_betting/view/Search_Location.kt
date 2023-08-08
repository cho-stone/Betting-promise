package com.PACOsoft.promise_betting.view

import KakaoAPI
import ResultSearchKeyword
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.PACOsoft.promise_betting.Adapter.Location_List_Adapter

import com.PACOsoft.promise_betting.databinding.ActivitySearchLocationBinding
import com.PACOsoft.promise_betting.obj.Location

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Search_Location : AppCompatActivity() {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK b00a5dcd170452d04b2fb88a535b842a"  // REST API 키
    }

    private lateinit var binding : ActivitySearchLocationBinding
    private val listItems = arrayListOf<Location>()   // 리사이클러 뷰 아이템
    private val locationListAdapter = Location_List_Adapter(listItems)    // 리사이클러 뷰 어댑터
    private var keyword = ""        // 검색 키워드

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchLocationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 리사이클러 뷰
        binding.rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.adapter = locationListAdapter
        // 리스트 아이템 클릭 시 해당 위치로 이동
        locationListAdapter.setItemClickListener(object: Location_List_Adapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

                val intent = Intent(applicationContext, Create_Room::class.java).apply {
                    putExtra("title", listItems[position].name)
                    putExtra("category", listItems[position].category)
                    putExtra("address", listItems[position].address)
                    putExtra("road", listItems[position].road)
                    putExtra("x", listItems[position].x)
                    putExtra("y", listItems[position].y)
                }
                setResult(RESULT_OK, intent)
                finish()


            }
        })

        binding.etSearchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                keyword = binding.etSearchField.text.toString()
                if(keyword == "") {
                    listItems.clear()
                    locationListAdapter.notifyDataSetChanged()
                }
                else {
                    searchKeyword(keyword)
                }


            }
        })
        // 삭제 버튼
        binding.btnSearch.setOnClickListener {
            binding.etSearchField.text.clear()
            listItems.clear()
            locationListAdapter.notifyDataSetChanged()
        }

    }

    // 키워드 검색 함수
    private fun searchKeyword(keyword: String) {
        val retrofit = Retrofit.Builder()          // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoAPI::class.java)            // 통신 인터페이스를 객체로 생성
        val call = api.getSearchKeyword(API_KEY, keyword)    // 검색 조건 입력

        // API 서버에 요청
        call.enqueue(object: Callback<ResultSearchKeyword> {
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
                // 통신 성공
                addItemsAndMarkers(response.body())
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Log.w("LocalSearch", "통신 실패: ${t.message}")
            }
        })
    }

    // 검색 결과 처리 함수
    private fun addItemsAndMarkers(searchResult: ResultSearchKeyword?) {
        if (!searchResult?.documents.isNullOrEmpty()) {
            // 검색 결과 있음
            listItems.clear()                   // 리스트 초기화
            for (document in searchResult!!.documents) {
                // 결과를 리사이클러 뷰에 추가
                val item = Location(document.place_name,
                    document.category_group_name,
                    document.road_address_name,
                    document.address_name,
                    document.x.toDouble(),
                    document.y.toDouble())
                listItems.add(item)
            }
            locationListAdapter.notifyDataSetChanged()
        } else {
            // 검색 결과 없음
            Toast.makeText(this, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

}