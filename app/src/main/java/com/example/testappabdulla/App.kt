package com.example.testappabdulla

import android.app.Application
import com.example.testappabdulla.di.AppComponent
import com.example.testappabdulla.di.AppModule
import com.example.testappabdulla.di.DaggerAppComponent

class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }
}