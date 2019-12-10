package com.example.domain.settings

import com.example.domain.settings.model.PerformanceSetting
import javax.inject.Inject

/**
 *This class handles the business logic of setting data
 *
 * @author Fethi Tewelde
 *
 * @property settingsRepository the SettingsRepository
 */
class GetPerformanceSetting @Inject constructor(private val settingsRepository: SettingsRepository) {

    /**
     * @return the Enum for the notification schedule options
     */
    operator fun invoke(): PerformanceSetting {
        return settingsRepository.getPerformanceSetting()
    }
}