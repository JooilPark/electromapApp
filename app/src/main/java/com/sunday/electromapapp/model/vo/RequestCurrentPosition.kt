package com.sunday.electromapapp.model.vo

import retrofit2.http.Field

data class RequestCurrentPosition (val latitude: Double,val longitude: Double,val ridusKm: Int)