package com.example.domain.settings

import com.example.domain.settings.model.NotificationSchedule
import javax.inject.Inject

/**
 * TODO
 *
 * @property settingsRepository
 */
class SetNotificationSchedule @Inject constructor(private val settingsRepository: SettingsRepository) {

    /**
     * TODO
     *
     * @param notificationSchedule
     */
    operator fun invoke(notificationSchedule: NotificationSchedule) {
        settingsRepository.setNotificationSchedule(notificationSchedule)
    }
}