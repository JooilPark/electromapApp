package com.sunday.electromapapp.model.vo

import android.os.Parcel
import android.os.Parcelable
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
    var phoneChager: Boolean = false, // 휴대 전화 충전가능
    val managerFacilityName: String, // 관리 기관명
    val managerFacilityPhone: String, // 관리 기관 전화번호
    val keyDay: String, // 데이터 기준일자
    val providerCode: String,// 제공기관코드
    val providerName: String,// 제공기관코드

) : Parcelable {
    constructor(facilityName: String, latitude: Double, longitude: Double) : this(
        0,
        facilityName,
        "",
        "",
        "",
        "",
        "",
        latitude,
        longitude,
        "",
        LocalTime.now(),
        LocalTime.now(),
        LocalTime.now(),
        LocalTime.now(),
        LocalTime.now(),
        LocalTime.now(),
        0,
        false,
        false,
        "",
        "01082652330",
        "",
        "",
        ""
    )


    // 1. 지금 충전 가능 상태 확인
    // 2. 요일별로 충전이 가능한 상태 표시
    // 00:00 24:00
    // 00:00 00:00
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readSerializable() as LocalTime,
        parcel.readSerializable() as LocalTime,
        parcel.readSerializable() as LocalTime,
        parcel.readSerializable() as LocalTime,
        parcel.readSerializable() as LocalTime,
        parcel.readSerializable() as LocalTime,
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    /**
     * 지금 충전 가능한가 >
     */
    fun isRechargeEnable(): Boolean {
        val nowDay = LocalDate.now()
        val nowTime = LocalTime.now()

        when (nowDay.dayOfWeek.value) {
            in 1..5 -> {
                return isWeek() && (weeklyTimeStart.isBefore(nowTime) &&
                        day24change(weeklyTimeEnd).isAfter(nowTime))
            }
            6 -> {
                return isSaturday() && (weeklySetdayStart.isBefore(nowTime) &&
                        day24change(weeklySetdayEnd).isAfter(nowTime))
            }
            7 -> {
                return isSunday() && (weeklySundayStart.isBefore(nowTime) &&
                        day24change(weeklySundayEnd).isAfter(nowTime))
            }
        }
        return false
    }

    /**
     * 끝나는 시간이 24시간이 아닌 12시간으로 했을때 변경 .
     * 01:00 같은 수지
     *
     */
    private fun day24change(endtime: LocalTime): LocalTime {
        if (endtime.hour != 0 && endtime.hour <= 12) {
            return LocalTime.of(endtime.hour + 12,0)
        }
        return endtime
    }

    fun isWeek(): Boolean =
        (!(weeklyTimeStart.toSecondOfDay() == 0 && weeklyTimeEnd.toSecondOfDay() == 0))

    fun isSaturday(): Boolean =
        (!(weeklySetdayStart.toSecondOfDay() == 0 && weeklySetdayEnd.toSecondOfDay() == 0))

    fun isSunday(): Boolean =
        (!(weeklySundayStart.toSecondOfDay() == 0 && weeklySundayEnd.toSecondOfDay() == 0))

    fun logWeek() = "${isWeek()} start $weeklyTimeStart end $weeklyTimeEnd"
    fun logSat() = "${isSaturday()} start $weeklySetdayStart end $weeklySetdayEnd"
    fun logSun() = "${isSunday()} start $weeklySundayStart end $weeklySundayEnd"
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {

        parcel.writeLong(this@Positioninfo.id)
        parcel.writeString(this@Positioninfo.facilityName)
        parcel.writeString(this@Positioninfo.cityName)
        parcel.writeString(this@Positioninfo.countryName)
        parcel.writeString(this@Positioninfo.countryCode)
        parcel.writeString(this@Positioninfo.addressRoad)
        parcel.writeString(this@Positioninfo.addressNumber)
        parcel.writeDouble(this@Positioninfo.latitude)
        parcel.writeDouble(this@Positioninfo.longitude)
        parcel.writeString(this@Positioninfo.pleaceDescription)
        parcel.writeSerializable(this@Positioninfo.weeklyTimeStart)
        parcel.writeSerializable(this@Positioninfo.weeklyTimeEnd)
        parcel.writeSerializable(this@Positioninfo.weeklySetdayStart)
        parcel.writeSerializable(this@Positioninfo.weeklySetdayEnd)
        parcel.writeSerializable(this@Positioninfo.weeklySundayStart)
        parcel.writeSerializable(this@Positioninfo.weeklySundayEnd)
        parcel.writeInt(this@Positioninfo.useNumber)
        parcel.writeValue(this@Positioninfo.aircompresser)
        parcel.writeValue(this@Positioninfo.phoneChager)
        parcel.writeString(this@Positioninfo.managerFacilityName)
        parcel.writeString(this@Positioninfo.managerFacilityPhone)
        parcel.writeString(this@Positioninfo.keyDay)
        parcel.writeString(this@Positioninfo.providerCode)
        parcel.writeString(this@Positioninfo.providerName)
    }

    companion object CREATOR : Parcelable.Creator<Positioninfo> {
        override fun createFromParcel(parcel: Parcel): Positioninfo {
            return Positioninfo(parcel)
        }

        override fun newArray(size: Int): Array<Positioninfo?> {
            return arrayOfNulls(size)
        }
    }

}
