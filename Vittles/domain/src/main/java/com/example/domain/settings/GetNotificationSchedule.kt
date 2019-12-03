package com.example.domain.settings

import com.example.domain.settings.model.NotificationSchedule
import javax.inject.Inject

/**
 * TODO
 *
 * @property settingsRepository
 */
class GetNotificationSchedule @Inject constructor(private val settingsRepository: SettingsRepository) {

    /**
     * TODO
     *
     * @return
     */
    operator fun invoke(): NotificationSchedule {
        return settingsRepository.getNotificationSchedule()
    }
}