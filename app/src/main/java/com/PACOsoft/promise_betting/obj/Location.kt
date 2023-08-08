package com.PACOsoft.promise_betting.obj

// 리사이클러 뷰 아이템 클래스
class Location(val name: String = "",      // 장소명
               val category: String = "",  // 카테고리
               val road: String = "",      // 도로명 주소
               val address: String = "",   // 지번 주소
               val x: Double = 0.0,         // 경도(Longitude)
               val y: Double = 0.0)         // 위도(Latitude)
