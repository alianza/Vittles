package com.example.vittles.services.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.crashlytics.android.Crashlytics
import com.example.domain.exceptions.NotificationDataException
import com.example.domain.notification.GetNotificationProductsExpired
import com.example.domain.notification.Notification
import dagger.android.DaggerBroadcastReceiver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Background scheduler for the notification service.
 *
 * @author Jeroen Flietstra
 */
class NotificationScheduleService : DaggerBroadcastReceiver() {
    @Inject
    lateinit var getNotification: GetNotificationProductsExpired

    private val disposables: CompositeDisposable = CompositeDisposable()


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
            getNotification.invoke().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ notify(it, context) }, { onNotifyFail(it, context) })
        )
    }


    /**
     * Creates the notification in the notification tray.
     *
     * @param context The application context needed for the notification service.
     */
    private fun notify(notification: Notification, context: Context?) {
        NotificationService.createDataNotification(
            context!!, notification
        )
        // Schedule alarm again
        scheduleNotificationAudit(context)
    }

    /**
     * If no notification can be shown, log if the error is not the NotificationDataException.
     *
     * @param error The throwable thrown by the observable.
     */
    private fun onNotifyFail(error: Throwable, context: Context?) {
        if (error !is NotificationDataException) {
            Crashlytics.logException(error)
        }
        context?.let { scheduleNotificationAudit(it) }
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