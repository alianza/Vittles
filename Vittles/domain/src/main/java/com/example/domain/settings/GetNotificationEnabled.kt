package com.example.domain.settings

import javax.inject.Inject

/**
 * TODO
 *
 * @property settingsRepository
 */
class GetNotificationEnabled @Inject constructor(private val settingsRepository: SettingsRepository) {

    /**
     * TODO
     *
     * @return
     */
    operator fun invoke(): Boolean {
        return settingsRepository.getNotificationEnabled()
    }
}