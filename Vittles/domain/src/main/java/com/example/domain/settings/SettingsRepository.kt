package com.example.domain.settings

import com.example.domain.settings.model.NotificationSchedule

interface SettingsRepository {
    fun getNotificationSchedule(): NotificationSchedule
    fun setNotificationSchedule(notificationSchedule: NotificationSchedule)
    fun getNotificationEnabled(): Boolean
    fun setNotificationEnabled(isEnabled: Boolean)
    fun getVibrationEnabled(): Boolean
    fun setVibrationEnabled(isEnabled: Boolean)
}
