package com.example.vittles

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.example.vittles.di.AppComponent
import com.example.vittles.di.DaggerAppComponent
import com.example.vittles.services.NotificationService

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


        NotificationService.createNotificationChannel(this@VittlesApp, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel.")
    }
}