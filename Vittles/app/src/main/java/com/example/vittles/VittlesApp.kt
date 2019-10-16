package com.example.vittles

import androidx.core.app.NotificationManagerCompat
import com.example.vittles.di.AppModule
import com.example.vittles.di.DaggerAppComponent
import com.example.vittles.services.NotificationService
import com.example.vittles.services.notification.NotificationScheduleService
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Startup class.
 *
 * @author Jeroen Flietstra
 */
class VittlesApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<VittlesApp> {
        val appComponent = DaggerAppComponent.builder()
            .applicationBind(this)
            .appModule(AppModule(this))
            .build()
        appComponent.inject(this)

        return appComponent
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Joda-Time
        JodaTimeAndroid.init(this)

        // Setup notification service
        NotificationService.createNotificationChannel(this@VittlesApp, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel.")

        // Setup notification scheduler
        NotificationScheduleService.scheduleNotificationAudit(applicationContext)
    }
}