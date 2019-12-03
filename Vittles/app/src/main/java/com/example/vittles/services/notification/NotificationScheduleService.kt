package com.example.vittles.services.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.crashlytics.android.Crashlytics
import com.example.domain.notification.NotificationDataException
import com.example.domain.notification.Notification
import com.example.domain.settings.model.NotificationSchedule
import com.example.data.settings.SharedPreferenceHelper
import dagger.android.DaggerBroadcastReceiver
import io.reactivex.disposables.CompositeDisposable
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Background scheduler for the notification service.
 *
 * @author Jeroen Flietstra
 * @author Fethi Tewelde
 */
class NotificationScheduleService : DaggerBroadcastReceiver(), NotificationScheduleContract.Service {

    @Inject
    lateinit var presenter: NotificationSchedulePresenter

    /** Disposables contains all async calls made */
    private val disposables: CompositeDisposable = CompositeDisposable()

    init {
        presenter.start(this)
    }

    /**
     * Overridden function that calls the audit notification method every time the audit timestamp
     * alarm goes off.
     *
     */
    override fun onReceive(p0: Context?, p1: Intent?) {
        super.onReceive(p0, p1)
        p0?.let { presenter.startPresenting(it) }
        disposables.clear()
    }


    /**
     * Creates the notification in the notification tray.
     *
     * @param context The application context needed for the notification service.
     */
    override fun notify(notification: Notification, context: Context?) {
        NotificationService.createDataNotification(
            context!!, notification
        )
    }

    /**
     * If no notification can be shown, log if the error is not the NotificationDataException.
     *
     * @param error The throwable thrown by the observable.
     */
    override fun onNotifyFail(error: Throwable) {
        if (error !is NotificationDataException) {
            Crashlytics.logException(error)
        }
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
        fun scheduleNotificationAudit(
            context: Context,
            notificationSchedule: NotificationSchedule,
            notificationEnabled: Boolean
        ) {
            if (notificationEnabled) {

                var nextAudit = DateTime()

                when (notificationSchedule) {
                    NotificationSchedule.DAILY -> {
                        nextAudit = // Set nextAudit to Daily at 12:00PM
                            DateTime().plusDays(1).withHourOfDay(12).withMinuteOfHour(0)
                                .withSecondOfMinute(0)
                    }
                    NotificationSchedule.WEEKLY -> {
                        nextAudit = // Set nextAudit to Weekly at 12:00PM
                            DateTime().plusWeeks(1).withHourOfDay(12).withMinuteOfHour(0)
                                .withSecondOfMinute(0)
                    }
                    NotificationSchedule.MONTHLY -> {
                        nextAudit = // Set nextAudit to Monthly at 12:00PM
                            DateTime().plusMonths(1).withHourOfDay(12).withMinuteOfHour(0)
                                .withSecondOfMinute(0)
                    }
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
            val sharedPreference =
                SharedPreferenceHelper(context)
            if (!sharedPreference.getValueBoolean("Notification", false)) {
                alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(broadcast)
            }
        }
    }

}