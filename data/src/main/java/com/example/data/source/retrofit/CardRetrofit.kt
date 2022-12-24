package com.example.data.source.retrofit

import com.example.data.AppRetrofit
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

class CardRetrofit {
    data class CardInfoModel(
        val number: Number?,
        val scheme: String?,
        val type: String?,
        val brand: String?,
        val prepaid: Boolean?,
        val country: Country?,
        val bank: Bank?
    )

    data class Number(
        val length: Int?,
        val luhn: Boolean?
    )

    data class Country(
        val numeric: String?,
        val alpha2: String?,
        val name: String?,
        val emoji: String?,
        val currency: String?,
        val latitude: Int?,
        val longitude: Int?
    )

    data class Bank(
        val name: String?,
        val url: String?,
        val phone: String?,
        val city: String?
    )

    interface CardAPI {
        @GET("/{number}")
        suspend fun getInfo(@Path("number") card: String): Response<CardInfoModel>
    }

    sealed class CardInfoResponse {
        class Success(val data: CardInfoModel?): CardInfoResponse()
        class Fail(val msg: String, val code: Int): CardInfoResponse()
    }

    suspend fun getCardInfo(card: String): CardInfoResponse {
        return try {
            val response = AppRetrofit.groupApi.getInfo(card)
            when {
                response.isSuccessful -> {
                    CardInfoResponse.Success(response.body())
                }
                else -> {
                    CardInfoResponse.Fail(response.message(), response.code())
                }
            }
        } catch (e: Exception) {
            CardInfoResponse.Fail("Проверьте соединение с интернетом", 0)
        }
    }
}


















