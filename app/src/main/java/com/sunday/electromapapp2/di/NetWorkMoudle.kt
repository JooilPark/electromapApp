package com.sunday.electromapapp2.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sunday.electromapapp2.model.net.ElectromapApi
import com.sunday.electromapapp2.model.util.JsonDataTimeDeseriialzer
import com.sunday.electromapapp2.model.util.JsonDateDeserializser
import com.sunday.electromapapp2.model.util.JsonTimeDeserializser
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


@Module
@InstallIn(SingletonComponent::class)
interface NetWorkMoudle {

    companion object {
        @Provides
        fun providerGsonBuilder(): Gson = GsonBuilder()
            .registerTypeAdapter(
                LocalDateTime::class.java,
                JsonDataTimeDeseriialzer()
            )
            .registerTypeAdapter(
                LocalDate::class.java,
                JsonDateDeserializser("yyyy-MM-dd")
            )
            .registerTypeAdapter(
                LocalTime::class.java,
                JsonTimeDeserializser()
            )
            .setLenient().create()

        @Provides
        fun providerOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

        @Provides
        fun getApi(gson: Gson, okHttpClient: OkHttpClient): ElectromapApi = Retrofit.Builder()
            .baseUrl("http://www.ewheelmap.com:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().create(ElectromapApi::class.java)


    }

}
