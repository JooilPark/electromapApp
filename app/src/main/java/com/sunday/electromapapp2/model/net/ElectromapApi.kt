package com.sunday.electromapapp2.model.net

import com.sunday.electromapapp2.model.vo.Positioninfo
import com.sunday.electromapapp2.model.vo.RequestCurrentPosition
import retrofit2.http.Body
import retrofit2.http.POST

interface ElectromapApi {
    /**
     * 지금위치의 충전소 정보를 가져온다
     *
     */
    @POST("/getpositions")
     suspend fun  getpositions(@Body pos: RequestCurrentPosition): List<Positioninfo>
}