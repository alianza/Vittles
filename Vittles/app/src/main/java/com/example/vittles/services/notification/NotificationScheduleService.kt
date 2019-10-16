package com.example.vittles.services.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.domain.model.Product
import com.example.domain.productfetch.FetchProductsUseCase
import com.example.vittles.services.NotificationService
import dagger.android.DaggerBroadcastReceiver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Final constant for the minimum days remaining to be considered 'expiring soon'.
 */
const val DAYS_REMAINING_BOUNDARY = 2

/**
 * Background scheduler for the notification service.
 *
 * @author Jeroen Flietstra
 */
class NotificationScheduleService : DaggerBroadcastReceiver() {
    @Inject
    lateinit var fetchProductsUseCase: FetchProductsUseCase

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var totalProductsToExpire = 0


    /**
     * Overridden function that calls the audit notification method every time the audit timestamp
     * alarm goes off.
     *
     */
    override fun onReceive(p0: Context?, p1: Intent?) {
        super.onReceive(p0, p1)
        auditNotification(p0)
    }

    /**
     * Fetches products needed to calculate the data for the notification.
     *
     * @param context The application context needed for displaying notifications.
     */
    private fun auditNotification(context: Context?) {
        disposables.add(
            fetchProductsUseCase.fetch().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ generateData(it, context) }, { })
        )
    }

    /**
     * Method that calculates the data for the notification.
     *
     * @param products The products in the products list.
     * @param context The application context needed to pass on to the notify method.
     */
    private fun generateData(products: List<Product>, context: Context?) {
        totalProductsToExpire = 0
        products.forEach { product ->
            if (product.getDaysRemaining() <= DAYS_REMAINING_BOUNDARY) {
                totalProductsToExpire++
            }
        }
        notify(context)
    }

    /**
     * Creates the notification in the notification tray.
     *
     * @param context The application context needed for the notification service.
     */
    private fun notify(context: Context?) {
        val message = "You have $totalProductsToExpire product(s) expiring within 2 days."
        NotificationService.createDataNotification(
            context!!,
            "Vittles",
            "Reduce food waste",
            message,
            false
        )
        // Schedule alarm again
        scheduleNotificationAudit(context)
    }

    companion object {

        /**
         * Schedules next audit to the next day.
         *
         * @param context The application context needed for the alarm manager.
         */
        fun scheduleNotificationAudit(context: Context) {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, NotificationScheduleService::class.java)
            val nextAudit = // Set nextAudit to tomorrow 12:00pm
                DateTime().withDate(DateTime().toLocalDate().plusDays(1)).withHourOfDay(12)
                    .withMinuteOfHour(0).withSecondOfMinute(0)
            val broadcast = // Set alarm
                PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAudit.millis, broadcast)
        }
    }

}