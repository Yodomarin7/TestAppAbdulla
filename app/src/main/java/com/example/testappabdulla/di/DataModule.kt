package com.example.testappabdulla.di

import android.content.Context
import com.example.data.AppDatabase
import com.example.data.repository.CardRepository
import com.example.data.source.retrofit.CardRetrofit
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideDB(context: Context): AppDatabase { return AppDatabase.getDatabase(context.applicationContext) }

    @Provides
    fun provideCardRepository(db: AppDatabase, cardRetrofit: CardRetrofit): CardRepository {
        return CardRepository(db.cardDao(), cardRetrofit)
    }

    @Provides
    fun provideCardRetrofit(): CardRetrofit { return CardRetrofit() }
}