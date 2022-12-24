package com.example.data

import com.example.data.source.retrofit.CardRetrofit
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AppRetrofit {
    val groupApi: CardRetrofit.CardAPI by lazy {
        retrofitCard.create(CardRetrofit.CardAPI::class.java)
    }

    private val retrofitCard: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://lookup.binlist.net/")
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)).build())
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .build()
    }
}