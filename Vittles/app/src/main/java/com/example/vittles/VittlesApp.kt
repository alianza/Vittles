package com.example.vittles

import androidx.core.app.NotificationManagerCompat
import com.example.vittles.di.AppModule
import com.example.vittles.di.DaggerAppComponent
import com.example.vittles.services.notification.NotificationService
import com.example.vittles.services.notification.NotificationScheduleService
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import net.danlew.android.joda.JodaTimeAndroid
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

/**
 * Startup class.
 *
 * @author Jeroen Flietstra
 */
class VittlesApp : DaggerApplication() {
    /** {@inheritDoc}*/
    override fun applicationInjector(): AndroidInjector<VittlesApp> {
        val appComponent = DaggerAppComponent.builder()
            .applicationBind(this)
            .appModule(AppModule(this))
            .build()
        appComponent.inject(this)

        return appComponent
    }

    /** {@inheritDoc}*/
    override fun onCreate() {
        super.onCreate()

        // Initialize Joda-Time
        JodaTimeAndroid.init(this)

        // Setup notification service
        NotificationService.createNotificationChannel(this@VittlesApp, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel.")

        // Setup notification scheduler
        NotificationScheduleService.scheduleNotificationAudit(applicationContext)

        // Necessary for creating a connection with the OFF API
        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
    }
}