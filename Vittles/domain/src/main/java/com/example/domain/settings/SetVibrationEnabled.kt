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
class SetVibrationEnabled @Inject constructor(private val settingsRepository: SettingsRepository) {

    /**
     * TODO ***
     * Calls the repository to set the value of vibration
     *
     * @param enabled The boolean value to be set
     */
    operator fun invoke(enabled: Boolean) {
        settingsRepository.setVibrationEnabled(enabled)
    }
}