package com.example.domain.settings

import javax.inject.Inject

/**
 * TODO ***
 *
 * This class handles the business logic of setting data
 *
 * @author Fethi Tewelde
 *
 * @property settingsRepository The SettingsRepository
 */
class SetNotificationEnabled @Inject constructor(private val settingsRepository: SettingsRepository) {

    /**
     * TODO ***
     * Calls the repository to set the value of notification
     *
     * @param isEnabled The boolean value to be set
     */
    operator fun invoke(isEnabled: Boolean) {
        settingsRepository.setNotificationEnabled(isEnabled)
    }
}