package com.sunday.electromapapp.model.vo

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime

data class Positioninfo(
    val id: Long,
    val facilityName: String, // 시설명
    val cityName: String, // 시도명
    val countryName: String, // 시군구명
    val countryCode: String, // 시군구 코드
    val addressRoad: String,// 도로명주소
    val addressNumber: String,// 지번주소
    val latitude: Double = 0.0, // 위도
    val longitude: Double = 0.0, // 경도
    val pleaceDescription: String, // 설치장소설명
    val weeklyTimeStart: LocalTime, // 평일운영시간
    val weeklyTimeEnd: LocalTime, // 평일운영종료시간
    val weeklySetdayStart: LocalTime, // 토요일 운영시작
    val weeklySetdayEnd: LocalTime, // 토요일 운영 종료
    val weeklySundayStart: LocalTime,// 일요일 운영 시작
    val weeklySundayEnd: LocalTime, // 일요일 운영 종료
    val useNumber: Int = 0,// 동시 충전 가능대수
    val aircompresser: Boolean = false,// 공기 주입기 사용가능
    val phoneChager: Boolean = false, // 휴대 전화 충전가능
    val managerFacilityName: String, // 관리 기관명
    val managerFacilityPhone: String, // 관리 기관 전화번호
    val keyDay: String, // 데이터 기준일자
    val providerCode: String,// 제공기관코드
    val providerName: String// 제공기관코드

){
    /**
     * 지금 충전 가능한가 >
     */
    fun isRechargeEnable() : Boolean{
        val nowDay = LocalDate.now()
        val nowTime = LocalTime.now()
        when(nowDay.dayOfWeek.value){
            in 1..5 -> { return weeklySetdayStart.isAfter(nowTime) && weeklyTimeEnd.isBefore(nowTime)  }
            6 -> { weeklySetdayStart.isAfter(nowTime) && weeklySetdayEnd.isBefore(nowTime)}
            7 ->{ weeklySetdayStart.isAfter(nowTime) && weeklySundayEnd.isBefore(nowTime)}
        }
        return true
    }
}
