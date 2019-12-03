package com.example.domain.settings

import com.example.domain.settings.model.NotificationSchedule
import javax.inject.Inject

class SetNotificationSchedule @Inject constructor(private val settingsRepository: SettingsRepository) {

    operator fun invoke(notificationSchedule: NotificationSchedule) {
        settingsRepository.setNotificationSchedule(notificationSchedule)
    }
}