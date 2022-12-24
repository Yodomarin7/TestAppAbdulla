package com.example.data.repository

import com.example.data.source.retrofit.CardRetrofit
import com.example.data.source.room.CardDao
import com.example.data.source.room.CardEntity
import kotlinx.coroutines.flow.Flow

class CardRepository(
    private val cardDao: CardDao,
    private val cardRetrofit: CardRetrofit
) {

    fun getAllFromDao(): Flow<List<CardEntity>> {
        return cardDao.getAllItems()
    }

    suspend fun insertDao(card: CardEntity) {
        cardDao.insertOne(card)
    }

    suspend fun deleteDao(card: CardEntity) {
        cardDao.deleteOne(card)
    }

    suspend fun getCardInfo(card: String): CardRetrofit.CardInfoResponse {
        return cardRetrofit.getCardInfo(card)
    }
}