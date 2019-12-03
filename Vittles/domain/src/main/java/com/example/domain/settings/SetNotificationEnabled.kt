package com.example.domain.settings

import javax.inject.Inject

/**
 * TODO
 *
 * @property settingsRepository
 */
class SetNotificationEnabled @Inject constructor(private val settingsRepository: SettingsRepository) {

    /**
     * TODO
     *
     * @param isEnabled
     */
    operator fun invoke(isEnabled: Boolean) {
        settingsRepository.setNotificationEnabled(isEnabled)
    }
}