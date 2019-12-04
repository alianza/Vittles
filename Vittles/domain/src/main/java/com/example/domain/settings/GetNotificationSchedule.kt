package com.example.domain.settings

import com.example.domain.settings.model.NotificationSchedule
import javax.inject.Inject

/**
 * TODO ***
 *
 * This class handles the business logic of setting data
 *
 * @author Fethi Tewelde
 *
 * @property settingsRepository the SettingsRepository
 */
class GetNotificationSchedule @Inject constructor(private val settingsRepository: SettingsRepository) {

    /**
     * TODO ***
     *
     * @return the Enum for the notification schedule options
     */
    operator fun invoke(): NotificationSchedule {
        return settingsRepository.getNotificationSchedule()
    }
}