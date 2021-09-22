package com.sunday.electromapapp.model.net

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Singleton
object RetrofitUtil {
    //private val baseUrl = "http://52.78.33.240:8080"
    private val baseUrl = "http://www.ewheelmap.com:8080/"
    private val gson = GsonBuilder().setLenient().create()
    private var instance: Retrofit? = null
    private fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return instance!!
    }

    fun getApi(): ElectromapApi = getInstance().create(ElectromapApi::class.java)


}