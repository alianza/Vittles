package com.example.domain.settings

import com.example.domain.settings.model.NotificationSchedule

interface SettingsRepository {

    fun getNotificationSchedule(): NotificationSchedule
    fun setNotificationSchedule(notificationSchedule: NotificationSchedule)

    fun setNotificationEnabled(enabled: Boolean)
}
