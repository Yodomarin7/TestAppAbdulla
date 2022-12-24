package com.example.testappabdulla.di

import com.example.testappabdulla.MainActivity
import com.example.testappabdulla.screen.CardInfoFragment
import com.example.testappabdulla.screen.SearchCardFragment
import com.example.testappabdulla.viewmodel.CardInfoViewModel
import dagger.Component

@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {
    fun inject(searchCardFragment: SearchCardFragment)
    fun inject(cardInfoFragment: CardInfoFragment)
}