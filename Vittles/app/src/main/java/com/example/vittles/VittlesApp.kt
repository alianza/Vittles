package com.example.vittles

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.example.vittles.di.AppComponent
import com.example.vittles.di.DaggerAppComponent
import com.example.vittles.services.NotificationService
import com.example.vittles.services.notification.NotificationScheduleService
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Startup class.
 *
 * @author Jeroen Flietstra
 */
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

        // Initialize Joda-Time
        JodaTimeAndroid.init(this)

        // Setup notification service
        NotificationService.createNotificationChannel(this@VittlesApp, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel.")

        // Setup notification scheduler
        NotificationScheduleService.scheduleNotificationAudit(applicationContext)
    }
}