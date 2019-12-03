package com.example.vittles.settings

import com.example.domain.settings.SetNotificationSchedule
import com.example.domain.settings.model.NotificationSchedule
import javax.inject.Inject

class SettingsPresenter @Inject constructor(
    private val setNotificationSchedule: SetNotificationSchedule
): SettingsContract.Presenter {

    override fun onNotificationScheduleChanged(notificationSchedule: NotificationSchedule) {
        setNotificationSchedule(notificationSchedule)
    }
}