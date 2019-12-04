package com.example.vittles.services.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.crashlytics.android.Crashlytics
import com.example.domain.notification.NotificationDataException
import com.example.domain.notification.GetNotificationProductsExpired
import com.example.domain.notification.Notification
import com.example.vittles.enums.SettingKeys
import com.example.vittles.settings.SharedPreference
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
 * @author Fethi Tewelde
 */
class NotificationScheduleService : DaggerBroadcastReceiver() {
    /**
     * The GetNotification use case from the domain module.
     */
    @Inject
    lateinit var getNotification: GetNotificationProductsExpired

    /** Disposables contains all async calls made */
    private val disposables: CompositeDisposable = CompositeDisposable()


    /**
     * Overridden function that calls the audit notification method every time the audit timestamp
     * alarm goes off.
     *
     */
    override fun onReceive(p0: Context?, p1: Intent?) {
        super.onReceive(p0, p1)
        auditNotification(p0)
        disposables.clear()
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

        private lateinit var alarmManager: AlarmManager
        private lateinit var broadcast: PendingIntent

        /**
         * Checks if the notification is turned on or off.
         * Changes when to send notification based on 'notificationTimer'.
         * Schedules next audit to the next day.
         *
         * @param context The application context needed for the alarm manager.
         */
        fun scheduleNotificationAudit(context: Context) {
            val sharedPreference = SharedPreference(context)
            if (sharedPreference.getValueBoolean(SettingKeys.Notifications.value, true)) {
                val notificationTimer = sharedPreference.getValueInt(SettingKeys.NotificationTime.value)

                var nextAudit = DateTime()

                when (notificationTimer) {
                    0 -> nextAudit = // Set nextAudit to Daily at 12:00PM
                        DateTime().plusDays(1).withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0)
                    1 -> nextAudit = // Set nextAudit to Weekly at 12:00PM
                        DateTime().plusWeeks(1).withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0)
                    2 -> nextAudit = // Set nextAudit to Monthly at 12:00PM
                        DateTime().plusMonths(1).withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0)
                }

                alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, NotificationScheduleService::class.java)

                broadcast = // Set alarm
                    PendingIntent.getBroadcast(
                        context,
                        100,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAudit.millis, broadcast)
            }

        }

        /**
         * Checks if the notification is turned on or off
         * Then based on that cancels broadcast of notification
         *
         * @param context The application context needed for the alarm manager.
         */
        fun exitNotificationSchedule(context: Context) {
            val sharedPreference = SharedPreference(context)
            if (!sharedPreference.getValueBoolean(SettingKeys.Notifications.value, false)) {
                alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(broadcast)
            }
        }
    }

}