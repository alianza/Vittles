package com.example.vittles.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule internal constructor(context: Context){
    private var context = context

    @Singleton
    @Provides
    public fun provideContext(): Context {
        return context
    }
}