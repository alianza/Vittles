package com.example.domain.settings

import javax.inject.Inject

/**
 * TODO
 *
 * @property settingsRepository
 */
class SetVibrationEnabled @Inject constructor(private val settingsRepository: SettingsRepository) {

    /**
     * TODO
     *
     * @param enabled
     */
    operator fun invoke(enabled: Boolean) {
        settingsRepository.setVibrationEnabled(enabled)
    }
}