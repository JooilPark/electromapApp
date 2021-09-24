package com.sunday.electromapapp.model.net

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.inject.Singleton
import com.google.gson.JsonParseException

import com.google.gson.JsonDeserializationContext

import com.google.gson.JsonElement

import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Singleton
object RetrofitUtil {
    //private val baseUrl = "http://52.78.33.240:8080"
    private val baseUrl = "http://www.ewheelmap.com:8080/"
    private val TAG = "RetrofitUtil"
    private val gson = GsonBuilder()
        .registerTypeAdapter(
            LocalDateTime::class.java,
            JsonDeserializer { json, typeOfT, context ->
                LocalDateTime.parse(
                    json.asString,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                )
            })
        .registerTypeAdapter(
            LocalDate::class.java,
            JsonDeserializer { json, typeOfT, context ->
                LocalDate.parse(
                    json.asString,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
            })
        .registerTypeAdapter(
            LocalTime::class.java,
            JsonDeserializer { json, typeOfT, context ->
                when(json.asString.length){
                    5->{
                        LocalTime.parse(
                            json.asString,
                            DateTimeFormatter.ofPattern("HH:mm")
                        )
                    }
                    else ->{
                        LocalTime.parse(
                            json.asString,
                            DateTimeFormatter.ofPattern("HH:mm:ss")
                        )
                    }
                }

            })

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
                //.addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(okClint)
                .build()
        }
        return instance!!
    }

    fun getApi(): ElectromapApi = getInstance().create(ElectromapApi::class.java)


}