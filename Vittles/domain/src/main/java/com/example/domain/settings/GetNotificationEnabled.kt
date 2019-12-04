package com.example.domain.settings

import javax.inject.Inject

/**
 * TODO ***
 *
 * This class handles the business logic of setting data
 *
 * @author Fethi Tewelde
 *
 * @property settingsRepository The SettingsRepository.
 */
class GetNotificationEnabled @Inject constructor(private val settingsRepository: SettingsRepository) {

    /**
     * TODO ***
     *
     * @return the boolean value of notification
     */
    operator fun invoke(): Boolean {
        return settingsRepository.getNotificationEnabled()
    }
}