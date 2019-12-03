package com.example.domain.settings

import javax.inject.Inject

/**
 * TODO
 *
 * @property settingsRepository
 */
class GetVibrationEnabled @Inject constructor(private val settingsRepository: SettingsRepository) {

    /**
     * TODO
     *
     * @return
     */
    operator fun invoke(): Boolean {
        return settingsRepository.getVibrationEnabled()
    }
}