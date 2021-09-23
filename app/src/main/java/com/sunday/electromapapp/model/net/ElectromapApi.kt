package com.sunday.electromapapp.model.net

import com.sunday.electromapapp.model.vo.Positioninfo
import com.sunday.electromapapp.model.vo.RequestCurrentPosition
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ElectromapApi {
    /**
     * 지금위치의 충전소 정보를 가져온다
     *
     */
    @POST("/getpositions")
    suspend fun  getpositions(@Body pos: RequestCurrentPosition): Flow<List<Positioninfo>>
}