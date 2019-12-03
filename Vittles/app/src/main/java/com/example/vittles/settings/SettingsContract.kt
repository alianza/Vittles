package com.example.vittles.settings

import com.example.domain.settings.model.NotificationSchedule


/**
 * MVP Contract for settings overview.
 *
 * @author Fethi Tewelde
 */
interface SettingsContract {
    interface View{
        fun initViews()
        fun setListeners()
    }

    interface Presenter{

        fun onNotificationScheduleChanged(notificationSchedule: NotificationSchedule)
    }
}