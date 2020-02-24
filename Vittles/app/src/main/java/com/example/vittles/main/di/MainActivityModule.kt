package com.example.vittles.main.di

import android.app.Activity
import com.example.vittles.main.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityModule {

    @ContributesAndroidInjector(
        modules = [Bindings::class]
    )
    fun bindMainActivity(): MainActivity

    @Module
    interface Bindings {

        @Binds
        fun bindActivity(activity: MainActivity): Activity
    }
}