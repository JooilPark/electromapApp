package com.sunday.electromapapp.model.net

import com.sunday.electromapapp.model.vo.Positioninfo
import kotlinx.coroutines.flow.Flow
import retrofit2.http.POST

interface ElectromapApi {
    /**
     * 지금위치의 충전소 정보를 가져온다
     *
     */
    @POST("/getpositions")
    fun getpositions(latitude: Double, longitude: Double, ridusKm: Int): Flow<List<Positioninfo>>
}