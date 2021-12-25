package com.sunday.electromapapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.sunday.electromapapp.model.net.ElectromapApi
import com.sunday.electromapapp.model.net.RetrofitUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Module
@InstallIn(SingletonComponent::class)
interface NetWorkMoudle {
    companion object {
        @Provides
        fun providerGsonBuilder(): Gson = GsonBuilder()
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
                    when (json.asString.length) {
                        5 -> {
                            LocalTime.parse(
                                json.asString,
                                DateTimeFormatter.ofPattern("HH:mm")
                            )
                        }
                        else -> {
                            LocalTime.parse(
                                json.asString,
                                DateTimeFormatter.ofPattern("HH:mm:ss")
                            )
                        }
                    }

                })

            .setLenient().create()

        @Provides
        fun providerOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

        @Provides
        fun getApi(gson: Gson, okHttpClient: OkHttpClient): ElectromapApi = Retrofit.Builder()
            .baseUrl(RetrofitUtil.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().create(ElectromapApi::class.java)
    }
}
