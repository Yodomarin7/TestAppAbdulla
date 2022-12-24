package com.example.testappabdulla.di

import android.content.Context
import com.example.data.repository.CardRepository
import com.example.testappabdulla.viewmodel.CardInfoViewModelFactory
import com.example.testappabdulla.viewmodel.SearchCardViewModel
import com.example.testappabdulla.viewmodel.SearchCardViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class AppModule(val context: Context) {

    @Provides
    fun provideContext(): Context { return context }

    @Provides
    fun provideSearchCardVMF(cardRepository: CardRepository): SearchCardViewModelFactory {
        return SearchCardViewModelFactory(cardRepository = cardRepository)
    }

    @Provides
    fun provideCardInfoVMF(cardRepository: CardRepository): CardInfoViewModelFactory {
        return CardInfoViewModelFactory(cardRepository = cardRepository)
    }

}