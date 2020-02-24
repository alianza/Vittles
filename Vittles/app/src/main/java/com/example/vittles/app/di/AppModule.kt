package com.example.vittles.app.di

import android.app.Application
import android.content.Context
import com.example.vittles.app.di.DataModule
import com.example.vittles.main.di.MainActivityModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        DataModule::class,
        MainActivityModule::class
    ]
)
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context = application.applicationContext
}