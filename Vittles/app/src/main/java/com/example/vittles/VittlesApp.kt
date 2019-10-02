package com.example.vittles

import android.app.Application
import com.example.vittles.di.AppComponent
import com.example.vittles.di.DaggerAppComponent

class VittlesApp : Application() {
    companion object {
        lateinit var component: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}