package com.example.domain.settings

import javax.inject.Inject

class SetVibrationEnabled @Inject constructor(private val settingsRepository: SettingsRepository) {

    operator fun invoke(enabled: Boolean) {
        settingsRepository.setNotificationEnabled(enabled)
    }
}