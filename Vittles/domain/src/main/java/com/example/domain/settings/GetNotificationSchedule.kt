package com.example.domain.settings

import com.example.domain.settings.model.NotificationSchedule
import javax.inject.Inject

class GetNotificationSchedule @Inject constructor(private val settingsRepository: SettingsRepository) {

    operator fun invoke(): NotificationSchedule {
        return settingsRepository.getNotificationSchedule()
    }
}