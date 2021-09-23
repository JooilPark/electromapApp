package com.sunday.electromapapp.model.net

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Singleton
object RetrofitUtil {
    //private val baseUrl = "http://52.78.33.240:8080"
    private val baseUrl = "http://www.ewheelmap.com:8080/"
    private val gson = GsonBuilder()
        .setDateFormat("E, dd MMMM yyyy HH:mm:ss X")
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        .setLenient().create()
    private var instance: Retrofit? = null
    private val okClint = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }).build()

    private fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okClint)
                .build()
        }
        return instance!!
    }

    fun getApi(): ElectromapApi = getInstance().create(ElectromapApi::class.java)


}