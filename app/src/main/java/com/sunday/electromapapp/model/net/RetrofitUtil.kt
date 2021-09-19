package com.sunday.electromapapp.model.net

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitUtil @Inject constructor(private val context: Context) {
    private val baseUrl = "http://52.78.33.240:8080"
    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)

        .client(OkHttpClient()).build()
    fun getApi ()  = retrofit.create(ElectromapApi::class.java)

}