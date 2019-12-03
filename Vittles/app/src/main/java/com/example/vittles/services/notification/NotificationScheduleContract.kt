package com.example.vittles.services.notification

import android.content.Context
import com.example.domain.notification.Notification

interface NotificationScheduleContract {

    interface Service {
        fun notify(notification: Notification, context: Context?)
        fun onNotifyFail(error: Throwable)
    }

    interface Presenter {
        fun startPresenting(context: Context)
    }
}